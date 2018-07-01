package zippingactor

import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill, Props}
import zippingactor.fileWatcher.{fileIsAvailable, fileIsUnavailable, isFileAvailable}
import zippingactor.types.S3Location

import scala.util.Random

object zippingJob {

  def props(jobActorId: String, filesToZip: List[S3Location]): Props = Props(new zippingJob(jobActorId, filesToZip))

  case class getJobResult(requestId: String)

  case class JobIsDone(requestId: String, s3Location: S3Location)

  case class JobIsPending(requestId: String)

}

class zippingJob(messageId: String, filesToZip: List[S3Location], zipOutputLocation: S3Location) extends Actor with ActorLogging {

  import zippingJob._

  val fileWorkers: List[ActorRef] = for {
    file <- filesToZip
  } yield context.actorOf(fileWatcher.props(messageId, file))

  private var filesProcessed: List[S3Location] = Nil

  def allFilesAvailable: Boolean = (for {
    a <- filesToZip
  } yield filesProcessed contains a).reduce(_ && _)

  var jobIsDone: Boolean = false

  def addAvailableFile(file: S3Location): Unit = {
    filesProcessed = file :: filesProcessed
  }

  override def preStart(): Unit = log.info(s"zipping job actor for message $messageId started ...")

  override def postStop(): Unit = log.info(s"zipping job actor for message $messageId stopped ...")

  override def receive: Receive = {
    case getJobResult(requestId) =>
      if (!allFilesAvailable) fileWorkers.foreach(wrk => wrk ! isFileAvailable(requestId))
      if (jobIsDone) sender() ! JobIsDone(requestId, zipOutputLocation) else sender ! JobIsPending(requestId)
    case fileIsAvailable(requestId, fileLocation) =>
      log.info(s"new File available $requestId => ${fileLocation.s3Key}")
      addAvailableFile(fileLocation)

    case fileIsUnavailable(requestId) => log.debug(s"File not yet available $requestId")

  }

}
