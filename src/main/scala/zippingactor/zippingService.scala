package zippingactor

import akka.actor.{ActorRef, ActorSystem, PoisonPill}
import zippingactor.types.S3Location
import zippingactor.zippingSupervisor.statusUpdate

import scala.io.StdIn

object zippingService extends App {

  val zippingSystem: ActorSystem = ActorSystem("Zipping-Service")

  val zipper: ActorRef = zippingSystem.actorOf(zippingSupervisor.props(), "zipping-supervisor")

  val bucket = "eigelb.projects"
  val input = List(S3Location(bucket, "test1"), S3Location(bucket, "test2"), S3Location(bucket, "test3"))

  zipper ! input

  while (true) {
    Thread.sleep(5000L)
    zipper ! statusUpdate
  }

  println(">>> Press ENTER to exit <<<")
  try StdIn.readLine()
  finally zippingSystem.terminate()

}
