package com.familia.flujo.logFamilia

sealed trait Mensaje

case class MensajeLog (                        mensajePersonalizado: String,
                        nombreClase: String,
                        fecha: String,
                        error: Option[String],
                        MensajeErrorTecnico: Option[String]
                      ) extends Mensaje
