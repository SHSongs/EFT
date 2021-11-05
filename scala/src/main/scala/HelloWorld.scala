import net.ruippeixotog.scalascraper.browser.JsoupBrowser

import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._

import net.ruippeixotog.scalascraper.model._


object HelloWorld {
  def main(args: Array[String]) {

    val browser = JsoupBrowser()
    val doc = browser.parseFile("example.html")
    // Extract the text inside the element with id "header"
    doc >> text("#header")

    // Extract the <span> elements inside #menu
    val items = doc >> elementList("#menu span")

    // From each item, extract all the text inside their <a> elements
    items.map(_ >> allText("a"))

    // From the meta element with "viewport" as its attribute name, extract the
    // text in the content attribute
    doc >> attr("content")("meta[name=viewport]")


  }
}