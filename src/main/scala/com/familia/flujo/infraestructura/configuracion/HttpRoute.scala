package com.familia.flujo.infraestructura.configuracion

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import com.familia.flujo.ContextoFamilia
import monix.execution.Scheduler

trait HttpRoute extends Directives {
  implicit def system: ActorSystem
  implicit def materializer: ActorMaterializer
  implicit def schedulerRoutes: Scheduler
  def contextoFamilia: ContextoFamilia

}
