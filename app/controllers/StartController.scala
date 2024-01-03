package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class StartController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def get(): Action[AnyContent] = Action {
    Ok(views.html.start())
  }

  def post(): Action[AnyContent] = Action { Redirect(routes.PagesController.get()).withNewSession }
}
