name    := "fusion"
version := "0.0.1-SNAPSHOT"

scalaVersion := "2.13.3"
scalacOptions += "-deprecation"

libraryDependencies ++= Seq(
  "org.http4s"            %% "http4s-blaze-server" % Ver.http4s,
  "org.http4s"            %% "http4s-circe"        % Ver.http4s,
  "org.http4s"            %% "http4s-dsl"          % Ver.http4s,
  "io.circe"              %% "circe-generic"       % Ver.circe,
  "org.tpolecat"          %% "doobie-core"         % Ver.doobie,
  "com.h2database"        %  "h2"                  % Ver.h2,
  "com.github.pureconfig" %% "pureconfig"          % Ver.pureConfig,
  "dev.zio"               %% "zio-interop-cats"    % Ver.zio_catz,
  "dev.zio"               %% "zio-logging-slf4j"   % "0.5.3",
  "dev.zio"               %% "zio"                 % Ver.zio,
  "dev.zio"               %% "zio-test"            % Ver.zio % Test,
  "dev.zio"               %% "zio-test-sbt"        % Ver.zio % Test,
)

testFrameworks += TestFramework("zio.test.sbt.ZTestFramework")
