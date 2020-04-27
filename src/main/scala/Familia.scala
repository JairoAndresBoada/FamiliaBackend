package com.familia.flujo

import cats.data.{EitherT, Reader}
import com.familia.flujo.Familia.ReaderTecnico
import com.familia.flujo.logFamilia.CorrelationId
import monix.eval.Task

package object Familia {

  type EitherTask[A] = EitherT[Task, ErrorFamilia, A]

  type ReaderTecnico[A] = Reader[ContextoFamilia, EitherTask[A]]

  type ID = String

  type ErrorOr[A] = EitherT[Task, ErrorFamilia, A]

}

trait ErrorFamilia {
  def nombre: String
}

case class ErrorInterno(
                         override val nombre: String = "Se ha producido un error en el servicio."
                       ) extends ErrorFamilia

case class ErrorNoExistePersona(
                                 override val nombre: String = "No existe la perona en el repositorio"
                       ) extends ErrorFamilia

case class ErrorAlGenerarBSON(nombre : String) extends ErrorFamilia

trait Comando[A] {

  def ejecutarComando()(implicit correlationId: CorrelationId): ReaderTecnico[A]

}

trait Consulta[A] {

  def ejecutarConsulta()(implicit correlationId: CorrelationId): ReaderTecnico[A]

}
