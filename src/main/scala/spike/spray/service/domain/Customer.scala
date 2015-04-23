package spike.spray.service.domain

import java.util.Date

case class Customer (
  id: Option[Long] = None,
  title: Option[String] = None,
  firstName: Option[String] = None,
  middleName: Option[String] = None,
  lastName: Option[String] = None,
  dateOfBirth: Option[Date] = None
)
