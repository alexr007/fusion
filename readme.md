Simple ZIO + HTTP4s + Doobie + Circe POC to show how to build a purely functional web application in Scala, using functional effects with ZIO.

This is just an example to show how to retrieve and update a stock value in a database, validate this value ( > 0) and return a result as json.

How to run the sample : 

 * sbt run
 * open http://localhost:8080/stock/1

- taken [here](https://github.com/loicdescotte/pureWebappSample)
- in process of constant improvement according to understanding process

#### updates
- scala 2.13.3
- sbt 1.4.3
- zio 1.0.3
- catz interop 2.2.0.1
- doobie 0.9.4
- https 0.21.13
- zio-logging-slf4j 0.5.3
