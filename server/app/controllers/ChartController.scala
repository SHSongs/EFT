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

import play.api.libs.json._
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._


case class StockData(date: String, open: String, high: String, low: String, close: String, adjClose: String, volume: String)

case class HistoricalData(name: String, start: String, end: String, data: List[StockData])

object JsonExample {

  val data = StockData("2021-11-03", "24.980000", "24.990000", "24.980000", "24.990000", "24.990000", "700")
  val historicalData = HistoricalData("QQQ", "1010", "2020", List(data, data, data))

  val json =
    ("chart" ->
      ("name" -> historicalData.name) ~
        ("start" -> historicalData.start) ~
        ("end" -> historicalData.end) ~

        ("history" ->
          historicalData.data.map { w =>
            (("Date" -> w.date) ~
              ("Open" -> w.open) ~
              ("High" -> w.high) ~
              ("Low" -> w.low) ~
              ("Close" -> w.close) ~
              ("Adj Close" -> w.adjClose) ~
              ("Volume" -> w.volume))
          })
      )

  def main(args: Array[String]): Unit = {
    println(compact(render(json)))
  }
}


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

    val scalaMap = Map("Abc" -> "V")

    val json = Json.toJson(scalaMap)

    println(json)

    Ok(json)
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
