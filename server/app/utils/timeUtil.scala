package utils

import java.text.SimpleDateFormat

object timeUtil {
  def dateToUnixTime(date: String): Option[Long] = {
    val dateString = s"$date 00:00:00 GMT"
    val dateFormat = new SimpleDateFormat("yyyyMMdd hh:mm:ss z")

    try {
      Option(dateFormat.parse(dateString).getTime / 1000)
    } catch {
      case ex: java.text.ParseException => Option.empty
    }
  }
}
