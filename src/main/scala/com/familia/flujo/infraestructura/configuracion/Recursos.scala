package com.familia.flujo.infraestructura.configuracion

import com.familia.flujo.aplicacion.route.{ConsultaFamilia, consultaFamiliares}

trait Recursos extends ConsultaFamilia with consultaFamiliares{

  val rutasFamilia= rutasConcultaFamilia ~ rutaConsultaFamiliares

}

