package utils

import domains.HistoricalData
import play.api.libs.json.{JsObject, Json}


object jsonUtil {
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
}
