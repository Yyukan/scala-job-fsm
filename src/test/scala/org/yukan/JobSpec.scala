import java.util.Date

import org.scalatest.{FreeSpecLike, BeforeAndAfterAll, FlatSpecLike, Matchers}
import akka.actor.{ Actor, Props, ActorSystem }
import akka.testkit.{TestFSMRef, ImplicitSender, TestKit, TestActorRef}
import org.yukan.job.Job
import org.yukan.job.Job.Data.JobModel
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

  val testName = "test1"

  def this() = this(ActorSystem("JobFSMSpec"))

  override def afterAll(): Unit = {
    system.shutdown()
    system.awaitTermination(10.seconds)
  }

  class TestedJob {
    val job = TestFSMRef(new Job(testName))
  }

  trait RunningJobState extends TestedJob {
    job.setState(State.Running, Data.Empty)
  }

  "A Job actor" - {
    "should be not started at birth" in new TestedJob {
      job.stateName should be(State.NotStarted)
      job.stateData should be(JobModel(testName, State.NotStarted.toString, None, None))
    }

    "should started by command" in new TestedJob {
      job ! Commands.Start
      job.stateName should be(State.Running)
      job.stateData should be(JobModel(testName, State.Running.toString, Some(new Date(1L)), None))
    }

    "should finished by command" in new RunningJobState {
      job ! Commands.Finish
      job.stateName should be(State.Finished)
      job.stateData should be(JobModel(testName, State.Finished.toString, Some(new Date(1L)), Some(new Date(5L))))
    }

    "should return data in any state" in new RunningJobState {
      job ! Commands.Check
      expectMsg(Data.Empty)
      job.stateName should be(State.Running)
      job.stateData should be(Data.Empty)
    }

  }

}
