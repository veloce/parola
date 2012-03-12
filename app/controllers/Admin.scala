package controllers

import java.util.Date
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

  def edit(date: Date) = Action {
    Parole.find(date).map { parole =>
      Ok(views.html.edit(parole, form.fill(parole)))
    }.getOrElse(NotFound)
  }

  def update(date: Date) = Action { implicit request =>
    Parole.find(date).map { parole =>
      form.bindFromRequest.fold(
        errors => BadRequest(views.html.edit(parole, errors)),
        value => {
          Parole.update(date, value)
          Redirect(routes.Admin.index)
        }
      )
    }.getOrElse(NotFound)
  }

  def delete(date: Date) = Action {
    Parole.delete(date)
    Redirect(routes.Admin.index)
  }
  
}
