name := "KafkaTest"
	scalaVersion := "2.13.2"
	
	libraryDependencies ++= Seq(
					"org.apache.kafka" % "kafka-clients" % "1.0.0"
						exclude("javax.jms", "jms")
						exclude("com.sun.jdmk", "jmxtools")
						exclude("com.sun.jmx", "jmxri")
						exclude("org.slf4j", "slf4j-simple"),
					"mysql" % "mysql-connector-java" % "5.1.40"
				)
	
	libraryDependencies += "com.typesafe" % "config" % "1.3.2"
	libraryDependencies += "org.apache.kafka" % "kafka-streams" % "2.4.0"
	

	val kafkaVer = "2.4.0"
	libraryDependencies += "org.apache.kafka" %% "kafka-streams-scala" % kafkaVer
