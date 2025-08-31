1. The install task
    ./gradlew build
   - Builds your .jar
   - Installs it to your local Maven repo (~/.m2/repository)
   - Makes it available to any other Gradle or Maven project on your machine

2. upgrade a new version then publishing
   ./gradlew publishToMavenLocal