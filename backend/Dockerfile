FROM maven:3.8.1-openjdk-11 as builder

ENV http_proxy http://wwwproxy.unimelb.edu.au:8000/
ENV https_proxy http://wwwproxy.unimelb.edu.au:8000/
ENV HTTP_PROXY http://wwwproxy.unimelb.edu.au:8000/
ENV HTTPS_PROXY http://wwwproxy.unimelb.edu.au:8000/
ENV no_proxy localhost,127.0.0.1,localaddress,172.16.0.0/12,.melbourne.rc.nectar.org.au,.storage.u nimelb.edu.au,.cloud.unimelb.edu.au

ADD ./pom.xml pom.xml
ADD ./src src/

RUN mvn clean package


FROM openjdk:11

ENV http_proxy http://wwwproxy.unimelb.edu.au:8000/
ENV https_proxy http://wwwproxy.unimelb.edu.au:8000/
ENV HTTP_PROXY http://wwwproxy.unimelb.edu.au:8000/
ENV HTTPS_PROXY http://wwwproxy.unimelb.edu.au:8000/
ENV no_proxy localhost,127.0.0.1,localaddress,172.16.0.0/12,.melbourne.rc.nectar.org.au,.storage.u nimelb.edu.au,.cloud.unimelb.edu.au

EXPOSE 8080

RUN mkdir /workspace
RUN mkdir /workspace/files
RUN mkdir /workspace/output
WORKDIR /workspace

RUN apt-get update \
    && apt-get install -y python3 python3-pip \
    && ln -s /usr/bin/pip3 /usr/local/bin/pip \
    && ln -s /usr/bin/python3 /usr/local/bin/python

ADD requirements.txt /workspace
RUN pip3 install -r requirements.txt

COPY --from=builder target/proj2-0.0.1-SNAPSHOT.jar proj.jar
COPY ../python/crawl_covid_cases.py crawl_covid_cases.py
COPY ../python/connect_db.py connect_db.py
COPY ../python/database.py database.py
COPY ../crawl/tweepy_geo.py tweepy_geo.py
COPY files/* /workspace/files/

RUN chmod +x proj.jar
RUN ls -la
RUN cd files/ \
	&& ls -la

CMD ["java", "-jar", "proj.jar"]
