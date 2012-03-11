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
) {

  def publishDateFormatted(): String = {
    val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
    format.format(this.publishDate)
  }
  
}

object Parole {

  val parole = {
    get[Date]("publish_date") ~
    get[String]("title") ~
    get[String]("comment") ~
    get[String]("quote") ~
    get[String]("author") ~
    get[String]("source") map {
      case publish_date ~ title ~ comment ~ quote ~ author ~ source => Parole(
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
    SQL("SELECT * FROM parole").as(parole *)
  }

  def find(date: String): Parole = DB.withConnection { implicit c =>
    SQL("SELECT * FROM parole WHERE publish_date = {publish_date}").on(
      "publish_date" -> date).as(parole.single)
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

  def update(date: String, parole: Parole) {
    DB.withConnection { implicit c =>
      SQL(
        """
          UPDATE parole
          SET title={title}, quote={quote}, comment={comment},
          author={author}, source={source}
          WHERE publish_date={date};
        """
      ).on(
        "date"    -> date,
        "title"   -> parole.title,
        "quote"   -> parole.quote,
        "comment" -> parole.comment,
        "author"  -> parole.author,
        "source"  -> parole.source
      ).executeUpdate()
    }
  }

  def delete(date: String) {
    DB.withConnection { implicit c =>
      SQL("DELETE FROM parole WHERE publish_date={date};").on(
        "date" -> date
      ).executeUpdate()
    }
  }
}
