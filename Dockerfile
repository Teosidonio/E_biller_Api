FROM 3.6.3-jdk-8
MAINTAINER Teodorico Mazivila (teosweb77@gmail.com)
RUN apt-get update
RUN apt-get install -y maven
COPY pom.xml C:\Users\a241085\OneDrive - Standard Bank\Documents\Work\Projects\e_biller_api\pom.xml
COPY src/* C:\Users\a241085\OneDrive - Standard Bank\Documents\Work\Projects\e_biller_api\src/*
WORKDIR /src/main
RUN mvn package
CMD ["java", "-cp", "target/e_biller-0.0.1-SNAPSHOT.war", "com.standardbank.e_biller.api"]