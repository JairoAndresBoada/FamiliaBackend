
package com.familia.flujo.aplicacion.consultas

import cats.data.Reader
import com.familia.flujo.Familia.ReaderTecnico
import com.familia.flujo.dominio.Persona
import com.familia.flujo.logFamilia.CorrelationId
import com.familia.flujo.{Consulta, ContextoFamilia}

case class ConsultarFamiliares() extends Consulta[List[Persona]]{
  override def ejecutarConsulta()(implicit correlationId: CorrelationId): ReaderTecnico[List[Persona]] = Reader{
    case contexto : ContextoFamilia =>
      contexto.repoFamilia.consultarFamiliares().run(contexto)
  }
}
