import org.scalatest.{FreeSpecLike, BeforeAndAfterAll, FlatSpecLike, Matchers}
import akka.actor.{ Actor, Props, ActorSystem }
import akka.testkit.{TestFSMRef, ImplicitSender, TestKit, TestActorRef}
import org.yukan.job.Job
import org.yukan.job.Job._
import scala.concurrent.duration._

/**
 * Test spec for Job FSM
 */
class JobSpec(_system: ActorSystem) extends TestKit(_system)
  with ImplicitSender
  with Matchers
  with FreeSpecLike
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("JobFSMSpec"))

  override def afterAll(): Unit = {
    system.shutdown()
    system.awaitTermination(10.seconds)
  }

  class TestedJob {
    val job = TestFSMRef(new Job("test1"))
  }

  trait RunningJobState extends TestedJob {
    job.setState(State.Running, Data.Empty)
  }

  "A Job actor" - {
    "should be not started at birth" in new TestedJob {
      job.stateName should be(State.NotStarted)
      job.stateData should be(Data.Empty)
    }

    "should started by command" in new TestedJob {
      job ! Commands.Start
      job.stateName should be(State.Running)
      job.stateData should be(Data.Empty)
    }


    "should finished by command" in new RunningJobState {
      job ! Commands.Finish
      job.stateName should be(State.Finished)
      job.stateData should be(Data.Empty)
    }

  }

}
