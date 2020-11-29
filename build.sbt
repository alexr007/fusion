name    := "webApp"
version := "0.0.1-SNAPSHOT"

val Http4sVersion = "0.21.13"
val ZioVersion    = "1.0.3"
val CirceVersion  = "0.13.0"
val DoobieVersion = "0.9.4"
val H2Version     = "1.4.196"

scalaVersion := "2.13.3"
scalacOptions += "-deprecation"

libraryDependencies ++= Seq(
  "org.http4s"     %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s"     %% "http4s-circe"        % Http4sVersion,
  "org.http4s"     %% "http4s-dsl"          % Http4sVersion,
  "io.circe"       %% "circe-generic"       % CirceVersion,
  "org.tpolecat"   %% "doobie-core"         % DoobieVersion,
  "com.h2database" %  "h2"                  % H2Version,
  "dev.zio"        %% "zio-interop-cats"    % "2.2.0.1",
  "dev.zio"        %% "zio-logging-slf4j"   % "0.5.3",
  "dev.zio"        %% "zio"                 % ZioVersion,
  "dev.zio"        %% "zio-test"            % ZioVersion % Test,
  "dev.zio"        %% "zio-test-sbt"        % ZioVersion % Test,
)

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
