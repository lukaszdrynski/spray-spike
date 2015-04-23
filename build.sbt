organization  := "uk.gov.fco.etc.service"

version       := "0.1"

scalaVersion  := "2.11.6"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")


resolvers ++= Seq(
  "sonatype releases"   at "https://oss.sonatype.org/content/repositories/releases/",
  "sonatype snapshots"  at "https://oss.sonatype.org/content/repositories/snapshots/",
  "typesafe repo"       at "http://repo.typesafe.com/typesafe/releases/",
  "spray repo"          at "http://repo.spray.io/"
)

libraryDependencies ++= {
  val akkaV = "2.3.9"
  val sprayV = "1.3.3"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-routing" % sprayV,
    "io.spray"            %%  "spray-json"    % "1.3.1", // still no 1.3.3 release for spray-json
    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "com.typesafe.akka"   %%  "akka-slf4j"    % akkaV,
    "org.specs2"          %%  "specs2-core"   % "2.3.11" % "test",
    "ch.qos.logback"      %  "logback-classic" % "1.0.13"
  )
}

Revolver.settings
