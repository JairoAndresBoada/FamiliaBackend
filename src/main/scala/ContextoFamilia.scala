package com.familia.flujo

import com.familia.flujo.infraestructura.configuracion.{ConexionReactivMongo, ConfiguracionFamiliares}


trait ContextoFamilia {

  def conexionABD: ConexionReactivMongo

  def config: ConfiguracionFamiliares

}
