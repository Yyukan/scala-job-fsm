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
  }
}

/**
 * Created by oshtykhno on 22/09/14.
 */
class Job extends FSM[Job.State, Job.Data] {
  import Job._

  /** initialize with not started state */
  startWith(State.NotStarted, Data.Empty)

  when(State.Running)(FSM.NullFunction)

  /** go to running state on start */
  when(State.NotStarted) {
    case Event(Commands.Start, Data.Empty) =>
      goto(State.Running)
  }
}
