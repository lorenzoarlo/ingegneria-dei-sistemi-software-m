# Problema
Quando si spegne il pc utilizza di default la versione di Java 25, ma è richiesta la versione 17.
Per risolvere
```bash
./gradlew --stop # Stop any running Gradle daemons
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home 
``` 