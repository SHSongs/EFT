package utils

import controllers.StockData
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.texts

import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._

import utils.Util.dateToUnixTime

object YahooFinanceUtil {

  def makeYahooFinanceURL(ticker: String, period1: String, period2: String): String = {
    val start = dateToUnixTime(period1).toString
    val end = dateToUnixTime(period2).toString

    val yahooFinanceURL = s"https://finance.yahoo.com/quote/$ticker/history?period1=$start&period2=$end&interval=1d&filter=history&frequency=1d&includeAdjustedClose=true"

    yahooFinanceURL
  }

  def YahooFinanceHtmlToStockData(s: String): List[StockData] = {
    val browser = JsoupBrowser()
    val doc = browser.parseString(s)

    val items = doc >> "tbody" >> "tr" >> pElementList
    val x = for (i <- items) yield {
      val text = i >> "td" >> texts("span")
      val lst = text.toList
      if (lst.length > 2) {
        StockData(lst(0), lst(1), lst(2), lst(3), lst(4), lst(5), lst(6))
      }
      else {
        StockData(lst(0), lst(1), lst(1), lst(1), lst(1), lst(1), lst(1))
      }
    }

    x
  }
}