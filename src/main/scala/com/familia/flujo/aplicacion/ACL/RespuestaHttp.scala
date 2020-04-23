package com.familia.flujo.aplicacion.ACL

import org.joda.time.DateTime

sealed trait BaseRespuesta[A] {
  def fecha: String
  def respuesta: A
}


case class RespuestaExitosa[A](
                                respuesta: A,
                                fecha: String = DateTime.now().toString
                              ) extends BaseRespuesta[A]

case class RespuestaFallida[A](
                                respuesta: A,
                                mensaje: Option[String],
                                fecha: String = DateTime.now().toString
                              ) extends BaseRespuesta[A]

