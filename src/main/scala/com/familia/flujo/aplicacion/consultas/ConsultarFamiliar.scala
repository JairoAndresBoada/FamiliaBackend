package com.familia.flujo.aplicacion.consultas

import cats.data.{EitherT, Reader}
import com.familia.flujo.Familia.ReaderTecnico
import com.familia.flujo.{Consulta, ContextoFamilia, CorrelationId}
import monix.eval.Task

case class ConsultarFamiliar(idFamiliar : String) extends Consulta[String]{
  override def ejecutarConsulta()(implicit correlationId: CorrelationId): ReaderTecnico[String] = Reader{
    case contexto : ContextoFamilia =>
      EitherT.right(Task.now(s"Hola $idFamiliar"))
  }
}
