FROM maven:3.5-jdk-8

COPY . /urs/src/gravitation-simulator

WORKDIR /urs/src/gravitation-simulator
RUN apt-get update && apt-get install openjfx -y && mvn install
EXPOSE 4000
CMD java -jar ./target/particles-0.0.1-dev.jar nogui
