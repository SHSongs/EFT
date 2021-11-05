import net.ruippeixotog.scalascraper.browser.JsoupBrowser

import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._

import net.ruippeixotog.scalascraper.model._


import io.Source
import java.net.URL


object HelloWorld {
  def main(args: Array[String]) {


    val financeURL = "https://finance.yahoo.com/quote/QQQ/history?period1=1634256000&period2=1635984000&interval=1d&filter=history&frequency=1d&includeAdjustedClose=true"

    val requestProperties = Map(
      "User-Agent" -> "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)"
    )

    val connection = new URL(financeURL).openConnection
    requestProperties.foreach({
      case (name, value) => connection.setRequestProperty(name, value)
    })

    val s = Source.fromInputStream(connection.getInputStream).getLines.mkString("\n")

    val browser = JsoupBrowser()
    val doc = browser.parseString(s)

    val items = doc >> "tbody" >> "tr" >> pElementList
    println(items)


  }
}