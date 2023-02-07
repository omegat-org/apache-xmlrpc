plugins {
    `java-library`
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

allprojects {
    group = "org.omegat"
    version = "3.1.3-20230208-SNAPSHOT"
}

nexusPublishing {
    repositories{
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(project.property("sonatypeUsername")?.toString()?: System.getenv("SONATYPE_USER"))
            password.set(project.property("sonatypePassword")?.toString()?: System.getenv("SONATYPE_PASS"))
        }
    }
}

val desc: String by project
val project_url: String by project
val scm_url: String by project

subprojects {
    apply(plugin="java-library")
    apply(plugin="maven-publish")
    apply(plugin="signing")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(8))
        }
        withSourcesJar()
    }

    repositories {
        mavenCentral()
    }

    val signKey = listOf("signingKey", "signing.keyId", "signing.gnupg.keyName").find {project.hasProperty(it)}
    signing {
        when (signKey) {
            "signingKey" -> {
                val signingKey: String? by project
                val signingPassword: String? by project
                useInMemoryPgpKeys(signingKey, signingPassword)
            }
            "signing.keyId" -> {
                val keyId: String? by project
                val password: String? by project
                val secretKeyRingFile: String? by project // e.g. gpg --export-secret-keys > secring.gpg
                useInMemoryPgpKeys(keyId, password, secretKeyRingFile)
            }
            "signing.gnupg.keyName" -> {
                useGpgCmd()
            }
        }
    }
}

project(":xmlrpc-common") {
   //  = "xmlrpc-common"
    dependencies {
        implementation("jaxme:jaxmeapi:0.5.1")
        implementation("org.apache.ws.commons.util:ws-commons-util:1.0.2")
    }

    publishing {
        publications {
            create<MavenPublication>("xmlrpcCommon") {
                from(components["java"])
                pom {
                    name.set("xmlrpc-common")
                    description.set(desc)
                    url.set(project_url)
                    licenses {
                        license {
                            name.set("Apache License 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0")
                            distribution.set("repo")
                        }
                    }
                    developers {
                        developer {
                            id.set("miurahr")
                            name.set("Hiroshi Miura")
                            email.set("miurahr@linux.com")
                        }
                    }
                    scm {
                        connection.set(scm_url)
                        developerConnection.set(scm_url)
                        url.set(project_url)
                    }
                }
            }
        }
    }
    signing.sign(publishing.publications["xmlrpcCommon"])
}

project(":xmlrpc-client") {
    dependencies {
        implementation(project(":xmlrpc-common"))
        implementation("commons-httpclient:commons-httpclient:3.0.1")
    }

    publishing {
        publications {
            create<MavenPublication>("xmlrpcClient") {
                from(components["java"])
                pom {
                    name.set("xmlrpc-client")
                    description.set(desc)
                    url.set(project_url)
                    licenses {
                        license {
                            name.set("Apache License 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0")
                            distribution.set("repo")
                        }
                    }
                    developers {
                        developer {
                            id.set("miurahr")
                            name.set("Hiroshi Miura")
                            email.set("miurahr@linux.com")
                        }
                    }
                    scm {
                        connection.set(scm_url)
                        developerConnection.set(scm_url)
                        url.set(project_url)
                    }
                }
            }
        }
    }
    signing.sign(publishing.publications["xmlrpcClient"])
}

project(":xmlrpc-server") {
    dependencies {
        implementation(project(":xmlrpc-common"))
        implementation(project(":xmlrpc-client"))
        implementation("commons-httpclient:commons-httpclient:3.0.1")
        implementation("commons-logging:commons-logging:1.1")
        implementation("javax.servlet:servlet-api:2.4")
        testImplementation("junit:junit:3.8.1")
        testImplementation("org.apache.ws.commons.util:ws-commons-util:1.0.2")
    }
}