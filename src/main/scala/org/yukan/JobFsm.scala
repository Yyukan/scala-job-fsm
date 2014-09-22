package org.yukan

import akka.actor.{ActorRef, Inbox, ActorSystem}
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

  val job = system.actorOf(Job.props("job1"))

  send(job, Commands.Check)

  job ! Commands.Start
  send(job, Commands.Check)

  job ! Commands.Finish
  send(job, Commands.Check)

  def send(job: ActorRef, command : AnyRef) {
    inbox.send(job, command)
    val message = inbox.receive(5 seconds)
    println(s"Job is $message")
  }
}
