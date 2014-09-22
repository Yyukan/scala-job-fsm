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
}

/**
 * Created by oshtykhno on 22/09/14.
 */
class Job extends FSM[Job.State, Job.Data] {
  import Job._
  startWith(State.NotStarted, Data.Empty)
}
