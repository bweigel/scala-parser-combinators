package zippingactor

import akka.actor.{ActorRef, ActorSystem, PoisonPill}

import scala.io.StdIn

object zippingService extends App {

  val zippingSystem: ActorSystem = ActorSystem("Zipping-Service")

  val zipper: ActorRef = zippingSystem.actorOf(zippingSupervisor.props(), "zipping-supervisor")

  zipper ! "Hello World"

  println(">>> Press ENTER to exit <<<")
  try StdIn.readLine()
  finally zippingSystem.terminate()

}
