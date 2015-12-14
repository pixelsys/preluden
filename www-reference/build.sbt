name := "www-reference"
version := "0.1-SNAPSHOT"
scalaVersion := "2.11.7"
EclipseKeys.withSource := true
libraryDependencies ++= Seq(
	"org.scalikejdbc"	%% "scalikejdbc"       		% "2.2.7",
	"ch.qos.logback"  	%  "logback-classic"   		% "1.1.3",
	"mysql"				%  "mysql-connector-java"	% "5.1.36",
	"com.github.spullara.mustache.java"	%  "compiler"	% "0.9.1",
	"org.scalatest"		%  "scalatest_2.11" 		% "2.2.4" 	% "test"
)

