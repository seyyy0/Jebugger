plugins {
    id("java")
}


version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}


tasks.register<Exec>("compileC") {

    commandLine(
        "gcc",
        "-o",
        "sum.exe",
        "-g",
        "sum.c"
    )
    workingDir = File(project.projectDir.toString())
    dependsOn(tasks.getByName("clean"))
}

tasks.named("build") {

    dependsOn("compileC")
}