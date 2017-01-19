logLevel := Level.Warn

resolvers += "Socrata Cloudbees" at "https://repo.socrata.com/artifactory/lib-release"

addSbtPlugin("com.socrata" % "socrata-sbt-plugins" % "1.6.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.6")
