package com.familia.flujo.aplicacion.comandos

import cats.data.Reader
import com.familia.flujo.{Comando, ContextoFamilia}
import com.familia.flujo.Familia.ReaderTecnico
import com.familia.flujo.dominio.Persona
import com.familia.flujo.logFamilia.CorrelationId

case class ComandoGuardarPersona(persona: Persona) extends Comando[Persona] {
  override def ejecutarComando()(implicit correlationId: CorrelationId): ReaderTecnico[Persona] = Reader {
    case contexto : ContextoFamilia =>
      contexto.repoFamilia.guardarFamiliar(persona).run(contexto)
  }
}
