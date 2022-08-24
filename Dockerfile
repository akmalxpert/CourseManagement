FROM tomcat:8.5.82-jdk11-corretto

RUN rm -rf /usr/local/tomcat/webapps/*

ARG WAR_FILE=web/target/backend.war

COPY ${WAR_FILE} /usr/local/tomcat/webapps/

CMD ["catalina.sh","run"]