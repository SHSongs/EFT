package controllers

import domains.HistoricalData

import javax.inject._
import play.api.mvc._
import domains.YahooFinanceDomain.{makeYahooFinanceURL, yahooFinanceHtmlToStockData}
import utils.jsonUtil.{historicalDataToJson, makeJson}
import utils.requestUtil.{requestProperties, requestServer}


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
