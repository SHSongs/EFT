package controllers

import javax.inject._
import play.api.mvc._
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.model._

import scala.io.Source
import java.net.URL


@Singleton
class ChartController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {



  def chart(name: String, period1: String, period2: String) = Action {
    val financeURL = s"https://finance.yahoo.com/quote/$name/history?period1=$period1&period2=$period2&interval=1d&filter=history&frequency=1d&includeAdjustedClose=true"

    val requestProperties = Map(
      "User-Agent" -> "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)"
    )

    val s = requestServer(financeURL, requestProperties)
    val info = getInformation(s)
    for (i <- info) {
      println(i)
    }
    Ok(info(0).toString)
  }


  def getInformation(s: String): List[Any] = {
    val browser = JsoupBrowser()
    val doc = browser.parseString(s)

    val items = doc >> "tbody" >> "tr" >> pElementList
    val x = for (item <- items) yield item >> "td" >> texts("span")

    x
  }

  def requestServer(URL: String, requestProperties: Map[String, String]): String = {

    val connection = new URL(URL).openConnection
    requestProperties.foreach({
      case (name, value) => connection.setRequestProperty(name, value)
    })

    val s = Source.fromInputStream(connection.getInputStream).getLines.mkString("\n")

    s
  }
}
