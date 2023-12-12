FROM openjdk:17 as build

WORKDIR spring-microservices

COPY target/*.jar application.jar

RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:17

WORKDIR spring-microservices

COPY --from=build spring-microservices/dependencies/ ./
COPY --from=build spring-microservices/spring-boot-loader/ ./
COPY --from=build spring-microservices/snapshot-dependencies/ ./
COPY --from=build spring-microservices/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]