package org.yukan.job

import akka.actor.FSM
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
  }

  object Commands {
    case object Start
    case object Finish
    case object Check
  }
}

/**
 * Job is an FSM Actor
 *
 * @param name - every job has a name
 */
class Job(name : String) extends FSM[Job.State, Job.Data] {

  import Job._

  /** initialize with not started state */
  startWith(State.NotStarted, Data.Empty)

  when(State.Running)(FSM.NullFunction)
  when(State.Finished)(FSM.NullFunction)

  /** go to running state on start */
  when(State.NotStarted) {
    case Event(Commands.Start, Data.Empty) =>
      goto(State.Running)
  }

  when(State.Running) {
    case Event(Commands.Finish, Data.Empty) =>
      goto(State.Finished)
  }

  whenUnhandled {
    /** on check send name to sender */
    case Event(Commands.Check,_) =>
      sender() ! name
      stay()
  }

}