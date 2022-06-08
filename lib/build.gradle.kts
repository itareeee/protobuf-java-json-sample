plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.31"
    id("com.google.protobuf") version "0.8.18"
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.google.guava:guava:30.1.1-jre")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    api("org.apache.commons:commons-math3:3.6.1")

    implementation("com.google.protobuf:protobuf-java:3.21.1")
    implementation("com.google.protobuf:protobuf-java-util:3.21.1")
}

sourceSets {
    main {
        proto.srcDir("$rootDir/proto/")
    }
}