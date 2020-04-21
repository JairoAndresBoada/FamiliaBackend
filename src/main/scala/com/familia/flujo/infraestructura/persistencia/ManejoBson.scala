package com.familia.flujo.infraestructura.persistencia

import cats.data.EitherT
import com.familia.flujo.{ErrorAlGenerarBSON, ErrorFamilia}
import com.familia.flujo.Familia.ErrorOr
import com.familia.flujo.dominio.Persona
import io.circe.bson.BsonCodecInstances
import io.circe.syntax._
import monix.eval.Task
import reactivemongo.bson.BSONValue
import com.familia.flujo.infraestructura.persistencia.DecoderEncoderDomain._

object ManejoBson extends BsonCodecInstances {
  def toBsonDomain(persona: Persona): ErrorOr[BSONValue] = {
    val bson: Either[ErrorFamilia, BSONValue] = jsonToBson(persona.asJson).
      fold(_ => Left(ErrorAlGenerarBSON("Error al transformar el objeto a bson")), y => Right(y))
    EitherT(Task.now(bson))
  }
}
