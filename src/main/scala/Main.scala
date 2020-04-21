package com.familia.flujo

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.familia.flujo.infraestructura.configuracion.{ConexionReactivMongo, ConfiguracionFamiliares, DefaultConfig, HttpServer}
import com.familia.flujo.infraestructura.persistencia.{RepoFamilia, RepoFamiliaMongo}
import com.familia.flujo.logFamilia.CorrelationId
import monix.execution.Scheduler

import scala.concurrent.Future
import scala.util.{Failure, Success}


object Main extends App with HttpServer{

  implicit override val system: ActorSystem             = ActorSystem("Actor_Familia")
  implicit override val materializer: ActorMaterializer = ActorMaterializer()
  override implicit val schedulerRoutes: Scheduler      = Scheduler.apply(system.dispatcher)

  implicit def correlationId: CorrelationId = CorrelationId("flujo-failia-main")

  val appConfig: ConfiguracionFamiliares = DefaultConfig.familiaConfig

  override val contextoFamilia: ContextoFamilia = new ContextoFamilia {
    override val conexionABD: ConexionReactivMongo = new ConexionReactivMongo(appConfig)
    override val config: ConfiguracionFamiliares = appConfig
    override val repoFamilia: RepoFamilia = RepoFamiliaMongo
  }

  val httpServer: Future[Http.ServerBinding] =
  Http().bindAndHandle(rutasFamilia, "localhost", contextoFamilia.config.http.port)(materializer)

  httpServer.onComplete {
    case Success(Http.ServerBinding(localAddress)) =>
    println(s"Http iniciado  - Escuchando para HTTP en $localAddress", getClass)
    case Failure(exception) =>
    println(s"Error al iniciar servicio HTTP ", Some(exception), getClass)
  }

}

