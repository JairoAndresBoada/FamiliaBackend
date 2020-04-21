package com.familia.flujo.dominio

import akka.http.scaladsl.model.DateTime
import com.familia.flujo.Familia.ID

case class Persona (
                   idPersona : ID,
                   nombres: String,
                   apellidos : String,
                   fechaNacimiento: DateTime,
                   imagen : String ,
                   descripcion : String,
                   ciudad : String
                   )