name := "HelloWorld"
version := "1.0"
scalaVersion := "2.13.14"

libraryDependencies ++= Seq(
  "dev.restate" % "sdk-api" % "1.0.0",
  "dev.restate" % "sdk-http-vertx" % "1.0.0"
)

Compile / PB.targets := Seq(
  scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
)

// (optional) If you need scalapb/scalapb.proto or anything from
// google/protobuf/*.proto
libraryDependencies ++= Seq(
  "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
  "com.thesamet.scalapb" %% "scalapb-json4s" % "0.11.1",
  "dev.zio" %% "zio" % "2.1.7"
)