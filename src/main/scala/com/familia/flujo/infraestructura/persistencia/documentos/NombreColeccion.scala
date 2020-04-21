package com.familia.flujo.infraestructura.persistencia.documentos

trait NombreColeccion {
    def nombreColeccion : String
}

case object ColeccionPersonas extends  NombreColeccion{
  override def nombreColeccion: String = "personas"
}
