package controllers

import play.api._
import play.api.mvc._

object Site extends Controller {
  
  def index = Action {
    Ok("Hello, World")
  }
  
}
