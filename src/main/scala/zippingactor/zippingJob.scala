package zippingactor

import akka.actor.{Actor, ActorLogging, PoisonPill, Props}
import zippingactor.types.S3Location

object zippingJob {

  def props(jobActorId: String): Props = Props(new zippingJob(jobActorId))

  case class zipJobMessage(messageId: String, content: String)

  case class getJobResult(messageId: String)

  case class JobIsDone(messageId: String, s3Location: S3Location)

  case class JobIsPending(messageId: String)

}

class zippingJob(jobActorId: String) extends Actor with ActorLogging {

  import zippingJob._

  var jobIsDone: Boolean = false

  override def preStart(): Unit = log.info(s"zipping job actor $jobActorId started ...")

  override def postStop(): Unit = log.info(s"zipping job actor $jobActorId stopped ...")

  override def receive: Receive = {
    case PoisonPill => log.info("Got Poison Pill...")
    case zipJobMessage(_, content) => {
      log.info(s"Recieved message: '$content'")

    }
  }

}
