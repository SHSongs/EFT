package controllers

import javax.inject._
import play.api.mvc._

import utils.YahooFinanceUtil.makeYahooFinanceURL
import utils.YahooFinanceUtil.yahooFinanceHtmlToStockData
import utils.Util.requestServer
import utils.Util.historicalDataToJson
import utils.HistoricalData


@Singleton
class ChartController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def chart(ticker: String, period1: String, period2: String) = Action {
    val url = makeYahooFinanceURL(ticker, period1, period2)

    val requestProperties = Map(
      "User-Agent" -> "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)"
    )

    val s = requestServer(url, requestProperties)
    val info = yahooFinanceHtmlToStockData(s)

    for (i <- info) println(i)

    val historicalData = HistoricalData(ticker, period1, period2, info)
    val json = historicalDataToJson(historicalData)

    Ok(json)
  }
}
