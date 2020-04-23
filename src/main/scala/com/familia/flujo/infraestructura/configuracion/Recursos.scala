package com.familia.flujo.infraestructura.configuracion

import com.familia.flujo.aplicacion.route.{ComandoFamilia, ConsultaFamilia, ConsultaFamiliares}

trait Recursos extends ConsultaFamilia with ConsultaFamiliares with ComandoFamilia{

  val rutasFamilia= rutasConcultaFamilia ~ rutaConsultaFamiliares ~ rutaGuardarPersona

}

