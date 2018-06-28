package zippingactor

import akka.actor.{Actor, ActorLogging, PoisonPill, Props}
import zippingactor.zippingJob.zipJobMessage

class zippingSupervisor extends Actor with ActorLogging {
  override def preStart(): Unit = log.info("Zipping Supervisor started")

  override def postStop(): Unit = log.info("Zipping Supervisor stopped")

  private val zipper1 = context.actorOf(zippingJob.props("zipper1"), "zipper-1")

  override def receive: Receive = {
    case s: String => {
      zipper1 ! zipJobMessage("id", s)
      zipper1 ! PoisonPill
    }
  }

}

object zippingSupervisor {
  def props(): Props = Props(new zippingSupervisor())
}
