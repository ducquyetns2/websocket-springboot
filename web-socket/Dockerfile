FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY .mvn .mvn
COPY pom.xml mvnw ./
RUN ["./mvnw","dependency:go-offline"]
COPY src ./src
CMD ["./mvnw","spring-boot:run"]

