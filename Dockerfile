# Build Stage
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

# Deploy Stage
FROM tomcat:10.0-jdk17-temurin
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]
