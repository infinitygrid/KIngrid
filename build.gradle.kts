plugins {
    kotlin("jvm") version "1.5.10"
}

group = "net.infinitygrid.infinibot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://m2.dv8tion.net/releases")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.code.gson", "gson", "2.8.7")
    implementation("com.google.guava:guava:30.1.1-jre")
    implementation("net.dv8tion:JDA:4.2.1_269")
}
