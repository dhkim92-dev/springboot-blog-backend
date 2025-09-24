FROM openjdk:21-jdk-slim
COPY build/libs/blog.restapi-1.1.1.jar /app.jar
CMD ["java", "-jar", "/app.jar"]