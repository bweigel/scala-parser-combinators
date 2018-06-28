package zippingactor

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import zippingactor.types.S3Location

class fileWatcherTest(_system: ActorSystem) extends TestKit(_system) with Matchers with WordSpecLike with BeforeAndAfterAll {


  def this() = this(ActorSystem("AkkaQuickstartSpec"))

  override def afterAll: Unit = {
    shutdown(system)
  }

  "A fileWatcher Actor" should {
    "reply with file availabiltiy" in {
      import fileWatcher._

      val probe = TestProbe()
      val fileWatcherActor = system.actorOf(fileWatcher.props("test", S3Location("bucket", "key")))

      fileWatcherActor.tell(isFileAvailable("id-123"), probe.ref)
      val response = probe.expectMsgType[fileIsUnavailable]

      response.requestId should ===("id-123")
    }
  }
}
