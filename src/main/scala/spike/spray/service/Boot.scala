package spike.spray.service

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import spike.spray.service.rest.ETDServiceActor

object Boot extends App with Configuration {

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("on-spray-spike")

  // create and start our service actor
  val service = system.actorOf(Props[ETDServiceActor], "demo-service")

  implicit val timeout = Timeout(5.seconds)
  // start a new HTTP server with our service actor as the handler
  IO(Http) ? Http.Bind(service, serviceHost, servicePort)
}
