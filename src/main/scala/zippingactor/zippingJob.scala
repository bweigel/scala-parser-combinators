package zippingactor

import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill, Props}
import zippingactor.fileWatcher.{fileIsAvailable, fileIsUnavailable, isFileAvailable}
import zippingactor.types.S3Location

import scala.util.Random

object zippingJob {

  def props(jobActorId: String): Props = Props(new zippingJob(jobActorId))

  case class zipJobMessage(messageId: String, filesToZip: List[S3Location])

  case class getJobResult(messageId: String)

  case class JobIsDone(messageId: String, s3Location: S3Location)

  case class JobIsPending(messageId: String)

}

class zippingJob(jobActorId: String) extends Actor with ActorLogging {

  import zippingJob._

  var fileWorkers: List[ActorRef] = Nil
  var jobIsDone: Boolean = false
  var zipOutputLocation: Option[S3Location] = None

  override def preStart(): Unit = log.info(s"zipping job actor $jobActorId started ...")

  override def postStop(): Unit = log.info(s"zipping job actor $jobActorId stopped ...")

  override def receive: Receive = {
    case PoisonPill => log.info("Got Poison Pill...")
    case zipJobMessage(messageId, content) =>
      log.info(s"Recieved message '$messageId'")
      fileWorkers = for {
        file <- content
      } yield context.actorOf(fileWatcher.props(messageId, file))
    case getJobResult(messageId) =>
     // log.info("Polling files...")
      fileWorkers.foreach(wrk => wrk ! isFileAvailable(Random.nextString(4)))
      if(jobIsDone) sender() ! JobIsDone(messageId, zipOutputLocation.get) else sender ! JobIsPending(messageId)
    case fileIsAvailable(requestId, fileLocation) => log.info(s"new File available $requestId => ${fileLocation.s3Key}")
    case fileIsUnavailable(requestId) => log.info(s"File not yet available $requestId")

  }

}
