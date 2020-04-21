package com.familia.flujo.infraestructura.configuracion

import cats.data.Reader
import com.familia.UUID.generarUUID
import com.familia.flujo.CorrelationId
import com.typesafe.config.ConfigFactory
import monix.eval.Task
import reactivemongo.api.{DefaultDB, MongoConnection, MongoDriver}

import scala.concurrent.Future



class ConexionReactivMongo(config: ConfiguracionFamiliares) {

  val driver = new MongoDriver(Some(ConfigFactory.load()))

  val conexionEstablecida = establecerConexion().run(config).memoizeOnSuccess

  private def establecerConexion(): Reader[ConfiguracionFamiliares, Task[DefaultDB]] = Reader {
    case config: ConfiguracionFamiliares =>
      implicit val correlationId = CorrelationId(s"Conexión a Mongo: ${generarUUID}")
      generarURLConexion(config) match {
        case Right(url)       => manejarConexionBD(url).run(config)
        case Left(errores) => manejarErrorConexion(errores)
      }
  }

  private def manejarConexionBD(
                                 url: String
                               )(implicit cid: CorrelationId): Reader[ConfiguracionFamiliares, Task[DefaultDB]] = Reader {
    case config: ConfiguracionFamiliares =>
      Task
        .deferFutureAction { implicit scheduler =>
          val parsedUri        = MongoConnection.parseURI(url)
          val connection       = parsedUri.map(driver.connection)
          val futureConnection = Future.fromTry(connection)
          futureConnection.flatMap(_.database(config.persistencia.nombre))
        }
        .onErrorHandleWith {
          case exception: Exception => {
            Task.raiseError(exception)
          }
        }
  }

  private def manejarErrorConexion(lista: String)(implicit cid: CorrelationId) = {
    val ex = new Exception("No se pudo conectar a Mongo")
    Task.raiseError(ex)
  }


  private def generarURLConexion(config: ConfiguracionFamiliares): Either[String,String] = {
    (
      datosParaLogeo(config),
      datosHosts(config),
      datosBD(config)
    ) match {
      case (Right(logeo), Right(host), Right(db)) =>  Right(s"mongodb://${logeo}${host}/${db}${config.persistencia.adicionales}")
      case (Left(_), Left(_) ,Left(_) ) => Left("ocurrio un error conectandose a las base de datos")
    }
  }

  private def datosBD(config: ConfiguracionFamiliares): Either[String, String] = {
    config.persistencia.admin.nonEmpty match {
      case true  => Right(config.persistencia.admin)
      case false => Left("No existe el nombre admin de la BD a conectarse")
    }
  }

  private def datosHosts(config: ConfiguracionFamiliares): Either[String, String] = {
    config.persistencia.nodos match {
      case Nil                 => Left("No hay ningún nodo configurado, debe existir almenos uno")
      case nodos: List[String] =>Right(s"${nodos.mkString(",")}")
    }
  }

  private def datosParaLogeo(config: ConfiguracionFamiliares): Either[String,String] = {
    (
      config.persistencia.usuario.nonEmpty,
      config.persistencia.password.nonEmpty
    ) match {
      case (true, true) => Right(s"${config.persistencia.usuario}:${config.persistencia.password}@")
      case (_, _)       => Left("El usuario y el password son necesarios para la conexión a la BD")
    }
  }

}
