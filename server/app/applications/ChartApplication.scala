package applications

import domains.HistoricalData
import domains.YahooFinanceDomain.{makeYahooFinanceURL, yahooFinanceHtmlToStockData}
import play.api.libs.json.JsObject
import utils.jsonUtil.{historicalDataToJson, makeJson}
import utils.requestUtil.{requestProperties, requestServer}

class ChartApplication {
  def chart(ticker: String, period1: String, period2: String): JsObject = {
    val url = makeYahooFinanceURL(ticker, period1, period2)

    if (url.isEmpty) {
      makeJson("chart", "날짜가 잘못 입력되었습니다.")
    }
    else {
      val s = requestServer(url.get, requestProperties)
      val info = yahooFinanceHtmlToStockData(s)

      val historicalData = HistoricalData(ticker, period1, period2, info)
      val json = historicalDataToJson(historicalData)
      json
    }
  }
}
