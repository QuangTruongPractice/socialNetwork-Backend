FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/socialNetwork-1.0-SNAPSHOT.war socialNetwork.war

EXPOSE 8080
ENTRYPOINT ["java","-jar","socialNetwork.war"]
