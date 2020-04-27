package com.familia.flujo.aplicacion.route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import com.familia.UUID.generarUUID
import com.familia.flujo.Familia.EitherTask
import com.familia.flujo.aplicacion.consultas.ConsultarFamiliar
import com.familia.flujo.dominio.Persona
import com.familia.flujo.infraestructura.configuracion.HttpRoute
import com.familia.flujo.logFamilia.{CorrelationId, LogFamilia}
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import io.circe.generic.auto._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors

import scala.util.{Failure, Success}

trait ConsultaFamilia extends ErrorAccumulatingCirceSupport with HttpRoute {

  lazy val rutasConcultaFamilia: Route = consultarFamiliar

  lazy val consultarFamiliar : Route = cors(){
    pathPrefix("familiar" / Segment) { idFamiliar =>
      get {
        implicit val correlationId: CorrelationId = CorrelationId(s"consultar Familiar $generarUUID")
        LogFamilia.logInfo("Se va a consulta el Familiar", getClass)
        manejarRespuestaConsulta(ConsultarFamiliar(idFamiliar).ejecutarConsulta().run(contextoFamilia))
      }
    }
  }
  private def manejarRespuestaConsulta(
                                          respuesta: EitherTask[Option[Persona]]
                                      )(implicit correlationId: CorrelationId): Route = {
    onComplete(respuesta.value.runAsync) {
      case Success(rs) =>
        rs match {
          case Right(resultado) =>
            LogFamilia.logInfo("Consulta de Familiar exitosa", getClass)
            complete(
              (
                StatusCodes.OK,
                resultado
              )
            )
          case Left(error) =>
            LogFamilia.logInfo(s"No se consultó el Familiar por: ${error.nombre}", None, getClass)
            complete(
              (
                StatusCodes.InternalServerError,
                error.nombre
              )
            )
        }
      case Failure(err) =>
        LogFamilia.logError("Ocurrió un error ejecutando la consulta del Familiar", Some(err), getClass)
        complete(
          (
            StatusCodes.InternalServerError,
            err.getMessage
          )
        )
    }
  }
}
