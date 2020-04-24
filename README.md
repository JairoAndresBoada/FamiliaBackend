familia-ref-arch
===============


## Run

Para generar el zip del proyecto hay que correr el comando:

`sbt universal:packageBin`


## GET
Probar servicios expuestos por http:

* Consultar familiares: `http://localhost:8080/familiares`
* Consultar familiar por id : `http://localhost:8080/familiar/8f28ce36-c270-4586-8b19-9bf054448f66`



## POST

* Guardar un familiar:

*url:*

http://localhost:8080/familiar

*Headers*
> Content-Type:application/json

*Body*
> {
      "nombres" : "example",
      "apellidos" : "example",
      "fechaNacimiento" : "11/12/1975",
      "imagen" : "assets/imagenes/example.png",
      "descripcion" : "example exmaple",
      "ciudad" : "example",
      "profesion":"example",
  	  "sexo" : "example"
  }

*Response*

Esperamos el mismo JSON que enviamos como Request agregando la fecha de la ejecucuion



## Mongo
Inicialmente para usar mongo con 1 cluster con 3 replicas en la informacion de la base de datos esa en application.conf, tener en cuenta que en mongo atlas se dee solicitar permisos para poder ver la base de datos.
