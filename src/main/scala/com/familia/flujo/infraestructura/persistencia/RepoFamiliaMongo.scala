package com.familia.flujo.infraestructura.persistencia
import cats.data.{EitherT, Reader}
import com.familia.flujo.Familia.{EitherTask, ID, ReaderTecnico}
import com.familia.flujo.dominio.Persona
import com.familia.flujo.dominio.PersonTransformer.personaMongo
import com.familia.flujo.infraestructura.persistencia.DecoderEncoderDomain._
import com.familia.flujo.infraestructura.persistencia.documentos.ColeccionPersonas
import com.familia.flujo.logFamilia.{CorrelationId, LogFamilia}
import com.familia.flujo.{ContextoFamilia, ErrorFamilia}
import monix.eval.Task
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, document}



trait RepoFamiliaMongo extends RepoFamilia with UtilidadesRepo {
def filtroPersonaPorId: Int => BSONDocument = (id: ID) => document("idPersona" -> id)


override def consultarFamiliares()(implicit correlationId: CorrelationId): ReaderTecnico[List[Persona]] = {
  Reader{
    contexto :ContextoFamilia =>
      ejecutarConsulta(ColeccionPersonas)(consultaAcoleccion()).run(contexto)
  }
}

  override def consultarFamiliar(idFamiliar: ID)(implicit correlationId: CorrelationId): ReaderTecnico[Option[Persona]] = {
    Reader {
      contexto: ContextoFamilia =>
        ejecutarConsulta(ColeccionPersonas)(consultaAcoleccionID(idFamiliar)).run(contexto)
    }
  }

  private def consultaAcoleccionID(
                                         idProspecto: ID
                                       )(implicit correlationId: CorrelationId): BSONCollection => EitherTask[Option[Persona]] =
    (bson: BSONCollection) => {
      EitherT(Task.deferFutureAction { implicit ec =>
        bson
          .find(filtroPersonaPorId(idProspecto), None)
          .one[BSONDocument]
          .map(respuestaConsulta => {
            val personaOpcional: Option[Persona] = for {
              respuestaBD <- respuestaConsulta
              json        <- bsonDocumentoAJson(respuestaBD)
              laPersona <- jsonA[Persona](json)
            } yield {
              laPersona
            }
            val respuestaEither: Either[ErrorFamilia, Option[Persona]] = Right(personaOpcional)
            respuestaEither
          })
      })
    }

  private def consultaAcoleccion()(implicit correlationId: CorrelationId): BSONCollection => EitherTask[List[Persona]] =
    (bson: BSONCollection) => {
      EitherT(Task.deferFutureAction { implicit ec =>
        bson
          .find(document("idPersona" -> document("$gt" -> -1)), None)
          .cursor[Persona](ReadPreference.secondaryPreferred)
          .collect[List](100, Cursor.FailOnError[List[Persona]]())
          .map(respuestaConsulta => {
            val personaOpcional: List[Persona] = for {
              respuestaBD <- respuestaConsulta
              _ = LogFamilia.logDebug(s"la respuest respuestaBD es $respuestaBD" ,None,  getClass)
            } yield {
              respuestaBD
            }
            val respuestaEither: Either[ErrorFamilia, List[Persona]] = Right(personaOpcional)
            respuestaEither
          })
      })
    }

  override def guardarFamiliar(persona: Persona)(implicit correlationId: CorrelationId): ReaderTecnico[Persona] = ???


}

object RepoFamiliaMongo extends RepoFamiliaMongo


