package com.familia.flujo.infraestructura.persistencia

import com.familia.flujo.dominio.Persona
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

object DecoderEncoderDomain {

  implicit val personaEnconder: Encoder[Persona] = deriveEncoder[Persona]
  implicit val personaDecoder: Decoder[Persona] = deriveDecoder[Persona]

  implicit val personaEnconderList: Encoder[List[Persona]] = deriveEncoder[List[Persona]]
  implicit val personaDecoderList: Decoder[List[Persona]] = deriveDecoder[List[Persona]]
}
