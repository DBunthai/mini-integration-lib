1. The install task (Testing017_)
    ./gradlew build
   - Builds your .jar
   - Installs it to your local Maven repo (~/.m2/repository)
   - Makes it available to any other Gradle or Maven project on your machine

2. upgrade a new version then publishing
   ./gradlew publishToMavenLocal




--------

publish maven central publicaiton

1) Prep (one-time)

Own the groupId on Central: io.github.dbunthai.

Create a PGP key (if you don’t already):

gpg --full-generate-key
gpg --list-secret-keys --keyid-format=long
# note the LONG key id or full fingerprint, e.g. 59DBA126A455C6D6


Publish your PUBLIC key and verify the email:

gpg --keyserver hkps://keys.openpgp.org --send-keys <YOUR_LONG_KEY_ID>


Then click the verification link sent to the email embedded in your key. (Central uses this to validate signatures.)

Create a Central Portal token (username/password pair).

2) Gradle properties

Put these in ~/.gradle/gradle.properties (or the project’s gradle.properties). Do not commit secrets.

# Central Portal token
mavenCentralUsername=<your-token-username>
mavenCentralPassword=<your-token-password>

# Signing (pick ONE method; recommended = GPG cmd)
signing.gnupg.executable=gpg
signing.gnupg.keyName=<YOUR_LONG_KEY_ID_OR_FINGERPRINT>
# omit if your key has NO passphrase:
signing.gnupg.passphrase=<your-passphrase>

# (optional) speed
org.gradle.parallel=true
org.gradle.caching=true


Do not also set signing.secretKeyRingFile or signingInMemoryKey* when using GPG mode.

3) build.gradle (library module)

Keep your dependencies. Make the output deterministic and publish with Vanniktech only.

4) Local sanity checks (before Central)
# Clean build to avoid stale bytes
./gradlew clean :<modulePath>:publishToMavenLocal --no-parallel --no-build-cache --rerun-tasks --info
# Example: :mini-integration-lib:publishToMavenLocal


Verify outputs:

build/libs/<artifactId>-<version>.jar (no -plain)

Signatures exist and validate:

gpg --verify build/libs/<artifactId>-<version>.jar.asc        build/libs/<artifactId>-<version>.jar
gpg --verify build/libs/<artifactId>-<version>-sources.jar.asc build/libs/<artifactId>-<version>-sources.jar
gpg --verify build/libs/<artifactId>-<version>-javadoc.jar.asc build/libs/<artifactId>-<version>-javadoc.jar
# POM path (usually):
gpg --verify build/publications/maven/<artifactId>-<version>.pom.asc \
build/publications/maven/<artifactId>-<version>.pom


All should say Good signature.

5) Push to Central (upload)
   ./gradlew :<modulePath>:publishAllPublicationsToMavenCentralRepository \
   --no-parallel --no-build-cache --no-configuration-cache --rerun-tasks --info


You’ll see the new Deployment in the Central Portal UI.

6) Finalize in the Portal

When the Deployment shows VALIDATED, click Publish.

After a short delay, the artifact is public and immutable.

7) Verify consumption

Try from a fresh project:

Gradle

dependencies {
implementation "io.github.dbunthai:<artifactId>:<version>"
}


Maven

<dependency>
  <groupId>io.github.dbunthai</groupId>
  <artifactId><artifactId></artifactId>
  <version><version></version>
</dependency>
