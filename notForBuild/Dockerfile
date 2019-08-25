FROM centos

RUN yum install -y java

VOLUME /tmp
ADD /web-1.0-SNAPSHOT-jar-with-dependencies.jar myapp.jar
RUN sh -c 'touch /myapp'
EXPOSE 8080
ENTRYPOINT ["java","-jar","/myapp.jar"]