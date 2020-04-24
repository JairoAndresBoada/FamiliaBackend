package com.familia.flujo.dominio

import com.familia.flujo.Familia.ID
import reactivemongo.bson.Macros


case class Persona (
                   _id : Option[ID],
                   nombres: String,
                   apellidos : String,
                   fechaNacimiento: String,
                   imagen : String ,
                   descripcion : String,
                   ciudad : String,
                   edad : Option[String],
                   profesion : Option[String],
                   sexo: Option[String]
                   )


object PersonTransformer{
  implicit val personaMongo = Macros.handler[Persona]
}