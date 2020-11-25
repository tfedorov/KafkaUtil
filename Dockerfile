FROM openjdk:8u131-jre-alpine
COPY ./target/scala-2.12/KafkaUtil-assembly-0.1.jar /usr/app/
WORKDIR /usr/app
EXPOSE 9094
ENTRYPOINT ["java", "-cp", "KafkaUtil-assembly-0.1.jar","com.tfedorov.ProducerApp"]