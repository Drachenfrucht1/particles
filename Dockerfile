FROM maven:3.5-jdk-8-alpine

COPY . /urs/src/gravitation-simulator

WORKDIR /urs/src/gravitation-simulator
RUN mvn install
CMD java -jar ./target/particles-0.0.1-dev.jar nogui