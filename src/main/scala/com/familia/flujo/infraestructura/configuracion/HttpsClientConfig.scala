package com.familia.flujo.infraestructura.configuracion


case class HttpConfig(host: String, port: Int)

case class ConexionMongo(
                          admin: String,
                          nombre: String,
                          usuario: String,
                          password: String,
                          adicionales: String,
                          nodos: List[String]
                        )



case class ConfiguracionFamiliares(
                              http: HttpConfig,
                              persistencia: ConexionMongo,

                                  )
