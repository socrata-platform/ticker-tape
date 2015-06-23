logLevel := Level.Warn

resolvers += "Socrata Cloudbees" at "https://repository-socrata-oss.forge.cloudbees.com/release"

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.13.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.2")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.0")