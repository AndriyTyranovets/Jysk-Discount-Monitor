plugins {
    java
    application
}

group = "com.github.andriityranovets.jyskdiscount"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor(libs.record.builder.processor)
    compileOnly(libs.record.builder.core)
    implementation(libs.gson)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/main/java"))
        }
        resources {
            setSrcDirs(listOf("src/main/resources"))
        }
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.github.andriityranovets.jyskdiscount.Main"
    }

    from (
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    )
}

application {
    mainClass.set("com.github.andriityranovets.jyskdiscount.Main")
}