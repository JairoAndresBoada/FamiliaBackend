package com.familia.flujo.aplicacion.route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.familia.UUID.generarUUID
import com.familia.flujo.ErrorInterno
import com.familia.flujo.Familia.EitherTask
import com.familia.flujo.aplicacion.ACL.{RespuestaExitosa, RespuestaFallida}
import com.familia.flujo.aplicacion.comandos.ComandoGuardarPersona
import com.familia.flujo.dominio.Persona
import com.familia.flujo.infraestructura.configuracion.HttpRoute
import com.familia.flujo.infraestructura.persistencia.DecoderEncoderDomain._
import com.familia.flujo.logFamilia.{CorrelationId, LogFamilia}
import com.softwaremill.quicklens._
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import io.circe.generic.auto._

import scala.util.{Failure, Success}

trait ComandoFamilia extends ErrorAccumulatingCirceSupport with HttpRoute  {

  lazy val rutaGuardarPersona: Route = guardarPersona


  lazy val guardarPersona: Route =  cors() {
    pathPrefix("familiar") {
      post {
        entity(as[Persona]) { requestPersona =>
          LogFamilia.logDebug(s"la Persona que se va a enviar es: $requestPersona", None, getClass)(CorrelationId("s"))
          implicit val correlationGuardarPersona: CorrelationId = CorrelationId(s"Guardar Persona: ${requestPersona.nombres}")
          manejarRespuestaComandoPersona(
            ComandoGuardarPersona(
              requestPersona.modify(_._id).setToIf(requestPersona._id.isEmpty)(Some(generarUUID))
            ).ejecutarComando().run(contextoFamilia)
          )
        }
      }
    }
  }

  private def manejarRespuestaComandoPersona(
                                                respuesta: EitherTask[Persona]
                                              )(implicit correlationId: CorrelationId): Route = {
    onComplete(respuesta.value.runAsync) {
      case Success(rs) =>
        rs match {
          case Right(resultado) =>
            LogFamilia.logInfo("Guardado de la Persona exitoso", getClass)
            complete((StatusCodes.OK, RespuestaExitosa(resultado)))
          case Left(error) =>
            LogFamilia.logDebug(s"No se guardó la Persona por: ${error.nombre}", None, getClass)
            complete((StatusCodes.InternalServerError, RespuestaFallida(error.nombre, None)))
        }
      case Failure(err) =>
        LogFamilia.logError("Ocurrió un error ejecutando el guardado del Persona", Some(err), getClass)
        complete(
          (StatusCodes.InternalServerError, RespuestaFallida(ErrorInterno(nombre = err.getMessage), None))
        )
    }
  }

}
