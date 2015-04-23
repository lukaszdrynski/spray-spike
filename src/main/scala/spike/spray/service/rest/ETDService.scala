package spike.spray.service.rest

import akka.actor.Actor
import spray.routing.{RequestContext, HttpService}
import spray.http.MediaTypes._
import akka.event.slf4j.SLF4JLogging
import spray.http.{StatusCode, StatusCodes}
import spike.spray.service.domain.{Failure, Customer}


// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class ETDServiceActor extends Actor with ETDHttpService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  implicit def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}

// this trait defines our service behavior independently from the service actor
trait ETDHttpService extends HttpService with SLF4JLogging {

  import spray.httpx.SprayJsonSupport.sprayJsonUnmarshaller
  import spike.spray.service.mapping.ETDJsonProtocol._

  val myRoute =
    path("") {
      get {
        respondWithMediaType(`text/html`) { // XML is marshalled to `text/xml` by default, so we simply override here
          log.info("hello world")
          complete {
            <html>
              <body>
                <h1>Say hello to <i>spray-routing</i>!</h1>
              </body>
            </html>
          }
        }
      }
    } ~
    path("test") {
      get {
        respondWithMediaType(`text/html`) {

          log.info("do nothing")
          complete {
            <html>
              <body>
                <h1>eeeeeeiii!</h1>
              </body>
            </html>
          }
        }
      }
    } ~
    path("customers") {
      post {
        entity(as[Customer]) {
          customer: Customer =>
            ctx: RequestContext =>
              handleRequest(ctx, StatusCodes.Created) {
                log.info("Creating customer: %s".format(customer))
                //customerService.create(customer)
                Right(customer)
              }
        }
      }
    } ~
    path("customers" / LongNumber) {
      customerId =>
        get {
          ctx: RequestContext =>
            handleRequest(ctx) {
              log.info("Retrieving customer with id %d".format(customerId))
              //customerService.get(customerId)
              Right(
                Customer(
                  id = Some(customerId),
                  title = Some("Mr"),
                  firstName = Some("John"),
                  middleName = Some("T"),
                  lastName = Some("Smith"),
                  dateOfBirth = None
                )
              )
            }
        }
    }


  /**
   * Handles an incoming request and create valid response for it.
   *
   * @param ctx         request context
   * @param successCode HTTP Status code for success
   * @param action      action to perform
   */
  protected def handleRequest(ctx: RequestContext, successCode: StatusCode = StatusCodes.OK)(action: => Either[Failure, _]) {
    import spray.json._

    action match {
      case Right(result: Customer) =>
        ctx.complete( result.toJson.compactPrint)
      case Left(error: Failure) =>
        ctx.complete(error.getStatusCode, (Map("error" -> error.message)).toJson.compactPrint)
      case _ =>
        ctx.complete(StatusCodes.InternalServerError)
    }
  }
}