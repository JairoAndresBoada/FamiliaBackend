package com.familia.flujo.dominio

import com.familia.flujo.Familia.ID
import reactivemongo.bson.Macros


case class Persona (
                   idPersona : ID,
                   nombres: String,
                   apellidos : String,
                   fechaNacimiento: String,
                   imagen : String ,
                   descripcion : String,
                   ciudad : String
                   )


object PersonTransformer{
  implicit val personaMongo = Macros.handler[Persona]
}