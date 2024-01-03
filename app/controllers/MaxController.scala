package controllers

import play.api.mvc._

import javax.inject._

@Singleton
class MaxController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def get: Action[AnyContent] = Action { request =>
    request.session.get("pages")
    .map( pages => Ok(views.html.max(pages)))
      .getOrElse(Redirect(routes.StartController.get())).withNewSession
  }

  def post: Action[AnyContent] = Action{ request =>
    request.body.asFormUrlEncoded
      .flatMap(values => {
        values.get("max")
          .flatMap(_.headOption)
          .flatMap(pages => pages.toIntOption)
          .map(max => Redirect(routes.SignaturesController.get()).withSession(request.session + ("max",max.toString)))
      })
      .getOrElse(Redirect(routes.ErrorController.get("could not find form value max")))
  }
}
