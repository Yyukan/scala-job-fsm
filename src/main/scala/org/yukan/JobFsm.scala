package org.yukan

import akka.actor.{Inbox, ActorSystem}
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
  val inbox = Inbox.create(system)

  val job1 = system.actorOf(Job.props("job1"))

  inbox.send(job1, Commands.Check)

  val message = inbox.receive(5 seconds)
  println(s"Job 1 name $message")
}
