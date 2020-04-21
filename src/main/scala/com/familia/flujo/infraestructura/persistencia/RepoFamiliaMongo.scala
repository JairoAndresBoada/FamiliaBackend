package com.familia.flujo.infraestructura.persistencia
import cats.data.{EitherT, Reader}
import com.familia.flujo.Familia.{EitherTask, ID, ReaderTecnico}
import com.familia.flujo.dominio.Persona
import com.familia.flujo.infraestructura.persistencia.DecoderEncoderDomain._
import com.familia.flujo.infraestructura.persistencia.documentos.ColeccionPersonas
import com.familia.flujo.logFamilia.CorrelationId
import com.familia.flujo.{ContextoFamilia, ErrorFamilia}
import monix.eval.Task
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, document}



trait RepoFamiliaMongo extends RepoFamilia with UtilidadesRepo {
def filtroPersonaPorId: String => BSONDocument = (id: String) => document("idPersona" -> id)


override def consultarFamiliares()(implicit correlationId: CorrelationId): ReaderTecnico[List[Persona]] = ???

  override def consultarFamiliar(idFamiliar: ID)(implicit correlationId: CorrelationId): ReaderTecnico[Option[Persona]] = {
    Reader {
      contextoSel: ContextoFamilia =>
        ejecutarConsulta(ColeccionPersonas)(consultaAcoleccion(idFamiliar)).run(contextoSel)
    }
  }

  private def consultaAcoleccion(
                                  idProspecto: String
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

  override def guardarFamiliar(persona: Persona)(implicit correlationId: CorrelationId): ReaderTecnico[Persona] = ???
}

object RepoFamiliaMongo extends RepoFamiliaMongo
