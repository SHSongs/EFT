package utils

import play.api.libs.json.{JsObject, Json}

import java.net.URL
import scala.io.Source
import java.text.SimpleDateFormat


case class StockData(date: String, open: String, high: String, low: String, close: String, adjClose: String, volume: String)

case class HistoricalData(name: String, start: String, end: String, data: List[StockData])


object Util {
  val requestProperties = Map(
    "User-Agent" -> "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)"
  )

  def dateToUnixTime(date: String): Option[Long] = {
    // date: yyyyMMdd  ex) 20140124
    val dateString = s"$date 00:00:00 GMT"
    val dateFormat = new SimpleDateFormat("yyyyMMdd hh:mm:ss z")

    try {
      Option(dateFormat.parse(dateString).getTime / 1000)
    } catch {
      case ex: java.text.ParseException => Option.empty
    }
  }

  def historicalDataToJson(historicalData: HistoricalData): JsObject = {
    val data = historicalData.data.map { w =>
      Json.obj("Date" -> w.date,
        "Open" -> w.open,
        "High" -> w.high,
        "Low" -> w.low,
        "Close" -> w.close,
        "Adj Close" -> w.adjClose,
        "Volume" -> w.volume)
    }

    Json.obj(
      "type" -> "chart",
      "data" -> Json.obj(
        "name" -> historicalData.name,
        "start" -> historicalData.start,
        "end" -> historicalData.end,
        "history" -> data
      )
    )
  }

  def makeJson(data_type: String, data: String): JsObject = {
    Json.obj(
      "type" -> data_type,
      "data" -> data
    )
  }

  def requestServer(URL: String, requestProperties: Map[String, String]): String = {
    val connection = new URL(URL).openConnection
    requestProperties.foreach({
      case (name, value) => connection.setRequestProperty(name, value)
    })
    Source.fromInputStream(connection.getInputStream).getLines().mkString("\n")
  }
}
