package controllers

import play.api._
import play.api.mvc._

import models._

object Site extends Controller {
  
  def index = Action {
    Ok(Parole.latestPublished.title)
  }
  
}
