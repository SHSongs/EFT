package utils

import play.api.libs.json.{JsObject, Json}

import java.net.URL
import scala.io.Source
import java.text.SimpleDateFormat


case class StockData(date: String, open: String, high: String, low: String, close: String, adjClose: String, volume: String)

case class HistoricalData(name: String, start: String, end: String, data: List[StockData])


object Util {
  def dateToUnixTime(date: String): Long = {
    // date: yyyyMMdd  ex) 20140124
    val dateString = s"$date 00:00:00 GMT"

    val dateFormat = new SimpleDateFormat("yyyyMMdd hh:mm:ss z")
    val unixTime = dateFormat.parse(dateString).getTime / 1000

    unixTime
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

    val json = Json.obj(
      "type" -> "chart",
      "data" -> Json.obj(
        "name" -> historicalData.name,
        "start" -> historicalData.start,
        "end" -> historicalData.end,
        "history" -> data
      )
    )

    json
  }


  def requestServer(URL: String, requestProperties: Map[String, String]): String = {
    val connection = new URL(URL).openConnection
    requestProperties.foreach({
      case (name, value) => connection.setRequestProperty(name, value)
    })

    val s = Source.fromInputStream(connection.getInputStream).getLines().mkString("\n")

    s
  }
}
