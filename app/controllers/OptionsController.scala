package controllers

import play.api.libs.json.{JsValue, Json, Reads, Writes}
import play.api.libs.ws.WSClient
import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class OptionsController @Inject()(ws: WSClient,
                                  val controllerComponents: ControllerComponents)
                                 (implicit execution: ExecutionContext) extends BaseController {
  case class Problem(minimum: Int,
                     maximum: Int,
                     sizes: Seq[Int],
                     format: String,
                     pageCount: Boolean)

  implicit val problemWrites: Writes[Problem] = (problem: Problem) =>
    Json.obj(
      "minimum" -> problem.minimum,
      "maximum" -> problem.maximum,
      "sizes" -> problem.sizes,
      "format" -> problem.format,
      "pageCount" -> problem.pageCount
    )

  private val optionsRequest = ws.url("http://sigature_finder:8080")

  private def parseJsonStringAs[T](s: String)(implicit rds: Reads[T]): Option[T] =
    Json.parse(s).validate[T].asOpt

  private def createProblem(session: Session): Option[Problem] =
    session.get("pages").flatMap(_.toIntOption)
      .flatMap(pages => session.get("max").flatMap(_.toIntOption).map((pages, _)))
      .flatMap(params => session.get("signatures").flatMap(parseJsonStringAs[Seq[Int]]).map((params._1, params._2, _)))
      .map(params => Problem(minimum = params._1, maximum = params._2, sizes = params._3, format = "json", pageCount = true))

  private def requestSolutionKey(problem: Problem) = {
    val body = Json.toJson(problem)
    optionsRequest
      .addHttpHeaders("Content-Type" -> "application/json")
      .post(body)
      .map(response => response.toString)
  }

  private def requestSolution(key: String): Future[JsValue] =
    optionsRequest
      .withQueryStringParameters(("key", key))
      .get()
      .map(response => Json.parse(response.body))

  private def getResult(problem: Problem) =
    requestSolutionKey(problem)
      .flatMap(requestSolution)
      .transformWith {
        case Success(value) => Future {
          Ok(value)
        }
        case Failure(exception) => Future {
          Redirect(routes.ErrorController.get(exception.getMessage))
        }
      }

  def get(): Action[AnyContent] = Action.async { request => {
    createProblem(request.session)
      .map(getResult)
      .getOrElse(Future {
        Redirect(routes.StartController.get()).withNewSession
      })
  }
  }

  def post(): Action[AnyContent] = TODO
}
