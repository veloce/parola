package models

import java.util.Date

import anorm._
import anorm.SqlParser._

import play.api.db._
import play.api.Play.current

case class Parole(

  publishDate: Date,

  title: String,

  comment: String,

  quote: String,

  author: String,

  source: String
)

object Parole {

  val parser = {
    get[Date]("publish_date") ~
    get[String]("title") ~
    get[String]("comment") ~
    get[String]("quote") ~
    get[String]("author") ~
    get[String]("source") map {
      case publish_date~title~comment~quote~author~source => Parole(
        publish_date,
        title,
        comment,
        quote,
        author,
        source
      )
    }
  }

  def all(): List[Parole] = DB.withConnection { implicit c =>
    SQL("SELECT * FROM parole").as(parser *)
  }

  def allPublished(): List[Parole] = DB.withConnection { implicit c =>
    SQL("SELECT * FROM parole WHERE publish_date <= {today}").on(
      "today" -> new Date).as(parser *)
  }

  def allNotPublished(): List[Parole] = DB.withConnection { implicit c =>
    SQL("SELECT * FROM parole WHERE publish_date > {today}").on(
      "today" -> new Date).as(parser *)
  }

  def latestPublished(): Parole = DB.withConnection { implicit c =>
    SQL(
      """
        SELECT * FROM parole WHERE publish_date <= {today}
        ORDER BY publish_date DESC LIMIT 1
      """
      ).on("today" -> new Date).as(parser.single)

  }

  def find(date: Date): Option[Parole] = {
    DB.withConnection { implicit c =>
    SQL("SELECT * FROM parole WHERE publish_date = {publish_date}").on(
      "publish_date" -> date).as(Parole.parser.singleOpt)
    }
  }

  def findPublished(date: Date): Option[Parole] = {
    DB.withConnection { implicit c =>
      SQL(
        """
          SELECT * FROM parole WHERE publish_date <= {today}
          AND publish_date = {publish_date}
        """
        ).on("today" -> new Date, "publish_date" -> date).as(
          Parole.parser.singleOpt
        )
    }
  }

  def findNotPublished(date: Date): Option[Parole] = {
    DB.withConnection { implicit c =>
      SQL(
        """
          SELECT * FROM parole WHERE publish_date > {today}
          AND publish_date = {publish_date}
        """
        ).on("today" -> new Date, "publish_date" -> date).as(
          Parole.parser.singleOpt
        )
    }
  }

  def create(parole: Parole) {
    DB.withConnection { implicit c =>
      SQL(
        """
          INSERT INTO parole
          (publish_date, title, quote, comment, author, source)
          VALUES
          ({publish_date}, {title}, {quote}, {comment}, {author}, {source});
        """
      ).on(
        "publish_date" -> parole.publishDate,
        "title"        -> parole.title,
        "quote"        -> parole.quote,
        "comment"      -> parole.comment,
        "author"       -> parole.author,
        "source"       -> parole.source
      ).executeUpdate()
    }
  }

  def update(date: Date, parole: Parole) {
    DB.withConnection { implicit c =>
      SQL(
        """
          UPDATE parole
          SET title={title}, quote={quote}, comment={comment},
          author={author}, source={source}, publish_date={publish_date}
          WHERE publish_date={date};
        """
      ).on(
        "date"         -> date,
        "title"        -> parole.title,
        "quote"        -> parole.quote,
        "comment"      -> parole.comment,
        "author"       -> parole.author,
        "source"       -> parole.source,
        "publish_date" -> parole.publishDate
      ).executeUpdate()
    }
  }

  def delete(date: Date) {
    DB.withConnection { implicit c =>
      SQL("DELETE FROM parole WHERE publish_date={date};").on(
        "date" -> date
      ).executeUpdate()
    }
  }

}
