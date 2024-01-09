package controllers

import play.api.mvc._

import javax.inject._

@Singleton
class ReportController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def get(): Action[AnyContent] = TODO

  def post(): Action[AnyContent] = TODO
}
