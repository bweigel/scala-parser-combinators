package zippingactor

import akka.actor.{Actor, ActorLogging, Props}
import zippingactor.types.S3Location

import scala.util.Random

class fileWatcher(messageId: String, fileLocation: S3Location) extends Actor with ActorLogging {

  import fileWatcher._

  override def preStart(): Unit = log.info(s"fileWatcher ($messageId - $fileLocation) started")

  override def postStop(): Unit = log.info(s"fileWatcher ($messageId - $fileLocation) stopped")

  def fileAvailable(): Boolean = Random.nextBoolean()

  override def receive: Receive = {
    case isFileAvailable(requestId) => if (this.fileAvailable()) {
      sender() ! fileIsAvailable(requestId, fileLocation)
    } else {
      sender() ! fileIsUnavailable(requestId)
    }
  }
}

object fileWatcher {
  def props(messageId: String, fileLocation: S3Location): Props = Props(new fileWatcher(messageId, fileLocation))

  def props(messageId: String, s3Bucket: String, s3Key: String): Props = Props(new fileWatcher(messageId, S3Location(s3Bucket, s3Key)))


  case class isFileAvailable(requestId: String)

  case class fileIsAvailable(requestId: String, location: S3Location)

  case class fileIsUnavailable(requestId: String)

}
