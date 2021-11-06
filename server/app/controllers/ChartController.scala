package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class ChartController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {



  def chart(name: String, period1: String, period2: String) = Action {
    Ok(name + period1 + period2)
  }
}
