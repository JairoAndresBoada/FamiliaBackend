package com.familia.flujo.logFamilia

import com.familia.flujo.logFamilia.LogFamilia.fechaActual
object FabricaMensaje {


  def crearMensajeLog(mensaje: String, causa: Option[Throwable], clase: Class[_]): MensajeLog = {
    MensajeLog(
      mensaje,
      clase.getCanonicalName,
      fechaActual,
      causa.map(error => {
        error.getCause match {
          case null => "Sin Causa"
          case exc  => exc.getMessage
        }
      }),
      causa.map(_.getMessage)
    )
  }

}
