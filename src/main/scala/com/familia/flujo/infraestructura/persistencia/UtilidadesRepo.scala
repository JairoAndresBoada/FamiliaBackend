package com.familia.flujo.infraestructura.persistencia

import com.familia.flujo.logFamilia.{CorrelationId, LogFamilia}
import io.circe.{Decoder, DecodingFailure, Json}
import reactivemongo.bson.BSONDocument
import com.familia.flujo.logFamilia.LogFamilia._

trait UtilidadesRepo {
  private def controlarErrorTransformacionAJson(implicit correlationId: CorrelationId) = (tw: Throwable) => {
    logError(
      s"Se presento un error en la transformacion de Bson a Json. El detalle es: ${tw.getMessage}",
      None,
      getClass
    )
    None
  }

  private def controlarExitoTransformacionAJson = (json: Json) => Some(json)

  def bsonDocumentoAJson(bsonDocumento: BSONDocument)(
    implicit correlationId: CorrelationId
  ): Option[Json] ={
    LogFamilia.logInfo("ingreso por bsonDocumentoAJson" , getClass)
    ManejoBson
      .bsonToJson(bsonDocumento)
      .fold(controlarErrorTransformacionAJson, controlarExitoTransformacionAJson)
  }


  private def controlarErrorTransformacionJsonToA[A](implicit correlationId: CorrelationId) =
    (error: DecodingFailure) => {
      logError(
        s"Se presento un error en la transformacion de Json to A. El detalle es: ${error}",
        None,
        getClass
      )
      None
    }

  private def controlarExitoTransformacionJsonToA[A] = (valor: A) => Some(valor)

  def jsonA[A](json: Json)(implicit decoder: Decoder[A], correlationId: CorrelationId) =
    json
      .as[A]
      .fold(controlarErrorTransformacionJsonToA, controlarExitoTransformacionJsonToA)
}
