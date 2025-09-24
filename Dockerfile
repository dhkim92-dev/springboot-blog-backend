FROM openjdk:21-jdk-slim
COPY build/libs/blog.restapi-1.1.1.jar /app.jar
ENV SPRING_PROFILES_ACTIVE=prod
CMD ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "/app.jar"]