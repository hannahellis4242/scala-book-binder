package controllers

import play.api.mvc._
import javax.inject._

@Singleton
class StartController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def get: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.start())
  }

  def post: Action[AnyContent] = Action {
    Redirect(routes.PagesController.get()).withNewSession
  }
}
