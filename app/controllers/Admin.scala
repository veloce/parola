package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models._

object Admin extends Controller {

  val form = Form(
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
    Ok(views.html.index(Parole.all(), form))
  }

  def add = Action { implicit request =>
    form.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Parole.all(), errors)),
      value => {
        Parole.create(value)
        Redirect(routes.Admin.index)
      }
    )
  }

  def edit(date: String) = Action {
    val parole = Parole.find(date)
    Ok(views.html.edit(parole, form.fill(parole)))
  }

  def update(date: String) = Action { implicit request =>
    val parole = Parole.find(date)
    form.bindFromRequest.fold(
      errors => BadRequest(views.html.edit(parole, errors)),
      value => {
        Parole.update(date, value)
        Redirect(routes.Admin.index)
      }
    )
  }

  def delete(date: String) = Action {
    Parole.delete(date)
    Redirect(routes.Admin.index)
  }
  
}
