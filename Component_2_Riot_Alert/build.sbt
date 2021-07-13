name :="Alert Streaming"
scalaVersion := "2.12.14"

libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.7.0"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "org.apache.kafka" % "kafka-streams" % "2.7.0"
libraryDependencies += "org.apache.spark" %% "spark-core" % "3.1.0"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.1.0"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "3.1.0"