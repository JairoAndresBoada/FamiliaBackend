package com.familia.flujo.infraestructura.persistencia

import com.familia.flujo.Familia.{ID, ReaderTecnico}
import com.familia.flujo.dominio.Persona
import com.familia.flujo.logFamilia.CorrelationId

trait RepoFamilia {
  def consultarFamiliares()(implicit correlationId: CorrelationId): ReaderTecnico[List[Persona]]
  def consultarFamiliar(idFamiliar : ID)(implicit correlationId: CorrelationId): ReaderTecnico[Option[Persona]]
  def guardarFamiliar(persona: Persona)(implicit correlationId: CorrelationId): ReaderTecnico[Persona]
}
