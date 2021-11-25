package domains

case class StockData(date: String, open: String, high: String, low: String, close: String, adjClose: String, volume: String)

case class HistoricalData(name: String, start: String, end: String, data: List[StockData])