package utils

import play.api.mvc._
import java.util.Date
import java.text._

object Binders {

  implicit def bindableDate = new PathBindable[Date] {
    def bind(key: String, value: String) = {
      try {
        val parser = new SimpleDateFormat("yyy-MM-dd")
        Right(parser.parse(value))
      } catch {
        case e: ParseException => Left("Cannot parse parameter " + key + " as jave.util.Date: " + e.getMessage)
      }
    }

    def unbind(key: String, value: Date): String = {
      val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
      format.format(value)
    }
  }

}
