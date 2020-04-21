package com.familia.flujo.logFamilia

import com.typesafe.scalalogging.{CanLog, LoggerTakingImplicit}
import io.circe.generic.auto._
import io.circe.syntax._
import org.joda.time.DateTime
import org.slf4j.MDC
import com.familia.flujo.logFamilia.FabricaMensaje._


trait LogFamilia {
  def fechaActual: String = DateTime.now().toLocalDate.toString + " hora:" + DateTime.now().toLocalTime.toString

  def formatoMensajeEspecifico(mensajeLog: String): String = s"\n${mensajeLog}\n"

  def formatoMensajeInfo: String => String =
    (mensajeInfo: String) => Console.GREEN + formatoMensajeEspecifico(mensajeInfo)

  def formatoMensajeDebug: String => String =
    (mensajeInfo: String) => Console.YELLOW + formatoMensajeEspecifico(mensajeInfo)

  def formatoMensajeError: String => String =
    (mensajeInfo: String) => Console.RED + formatoMensajeEspecifico(mensajeInfo)

  def formatoGeneralMensaje: String => String = (mensajeEspecifico: String) => mensajeEspecifico + Console.RESET

  implicit case object CanLogCorrelationId extends CanLog[CorrelationId] {
    override def logMessage(originalMsg: String, a: CorrelationId): String = {
      MDC.put("correlationId", a.value)
      s"${a.value} $originalMsg"
    }
  }

  protected lazy val logger: LoggerTakingImplicit[CorrelationId] =
    com.typesafe.scalalogging.Logger.takingImplicit("arquitectura.familia")
}

case class CorrelationId(value: String) {
  MDC.put("correlationId", value)
}

object LogFamilia extends LogFamilia {

  def contruirMensaje(tipo: TipoLog, mensaje: MensajeLog): String = {
    val configuracionMensaje = tipo match {
      case INFO  => formatoMensajeInfo(_: String)
      case DEBUG => formatoMensajeDebug(_: String)
      case ERROR => formatoMensajeError(_: String)
    }
    formatoGeneralMensaje(configuracionMensaje(mensaje.asJson.toString()))
  }

  def logInfo(mensaje: String, clase: Class[_])(implicit correlationId: CorrelationId) = {
    logger.info(contruirMensaje(INFO, crearMensajeLog(mensaje, None, clase)))
  }

  def logDebug(mensaje: String, causa: Option[Throwable], clase: Class[_])(implicit correlationId: CorrelationId) = {
    logger.debug(contruirMensaje(DEBUG, crearMensajeLog(mensaje, causa, clase)))
  }

  def logError(mensaje: String, causa: Option[Throwable], clase: Class[_])(implicit correlationId: CorrelationId) = {
    logger.debug(contruirMensaje(ERROR, crearMensajeLog(mensaje, causa, clase)))
  }

}

