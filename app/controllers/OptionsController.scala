package controllers

import model.{BookOption, BookOptionsProblem}
import play.api.libs.json.{JsValue, Json, Reads}
import play.api.libs.ws.WSClient
import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class OptionsController @Inject()(ws: WSClient,
                                  val controllerComponents: ControllerComponents)
                                 (implicit execution: ExecutionContext) extends BaseController {

  //private val urlPod = "http://sigature_finder:8080"
  private val urlLocal = "http://localhost:5001"
  private val optionsRequest = ws.url(urlLocal)

  private def parseJsonStringAs[T](s: String)(implicit rds: Reads[T]): Option[T] =
    Json.parse(s).validate[T].asOpt

  private def createBookOptionsProblem(session: Session): Option[BookOptionsProblem] =
    session.get("pages").flatMap(_.toIntOption)
      .flatMap(pages => session.get("max").flatMap(_.toIntOption).map((pages, _)))
      .flatMap(params => session.get("signatures").flatMap(parseJsonStringAs[Seq[Int]]).map((params._1, params._2, _)))
      .map(params => BookOptionsProblem(minimum = params._1, maximum = params._2, sizes = params._3, format = "json", pageCount = true))

  private def requestSolutionKey(BookOptionsProblem: BookOptionsProblem) =
    optionsRequest
      .addHttpHeaders("Content-Type" -> "application/json")
      .post(Json.toJson(BookOptionsProblem))
      .flatMap(response => response.status match {
        case 200 => Future.successful(response.body)
        case 400 => Future.failed(new Exception(response.statusText))
      })

  private def requestSolution(key: String): Future[JsValue] =
    optionsRequest
      .withQueryStringParameters(("key", key))
      .get()
      .flatMap(response => response.status match {
        case 404 => Future.failed(new Exception(response.statusText))
        case 200 => Future.successful(Json.parse(response.body))
      })

  private def getResult(BookOptionsProblem: BookOptionsProblem)(implicit request: RequestHeader) =
    requestSolutionKey(BookOptionsProblem)
      .flatMap(requestSolution)
      .transformWith {
        case Success(value) => Future {
          value.validate[Seq[BookOption]]
            .asOpt
            .map(x => Ok(views.html.options(x)))
            .getOrElse(Redirect(routes.ErrorController.get("Something went wrong")))
        }
        case Failure(exception) => Future {
          Redirect(routes.ErrorController.get(exception.getMessage))
        }
      }

  def get(): Action[AnyContent] = Action.async { implicit request => {
    createBookOptionsProblem(request.session)
      .map(getResult)
      .getOrElse(Future {
        Redirect(routes.StartController.get()).withNewSession
      })
  }
  }

  def post(): Action[AnyContent] = Action { request =>
    request.body.asFormUrlEncoded
      .map(Ok(_))
      .getOrElse(Redirect(routes.ErrorController.get("Expected an option to be selected")))
  }
}
