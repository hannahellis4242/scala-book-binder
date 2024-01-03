package controllers

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class SignaturesController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def get(): Action[AnyContent] = Action { request =>
    Ok(request.session.toString)
  }

  def post(): Action[AnyContent] = TODO
}
