FROM amazoncorretto:21-alpine
COPY /app    /usr/local/share/app
EXPOSE 8082
CMD ["java", "-jar", "/usr/local/share/app/family-app-spring-boot.jar"]
