package controllers

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class SignaturesController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def get(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.signatures())
  }

  def post(): Action[AnyContent] = Action {request =>
    request.
      body
      .asFormUrlEncoded
      .map(_.keySet)
      .map( _.map(_.toIntOption)
        .filter(_.isDefined)
        .map(_.get))
      .map(selection=>
        Redirect(routes.OptionsController.get()).withSession(request.session+("signatures",s"[${selection.mkString(",")}]"))
      )
      .getOrElse(Redirect(routes.SignaturesController.get()).withSession(request.session))
  }
  /*{
    request =>
      request.body.asFormUrlEncoded
        .flatMap(values => {
          values.get("signatures")
            .flatMap(_.headOption)
            .flatMap(pages => pages.toIntOption)
            .map(pages => Redirect(routes.MaxController.get()).withSession("pages" -> pages.toString))
        })
        .getOrElse(Redirect(routes.ErrorController.get("could not find form value pages")))
  }*/
}
