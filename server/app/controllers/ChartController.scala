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
import play.api.libs.json.Json


case class StockData(date: String, open: String, high: String, low: String, close: String, adjClose: String, volume: String)

case class HistoricalData(name: String, start: String, end: String, data: List[StockData])



@Singleton
class ChartController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {


  def chart(ticker: String, period1: String, period2: String) = Action {

    val start = dateToUnixTime(period1).toString
    val end = dateToUnixTime(period2).toString

    val financeURL = s"https://finance.yahoo.com/quote/$ticker/history?period1=$start&period2=$end&interval=1d&filter=history&frequency=1d&includeAdjustedClose=true"

    val requestProperties = Map(
      "User-Agent" -> "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)"
    )

    val s = requestServer(financeURL, requestProperties)
    val info = getInformation(s)

    val historicalData = HistoricalData(ticker, period1, period2, info)


    for (i <- info) {
      println(i)
    }

    val data = historicalData.data.map { w =>
      Json.obj("Date" -> w.date,
        "Open" -> w.open,
        "High" -> w.high,
        "Low" -> w.low,
        "Close" -> w.close,
        "Adj Close" -> w.adjClose,
        "Volume" -> w.volume)
    }
    val json = Json.obj(
      "chart" -> Json.obj(
        "name" -> historicalData.name,
        "start" -> historicalData.start,
        "end" -> historicalData.end,
        "history" -> data
      )
    )


    Ok(json)
  }

  def dateToUnixTime(date: String): Long = {
    // date: yyyyMMdd  ex) 20140124

    val dateString = s"$date 00:00:00 GMT"

    import java.text.SimpleDateFormat
    val dateFormat = new SimpleDateFormat("yyyyMMdd hh:mm:ss z")
    val unixTime = dateFormat.parse(dateString).getTime/ 1000

    unixTime
  }

  def getInformation(s: String): List[StockData] = {
    val browser = JsoupBrowser()
    val doc = browser.parseString(s)

    val items = doc >> "tbody" >> "tr" >> pElementList
    val x = for (i <- items) yield {
      val text = i >> "td" >> texts("span")
      val lst = text.toList
      if (lst.length > 2){
        StockData(lst(0), lst(1), lst(2), lst(3), lst(4), lst(5), lst(6))
      }
      else {
        StockData(lst(0), lst(1), lst(1), lst(1), lst(1), lst(1), lst(1))
      }
    }

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
