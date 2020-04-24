package com.familia.flujo.dominio

import java.time.LocalDate

import akka.http.scaladsl.model.DateTime
import cats.data.EitherT
import com.familia.flujo.ErrorNoExistePersona
import com.familia.flujo.Familia.EitherTask
import monix.eval.Task
import com.softwaremill.quicklens._
import org.joda.time.{DateTime, DateTimeZone}
import java.time.format.DateTimeFormatter
import java.time.Period

import com.familia.flujo.logFamilia.{CorrelationId, LogFamilia}

object ServiciosPersona {

  def calcularEdadesPersona(
                                  personas: Option[Persona]
                                )(implicit correlationId: CorrelationId): EitherTask[Option[Persona]] = {

    val calcularEdad: EitherTask[Option[Persona]] = personas.fold[EitherTask[Option[Persona]]](
      EitherT.left(Task.now(ErrorNoExistePersona()))
    ) ( perso =>
      EitherT.right(Task.now(Some(perso.modify(_.edad).setTo(Some(agregarEdad(perso.fechaNacimiento))))))
    )
    calcularEdad
  }

  private def agregarEdad(fecha : String)(implicit correlationId: CorrelationId): String = {
    val formato: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val fechaNac = LocalDate.parse(fecha, formato)
    val ahora = LocalDate.now
    val periodo: Period = Period.between(fechaNac, ahora)
    s"${periodo.getYears} años , ${periodo.getMonths} meses y ${periodo.getDays} dias"
   }

}
