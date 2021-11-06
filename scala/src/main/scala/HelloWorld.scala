import net.ruippeixotog.scalascraper.browser.JsoupBrowser

import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._

import net.ruippeixotog.scalascraper.model._


import io.Source
import java.net.URL

object HelloWorld {
  def main(args: Array[String]) {
    val name = "QQQ"
    val period1 = "1634256000"
    val period2 = "1635984000"

    val financeURL = s"https://finance.yahoo.com/quote/$name/history?$period1=1634256000&$period2=1635984000&interval=1d&filter=history&frequency=1d&includeAdjustedClose=true"

    val requestProperties = Map(
      "User-Agent" -> "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)"
    )

    val s = requestServer(financeURL, requestProperties)
    val info = getInformation(s)
    for (i <- info) {
      println(i)
    }

  }

  def getInformation(s: String): List[Any] = {
    val browser = JsoupBrowser()
    val doc = browser.parseString(s)

    val items = doc >> "tbody" >> "tr" >> pElementList
    val x = for (item <- items) yield item >> "td" >> texts("span")

    x
  }

  def requestServer(URL: String, requestProperties: Map[String, String]): String = {

    val connection = new URL(URL).openConnection
    requestProperties.foreach({
      case (name, value) => connection.setRequestProperty(name, value)
    })

    val s = Source.fromInputStream(connection.getInputStream).getLines.mkString("\n")

    s
  }


}