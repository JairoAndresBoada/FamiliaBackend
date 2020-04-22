package com.familia.flujo.aplicacion.route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import com.familia.UUID.generarUUID
import com.familia.flujo.Familia.EitherTask
import com.familia.flujo.aplicacion.consultas.ConsultarFamiliares
import com.familia.flujo.dominio.Persona
import com.familia.flujo.infraestructura.configuracion.HttpRoute
import com.familia.flujo.logFamilia.{CorrelationId, LogFamilia}
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import io.circe.generic.auto._

import scala.util.{Failure, Success}


trait consultaFamiliares extends ErrorAccumulatingCirceSupport with HttpRoute{
  lazy val rutaConsultaFamiliares: Route = consultarFamiliares

  lazy val consultarFamiliares : Route = {
    pathPrefix("familiares") {
      get {
        implicit val correlationId: CorrelationId = CorrelationId(s"consultar Familiares $generarUUID")
        LogFamilia.logInfo("Se va a consulta el Familiar", getClass)
        manejarRespuestaConsulta(ConsultarFamiliares().ejecutarConsulta().run(contextoFamilia))
      }
    }
  }

  private def manejarRespuestaConsulta(
                                        respuesta: EitherTask[List[Persona]]
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
            println(s"No se consultó el Familiar por: ${error.nombre}", None, getClass)
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
