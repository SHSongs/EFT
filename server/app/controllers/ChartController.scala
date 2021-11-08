package controllers

import javax.inject._
import play.api.mvc._


case class StockData(date: String, open: String, high: String, low: String, close: String, adjClose: String, volume: String)

case class HistoricalData(name: String, start: String, end: String, data: List[StockData])

import utils.YahooFinanceUtil._
import utils.Util._


@Singleton
class ChartController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def chart(ticker: String, period1: String, period2: String) = Action {


    val url = makeYahooFinanceURL(ticker, period1, period2)

    val requestProperties = Map(
      "User-Agent" -> "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)"
    )

    val s = requestServer(url, requestProperties)
    val info = YahooFinanceHtmlToStockData(s)
    for (i <- info) {
      println(i)
    }

    val historicalData = HistoricalData(ticker, period1, period2, info)

    val json = historicalDataToJson(historicalData)

    Ok(json)
  }


}
