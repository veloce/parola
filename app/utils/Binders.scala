package utils

import play.api.mvc._
import java.util.Date
import java.text._

object Binders {

  implicit def bindableDate = new PathBindable[Date] {

    val dateFormat = new SimpleDateFormat("yyy-MM-dd")

    def bind(key: String, value: String) = {
      try {
        Right(dateFormat.parse(value))
      } catch {
        case e: ParseException => Left("Cannot parse parameter " + key + " as jave.util.Date: " + e.getMessage)
      }
    }

    def unbind(key: String, value: Date): String = {
      dateFormat.format(value)
    }
  }

}
