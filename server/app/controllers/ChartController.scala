package controllers


import javax.inject._
import play.api.mvc._
import applications.ChartApplication

@Singleton
class ChartController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def chart(ticker: String, period1: String, period2: String) = Action {
    val app = new ChartApplication
    Ok(app.chart(ticker, period1, period2))
  }
}
