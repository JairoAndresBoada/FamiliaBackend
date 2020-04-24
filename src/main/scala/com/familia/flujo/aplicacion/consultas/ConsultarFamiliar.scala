package com.familia.flujo.aplicacion.consultas

import cats.data.Reader
import com.familia.flujo.Familia.{EitherTask, ReaderTecnico}
import com.familia.flujo.dominio.{Persona, ServiciosPersona}
import com.familia.flujo.logFamilia.{CorrelationId, LogFamilia}
import com.familia.flujo.{Consulta, ContextoFamilia}

case class ConsultarFamiliar(idFamiliar : String) extends Consulta[Option[Persona]]{
  override def ejecutarConsulta()(implicit correlationId: CorrelationId): ReaderTecnico[Option[Persona]] = Reader{
    case contexto : ContextoFamilia =>

      val respuestaTarifacionProspecto: EitherTask[Option[Persona]] = for {
      personaConsultada <- contexto.repoFamilia.consultarFamiliar(idFamiliar).run(contexto)
        _ = LogFamilia.logDebug(s"La persona que se consulto es $personaConsultada" , None , getClass)
      calcularEdadPersona <- ServiciosPersona.calcularEdadesPersona(personaConsultada)
      }yield {
        calcularEdadPersona
      }
        respuestaTarifacionProspecto
  }
}
