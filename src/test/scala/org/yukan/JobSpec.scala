import org.scalatest.{FreeSpecLike, BeforeAndAfterAll, FlatSpecLike, Matchers}
import akka.actor.{ Actor, Props, ActorSystem }
import akka.testkit.{TestFSMRef, ImplicitSender, TestKit, TestActorRef}
import org.yukan.job.Job
import org.yukan.job.Job._
import scala.concurrent.duration._

class JobSpec(_system: ActorSystem) extends TestKit(_system)
  with ImplicitSender
  with Matchers
  with FreeSpecLike
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("JobFSMSpec"))

  override def afterAll: Unit = {
    system.shutdown()
    system.awaitTermination(10.seconds)
  }

  "A Job actor" - {
    "should be not started at birth" in {
      val job = TestFSMRef(new Job)
      job.stateName should be(State.NotStarted)
      job.stateData should be(Data.Empty)
    }
  }

}
