import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask    //Import DependencyUpdatesTask dependency from github.

plugins {    //Imports Below Plugin
    java
    idea
    eclipse
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.github.ben-manes.versions")
    id("com.diffplug.spotless")
}

java {        //Toolchain for setting Required Java version.
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
    }
}

repositories {
    mavenCentral()   //Specifies Maven Central as source of dependency.
}

sourceSets {
    create("functionalTest") {   //Create source set as functionalTest.
        java {
            compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output     //Adds main and test output to classpath.
            runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
            srcDir("src/functional-test/java")  //Source directory for functional test.
        }
    }
}

idea {
    module {
        testSources.from(sourceSets["functionalTest"].java.srcDirs)  //srcDir above is Test source.
    }
}

val functionalTestImplementation: Configuration by configurations.getting {    //This configuration functionalTestImplementation, takes from implementation. 
    extendsFrom(configurations.implementation.get())    
}
val functionalTestRuntimeOnly: Configuration by configurations.getting         //This configuration functionalTestRuntime, takes from implementation.

configurations {
    configurations["functionalTestImplementation"].extendsFrom(configurations.testImplementation.get())  //This configuration functionalTestImplementation, takes from testImplementation. 
    configurations["functionalTestRuntimeOnly"].extendsFrom(configurations.testRuntimeOnly.get())   //This configuration functionalTestImplementation, takes from testRuntime. 
}


val functionalTest = task<Test>("functionalTest") {                      
    description = "Runs functional tests."
    group = "verification"

    testClassesDirs = sourceSets["functionalTest"].output.classesDirs
    classpath = sourceSets["functionalTest"].runtimeClasspath
    shouldRunAfter("test")  //Runs after test task.

    useJUnitPlatform()    //Uses JUnit5.

    testLogging {
        events ("failed", "passed", "skipped", "standard_out")   //configuring logging.
    }
}


dependencies {
    /* Spring Boot */
    implementation ("org.springframework.boot:spring-boot-starter-web")    //configures spring-boot-starter-web.
    testImplementation("org.springframework.boot:spring-boot-starter-test") {  //configures spring-boot-starter-test.
        exclude (group = "org.junit.vintage", module = "junit-vintage-engine")  //excludes junit5 vintage
    }
}

tasks.named<Test>("test") {  //Configures the test task to use JUnit 5 and log detailed test events (e.g., passed, failed).
    useJUnitPlatform()

    testLogging {
        events ("failed", "passed", "skipped", "standard_out")
    }
}

tasks.check { dependsOn(functionalTest) }   //Adds functionalTest as a dependency for the check task.


fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) //Identifiers of Unstable version.}
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.withType<DependencyUpdatesTask> {  //creates gradles dependency tasks and execute unstable version.
    rejectVersionIf {
        isNonStable(candidate.version)
    }
    gradleReleaseChannel="current"
}

spotless {  // code formatting and linting plugin for Gradle that ensures your project's code adheres to a consistent style and formatting rules.
    java {
        palantirJavaFormat()// Applies Palantir Java code formatter.
        formatAnnotations()// Formats annotations in code.
    }
}