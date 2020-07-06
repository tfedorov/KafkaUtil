# KafkaUtil
Scala wrapper for Kafka Producer/Consumer

Quick steps for launching Apache Kafka instance. **Confluent Platform** a set of docker images for Kafka.

###  Download and Start Confluent Platform
https://docs.confluent.io/current/quickstart/ce-docker-quickstart.html

```sh
cd cp-all-in-one
docker-compose up -d
```
### Create a topic
http://localhost:9021/clusters/bkULLWnWTJe1sFacnjpL7g/management/create-topic
Go to Confluent admin panel and create a topic **topic4test**

#Push message
Launch **com.tfedorov.ProducerApp**

### Console consumer
Run inside broker container
```sh
docker-compose exec broker ./usr/bin/kafka-console-consumer --topic topic4test --bootstrap-server :9092
```
