package com.familia.flujo.aplicacion.consultas

import cats.data.Reader
import com.familia.flujo.Familia.ReaderTecnico
import com.familia.flujo.dominio.Persona
import com.familia.flujo.logFamilia.CorrelationId
import com.familia.flujo.{Consulta, ContextoFamilia}

case class ConsultarFamiliar(idFamiliar : Int) extends Consulta[Option[Persona]]{
  override def ejecutarConsulta()(implicit correlationId: CorrelationId): ReaderTecnico[Option[Persona]] = Reader{
    case contexto : ContextoFamilia =>
      contexto.repoFamilia.consultarFamiliar(idFamiliar).run(contexto)
  }
}
