package controllers

import javax.inject._
import play.api.mvc._
import utils.YahooFinanceUtil.{makeYahooFinanceURL, yahooFinanceHtmlToStockData}
import utils.Util.{historicalDataToJson, makeJson, requestProperties, requestServer}
import utils.HistoricalData


@Singleton
class ChartController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def chart(ticker: String, period1: String, period2: String) = Action {
    val url = makeYahooFinanceURL(ticker, period1, period2)

    if (url.isEmpty) {
      Ok(makeJson("chart", "날짜가 잘못 입력되었습니다."))
    }
    else {
      val s = requestServer(url.get, requestProperties)
      val info = yahooFinanceHtmlToStockData(s)

      val historicalData = HistoricalData(ticker, period1, period2, info)
      val json = historicalDataToJson(historicalData)

      Ok(json)
    }
  }
}
