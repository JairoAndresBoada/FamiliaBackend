package com.familia.flujo.infraestructura

import cats.data.{EitherT, Reader}
import com.familia.flujo.ContextoFamilia
import com.familia.flujo.Familia.EitherTask
import com.familia.flujo.infraestructura.persistencia.documentos.NombreColeccion
import monix.eval.Task
import reactivemongo.api.collections.bson.BSONCollection

package object persistencia {
  type funcionConsulta[A]    = BSONCollection => EitherTask[A]
  type ReaderPersistencia[A] = Reader[ContextoFamilia, EitherTask[A]]

  def darColeccion(nombreColeccion: NombreColeccion): Reader[ContextoFamilia, Task[BSONCollection]] = Reader {
    case contexto: ContextoFamilia=>
      contexto.conexionABD.conexionEstablecida
        .map(_.collection[BSONCollection](nombreColeccion.nombreColeccion))
  }

  def ejecutarConsulta[A](nombreColeccion: NombreColeccion)(f: funcionConsulta[A]): ReaderPersistencia[A] =
    Reader {
      case contexto: ContextoFamilia=>
        EitherT(
          darColeccion(nombreColeccion)
            .run(contexto)
            .flatMap(f(_).value)
        )
    }
}
