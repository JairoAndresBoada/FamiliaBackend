package com.familia

package object UUID {
  def generarUUID: String = java.util.UUID.randomUUID.toString
}
