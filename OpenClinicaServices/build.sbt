name := "gel-openclinicaservices"
version := "2.0.0-SNAPSHOT"
scalaVersion := "2.11.7"
resolvers += "BioJava" at "http://biojava.org/download/maven/"
libraryDependencies ++= Seq(

	"com.sparkjava" % "spark-core" % "2.3",
	"javax.servlet"	% "javax.servlet-api" % "3.0.1",
	"com.google.zxing" % "javase" % "3.1.0",
	"org.biojava" % "biojava3-core" % "3.0",
	"org.biojava" % "biojava3-ontology" % "3.0.8",
	"com.google.code.gson" % "gson" % "2.3.1",
	"org.json" % "json" % "20140107",
	"org.json4s" %% "json4s-native" % "3.3.0", // Scala JSON library
	"avalon-framework" % "avalon-framework-api" % "4.2.0",
	"org.apache.xmlgraphics" % "fop" % "1.1" excludeAll(
		ExclusionRule(organization = "org.apache.avalon.framework", name = "avalon-framework-api"),
		ExclusionRule(organization = "org.apache.avalon.framework", name = "avalon-framework-impl")
	),
	"javax.servlet" % "servlet-api" % "2.5",
	"javax.xml.parsers" % "jaxp-api" % "1.4.5",
	"xalan" % "xalan" % "2.7.0",
	"avalon-framework" % "avalon-framework-impl" % "4.2.0",
	"org.apache.commons" % "commons-lang3" % "3.4",
	"ch.qos.logback" % "logback-classic" % "1.1.3",	

	"org.mockito" % "mockito-all" % "1.9.5" % "test",
	"junit" % "junit" % "4.12" % "test",  
	"org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)

EclipseKeys.withSource := true