package com.familia.flujo.infraestructura.configuracion


case class HttpConfig(host: String, port: Int)


case class ConfiguracionFamiliares(
                              http: HttpConfig,
                              persistencia: ConexionReactivMongo,

                                  )
