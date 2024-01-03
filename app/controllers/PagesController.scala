package controllers

import play.api.mvc._

import javax.inject._

@Singleton
class PagesController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def get: Action[AnyContent] = Action {
    Ok(views.html.pages())
  }

  def post: Action[AnyContent] = Action { request =>
    request.body.asFormUrlEncoded
      .flatMap(values => {
        values.get("pages")
          .flatMap(_.headOption)
          .flatMap(pages => pages.toIntOption)
          .map(pages => Redirect(routes.MaxController.get()).withSession("pages" -> pages.toString))
      })
      .getOrElse(Redirect(routes.ErrorController.get("could not find form value pages")))
  }
}
