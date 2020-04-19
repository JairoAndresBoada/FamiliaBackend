name := "Familia"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= mainLibs



val akkaVersion          = "2.5.21"
val akkaHttpCirceVersion = "1.24.3"
val akkaHttpVersion      = "10.1.7"
val catsCoreVersion      = "1.6.0"
val catsEffectVersion    = "1.2.0"
val circeVersion         = "0.11.1"
val monixVersion         = "3.0.0-RC1"
val pureConfigVersion    = "0.10.2"
val logBackVersion       = "1.2.3"
val mongoVersion         = "0.16.5"
val Json4sVersion        = "3.6.5"
val quicklensVersion     = "1.4.11"

val exclusionAkka      = ExclusionRule("com.typesafe.akka")
val exclusionTypelevel = ExclusionRule("org.typelevel")

lazy val mainLibs =
  Seq(
    "com.typesafe.akka"             %% "akka-actor"            % akkaVersion,
    "com.typesafe.akka"             %% "akka-http"             % akkaHttpVersion,
    "com.typesafe.akka"             %% "akka-stream"           % akkaVersion,
    "de.heikoseeberger"             %% "akka-http-circe"       % akkaHttpCirceVersion exclude ("org.typelevel", "cats-core_2.12") excludeAll (exclusionAkka),
    "com.typesafe.akka"             %% "akka-slf4j"            % akkaVersion,
    "org.typelevel"                 %% "cats-effect"           % catsEffectVersion exclude ("org.typelevel", "cats-core_2.12"),
    "org.typelevel"                 %% "cats-core"             % catsCoreVersion,
    "io.circe"                      %% "circe-bson"            % "0.3.1" excludeAll exclusionTypelevel,
    "io.circe"                      %% "circe-core"            % circeVersion excludeAll exclusionTypelevel,
    "io.circe"                      %% "circe-generic"         % circeVersion excludeAll exclusionTypelevel,
    "io.circe"                      %% "circe-parser"          % circeVersion excludeAll exclusionTypelevel,
    "io.circe"                      %% "circe-java8"           % circeVersion excludeAll exclusionTypelevel,
    "io.monix"                      %% "monix"                 % monixVersion excludeAll exclusionTypelevel,
    "com.github.pureconfig"         %% "pureconfig"            % pureConfigVersion,
    "com.github.pureconfig"         %% "pureconfig-enumeratum" % pureConfigVersion,
    "ch.qos.logback"                % "logback-classic"        % logBackVersion,
    "org.reactivemongo"             %% "reactivemongo"         % mongoVersion excludeAll exclusionAkka,
    "com.softwaremill.quicklens"    %% "quicklens"             % quicklensVersion
  )