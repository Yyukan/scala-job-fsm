package org.yukan

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import org.yukan.job.Job._
import org.yukan.job.Job

/**
 * Job Final State Machine example application
 */
object JobFsm extends App {

  val system = ActorSystem("JobFSM")

  // TODO:yukan rewrite using inbox
  val job1 = system.actorOf(Job.props("job1"))
  implicit val timeout = Timeout(5 seconds)
  val a = job1 ? Commands.Check
  a.onSuccess {
    case result : String ⇒ println("Got result " + result)
  }
}
