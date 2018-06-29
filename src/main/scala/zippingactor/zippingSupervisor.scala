package zippingactor

import akka.actor.{Actor, ActorLogging, PoisonPill, Props}
import zippingactor.types.S3Location
import zippingactor.zippingJob.{getJobResult, zipJobMessage}

class zippingSupervisor extends Actor with ActorLogging {

  import zippingSupervisor._

  val messageId = "id-1234"

  override def preStart(): Unit = log.info("Zipping Supervisor started")

  override def postStop(): Unit = log.info("Zipping Supervisor stopped")

  private val zipper1 = context.actorOf(zippingJob.props("zipper1"), "zipper-1")

  override def receive: Receive = {
    case ls: List[S3Location] => zipper1 ! zipJobMessage(messageId, ls)
    case statusUpdate => zipper1 ! getJobResult(messageId)
  }

}

object zippingSupervisor {
  def props(): Props = Props(new zippingSupervisor())

  case object statusUpdate

}
