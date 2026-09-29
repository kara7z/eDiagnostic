# eDiagnostic (Jakarta EE 10)

## Requirements
- Java 17
- Maven 3.9+
- Payara Micro 6 (Jakarta EE 10 runtime)

## Run
```sh
mvn -q -DskipTests package
java -jar payara-micro.jar --deploy target/eDiagnostic.war --port 8080
```

Test: `http://localhost:8080/eDiagnostic/hello`

## Download runtime (once)
```sh
curl -L -o payara-micro.jar https://repo1.maven.org/maven2/fish/payara/extras/payara-micro/6.2025.6/payara-micro-6.2025.6.jar
```
