package com.familia.flujo

import com.familia.flujo.infraestructura.configuracion.{ConexionReactivMongo, ConfiguracionFamiliares}
import com.familia.flujo.infraestructura.persistencia.RepoFamilia


trait ContextoFamilia {

  def conexionABD: ConexionReactivMongo

  def config: ConfiguracionFamiliares

  def repoFamilia : RepoFamilia

}
