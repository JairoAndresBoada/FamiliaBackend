package com.familia.flujo.aplicacion.route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import com.familia.UUID.generarUUID
import com.familia.flujo.CorrelationId
import com.familia.flujo.Familia.EitherTask
import com.familia.flujo.aplicacion.consultas.ConsultarFamiliar
import com.familia.flujo.infraestructura.configuracion.HttpRoute
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport

import scala.util.{Failure, Success}

trait ConsultaFamilia extends ErrorAccumulatingCirceSupport with HttpRoute {

  lazy val rutasConcultaFamilia = consultarFamiliar

  lazy val consultarFamiliar : Route = {
    pathPrefix("familia" / Segment) { idFamiliar =>
      get {
        implicit val correlationId = CorrelationId(s"consultar Familiar ${generarUUID}")
        println("Se va a consulta el Familiar", getClass)
        manejarRespuestaConsulta(ConsultarFamiliar(idFamiliar).ejecutarConsulta().run(contextoFamilia))
      }
    }
  }
  private def manejarRespuestaConsulta(
                                        respuesta: EitherTask[String]
                                      )(implicit correlationId: CorrelationId): Route = {
    onComplete(respuesta.value.runAsync) {
      _ match {
        case Success(rs) => {
          rs match {
            case Right(resultado) => {
              println("Consulta de Familiar exitosa", getClass)
              complete(
                (
                  StatusCodes.OK,
                  resultado
                )
              )
            }
            case Left(error) => {
              println(s"No se consultó el Familiar por: ${error.nombre}", None, getClass)
              complete(
                (
                  StatusCodes.InternalServerError,
                  error.nombre
                )
              )
            }
          }
        }
        case Failure(err) => {
          println("Ocurrió un error ejecutando la consulta del Familiar", Some(err), getClass)
          complete(
            (
              StatusCodes.InternalServerError,
               err.getMessage
            )
          )
        }
      }
    }
  }
}
