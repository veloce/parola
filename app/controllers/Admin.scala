package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models._

object Admin extends Controller {

  val addForm = Form(
    mapping(
      "publishDate" -> date("yyyy-MM-dd"),
      "title" -> nonEmptyText,
      "comment" -> text,
      "quote" -> nonEmptyText,
      "author" -> nonEmptyText,
      "source" -> nonEmptyText
    )(Parole.apply)(Parole.unapply)
  )
  
  def index = Action {
    Ok(views.html.index(Parole.all(), addForm))
  }

  def add = Action { implicit request =>
    addForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Parole.all(), errors)),
      value => {
        Parole.create(value)
        Redirect(routes.Admin.index)
      }
    )
  }

  def edit(date: String) = TODO

  def delete(date: String) = Action {
    Parole.delete(date)
    Redirect(routes.Admin.index)
  }
  
}
