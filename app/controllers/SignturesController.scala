package controllers

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class SignturesController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def get(): Action[AnyContent] = TODO

  def post(): Action[AnyContent] = TODO
}
