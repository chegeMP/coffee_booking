// The Play plugin
addSbtPlugin("org.playframework" % "sbt-plugin" % "3.0.6")

// Defines scaffolding (found under .g8 folder)
// http://www.foundweekends.org/giter8/scaffolding.html
// sbt "g8Scaffold form"
addSbtPlugin("org.foundweekends.giter8" % "sbt-giter8-scaffold" % "0.17.0")

addSbtPlugin("org.playframework" % "sbt-play-ebean" % "8.2.0")