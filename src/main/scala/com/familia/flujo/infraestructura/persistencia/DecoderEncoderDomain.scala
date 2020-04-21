package com.familia.flujo.infraestructura.persistencia

import com.familia.flujo.dominio.Persona
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import org.joda.time.DateTime

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.joda.time.DateTime
import io.circe.syntax._
import io.circe.generic.semiauto._

object DecoderEncoderDomain {
  import io.circe.generic.auto._


  implicit val dateTimeEncoder: Encoder[DateTime] = Encoder.instance(a => a.getMillis.asJson)
  implicit val dateTimeDecoder: Decoder[DateTime] = Decoder.instance(a => a.as[Long].map(new DateTime(_)))

  implicit val personaEnconder: Encoder[Persona] = deriveEncoder[Persona]
  implicit val personaDecoder: Decoder[Persona] = deriveDecoder[Persona]
}
