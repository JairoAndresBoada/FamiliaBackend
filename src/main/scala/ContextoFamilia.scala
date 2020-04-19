package com.familia.flujo

import com.familia.flujo.infraestructura.configuracion.{ConexionReactivMongo, ConfiguracionFamiliares}


trait ContextoFamilia {

  def config: ConfiguracionFamiliares

  def conexionABD: ConexionReactivMongo

}
