
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src src

RUN chmod +x mvnw \
    && ./mvnw clean package -DskipTests \
    && groupadd -r appgroup \
    && useradd -r -g appgroup appuser

COPY --from=0 --chown=appuser:appgroup /app/target/backend-0.0.1-SNAPSHOT.jar /app/app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
