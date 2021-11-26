package domains

abstract class StockProvider {
  def makeURL(ticker: String, period1: String, period2: String): Option[String]

  def htmlToStockData(s: String): List[StockData]
}
