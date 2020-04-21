package com.familia.flujo.aplicacion.constantes

import com.typesafe.config.{Config, ConfigFactory}
import scala.collection.JavaConversions._

object Contantes {

  val config: Config = ConfigFactory.load
  val PREFIJO = "com.familia.flujo.persistencia."

  private def devolverString(llave: String): String = {
    config.getString(PREFIJO + llave)
  }

  private def devolverListaStrings(llave: String): List[String] = {
    config.getStringList(PREFIJO + llave).toList
  }

  lazy val DB_NAME  = devolverString("nombre")
  lazy val USERNAME = devolverString("usuario")
  lazy val PASSWORD = devolverString("password")
  lazy val NODES    = devolverListaStrings("nodos")

  //colecciones
  lazy val COL_PERSONAS = devolverString("col_personas")


}
