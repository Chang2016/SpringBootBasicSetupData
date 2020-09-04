FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=target/dependency
# COPY <source>... <destination>
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/BOOT-INF/classes/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENV spring.profiles.active=staging
EXPOSE 8080
ENTRYPOINT ["java","-cp","app:app/lib/*","org.chang.springboot.SpringBootBasicDataMain"]
