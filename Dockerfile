FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . .
RUN ./gradlew assemble
EXPOSE 8080
CMD ./gradlew bootRun