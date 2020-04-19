package com.familia.flujo.infraestructura.configuracion

import pureconfig.loadConfigOrThrow

object DefaultConfig {
  import pureconfig.generic.auto._

  val familiaConfig: ConfiguracionFamiliares= loadConfigOrThrow[ConfiguracionFamiliares](familiaNamespacePrefix)

}
