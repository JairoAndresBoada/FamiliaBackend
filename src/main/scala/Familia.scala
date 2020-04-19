package com.familia.flujo

import cats.data.{EitherT, Reader}
import com.familia.flujo.Familia.{EitherTask, ReaderTecnico}
import monix.eval.Task
import org.slf4j.MDC

package object Familia {

  type EitherTask[A] = EitherT[Task, ErrorFamilia, A]

  type ReaderTecnico[A] = Reader[ContextoFamilia, EitherTask[A]]


}

trait ErrorFamilia {
  def nombre: String
}

trait Comando[A] {

  def ejecutarComando()(implicit correlationId: CorrelationId): ReaderTecnico[A]

}

trait Consulta[A] {

  def ejecutarConsulta()(implicit correlationId: CorrelationId): ReaderTecnico[A]

}

case class CorrelationId(value: String) {
  MDC.put("correlationId", value)
}