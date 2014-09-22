package org.yukan.job

import java.util.Date

import akka.actor.{Props, FSM}
import org.yukan.job.Job.Data.JobModel
import scala.concurrent.duration._

/** Job has state and data */
object Job {
  sealed trait State
  sealed trait Data

  object State {
    case object NotStarted extends State
    case object Running extends State
    case object Finished extends State
  }

  object Data {
    case object Empty extends Data
    case class JobModel(name: String, state: String, start: Option[Date], finished: Option[Date]) extends Data
  }

  object Commands {
    case object Start
    case object Finish
    case object Check
  }

  def props(name: String) = Props(classOf[Job], name)
}

/**
 * Job is an FSM Actor
 *
 * @param name - every job has a name
 */
class Job(name : String) extends FSM[Job.State, Job.Data] {

  import Job._

  /** initialize with not started state */
  startWith(State.NotStarted, JobModel(name, State.NotStarted.toString, None, None))

  /** go to running state on start command */
  when(State.NotStarted) {
    case Event(Commands.Start, _) =>
      goto(State.Running) using JobModel(name, State.Running.toString, Some(new Date(1L)), None)
  }

  /** go to finish state on finish command */
  when(State.Running) {
    case Event(Commands.Finish, _) =>
      goto(State.Finished) using JobModel(name, State.Finished.toString, Some(new Date(1L)), Some(new Date(5L)))
  }

  /** do nothing in finish state */
  when(State.Finished)(FSM.NullFunction)

  whenUnhandled {
    /** on check send name to sender */
    case Event(Commands.Check, _) =>
      sender() ! stateData
      stay()
  }

}