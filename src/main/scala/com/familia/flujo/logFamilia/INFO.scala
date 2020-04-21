package com.familia.flujo.logFamilia

sealed trait TipoLog

case object INFO extends TipoLog

case object DEBUG extends TipoLog

case object ERROR extends TipoLog
