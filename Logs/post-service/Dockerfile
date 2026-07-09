FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -pl post-service -am -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/post-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
