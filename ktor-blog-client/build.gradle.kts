import com.github.gradle.node.npm.task.NpmTask

plugins {
    java
    id("com.github.node-gradle.node") version "7.0.1"
}

node {
    version.set("20.8.0")
    npmVersion.set("10.2.0")
}

group = "com.example"
version = "0.0.1"

val npmBuild = tasks.register<NpmTask>("npmBuild") {
    dependsOn(tasks.npmInstall)
    npmCommand.set(listOf("run", "build"))
}

//tasks.register<Copy>("copy") {
//    dependsOn(npmBuild)
//    from(layout.projectDirectory.dir("build"))
//    into(layout.projectDirectory.dir("copy"))
//}

//jar

