package domains

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.texts
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import utils.timeUtil.dateToUnixTime


object YahooFinanceDomain {
  def makeYahooFinanceURL(ticker: String, period1: String, period2: String): Option[String] = {
    val start = dateToUnixTime(period1)
    val end = dateToUnixTime(period2)

    val x: Option[String] = (start, end) match {
      case (Some(s), Some(e)) => {
        val yahooFinanceURL = s"https://finance.yahoo.com/quote/$ticker/history?period1=${s.toString}&period2=${e.toString}&interval=1d&filter=history&frequency=1d&includeAdjustedClose=true"
        Option(yahooFinanceURL)
      }
      case _ => Option.empty
    }
    x
  }

  def yahooFinanceHtmlToStockData(s: String): List[StockData] = {
    val browser = JsoupBrowser()
    val doc = browser.parseString(s)

    val items = doc >> "tbody" >> "tr" >> pElementList

    val x = for {
      i <- items
      text = i >> "td" >> texts("span")
      lst = text.toList
      if (lst.length > 2)
    } yield {
      StockData(lst(0), lst(1), lst(2), lst(3), lst(4), lst(5), lst(6))
    }
    x
  }
}