package spike.spray.service.mapping

import spray.json.DefaultJsonProtocol
import spray.json.RootJsonFormat
import java.util.Date
import spray.json.JsValue
import spray.json.JsString
import spike.spray.service.domain.Customer
import akka.event.slf4j.SLF4JLogging

object ETDJsonProtocol extends DefaultJsonProtocol with SLF4JLogging{

  implicit val DateFormat = new RootJsonFormat[Date] {
    lazy val formatter = new java.text.SimpleDateFormat("yyyy-MM-dd")
    def read(json: JsValue) :Date = formatter.parse(json.convertTo[String])
    def write(date: Date) =  JsString(formatter.format(date))
  }

  implicit val customerFormat = jsonFormat6(Customer)
}
