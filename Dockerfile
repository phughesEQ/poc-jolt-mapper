FROM openjdk:8-jre
ADD target/mainModule-1.0-SNAPSHOT-shaded.jar app.jar
EXPOSE 9000:9000
ENTRYPOINT ["java","-cp","/app.jar","MainKt"]