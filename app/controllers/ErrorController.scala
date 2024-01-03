package controllers

import play.api.mvc._

import javax.inject._

@Singleton
class ErrorController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def get(message:String): Action[AnyContent] = Action {
    //TODO improve this
    Ok(s"Error : $message") }
}
