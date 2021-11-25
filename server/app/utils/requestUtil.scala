package utils

import java.net.URL
import scala.io.Source

object requestUtil {
  val requestProperties = Map(
    "User-Agent" -> "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)"
  )

  def requestServer(URL: String, requestProperties: Map[String, String]): String = {
    val connection = new URL(URL).openConnection
    requestProperties.foreach({
      case (name, value) => connection.setRequestProperty(name, value)
    })
    Source.fromInputStream(connection.getInputStream).getLines().mkString("\n")
  }
}
