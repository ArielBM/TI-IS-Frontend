FROM openjdk:22-jdk-slim AS build
WORKDIR /app

RUN apt-get update && apt-get install -y maven nodejs npm && npm install -g npm@latest

COPY pom.xml .
COPY src ./src
COPY package.json .
COPY package-lock.json .

RUN npm install

RUN mvn clean package -Pproduction

FROM openjdk:22-jdk-slim
WORKDIR /app

COPY --from=build /app/target/tech-test-1.0-SNAPSHOT.jar /app/tech-test-1.0-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "tech-test-1.0-SNAPSHOT.jar"]
