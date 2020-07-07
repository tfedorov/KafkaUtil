# KafkaUtil
Scala wrapper for Kafka Producer/Consumer

Quick steps for launching Apache Kafka instance. **Confluent Platform** a set of docker images for Kafka.

###  Download and Start Confluent Platform

[Docker Confluent](https://docs.confluent.io/current/quickstart/ce-docker-quickstart.html) quickstart. And instructions
Docker compose file.

```sh
cd cp-all-in-one
docker-compose up -d
```
### Create a topic
[Admin UI](http://localhost:9021/clusters/bkULLWnWTJe1sFacnjpL7g/management/create-topic) Localhost ui if docker-compose is up

Or via CLI
```sh
docker-compose exec broker kafka-topics --create --topic topic4test --partitions 2 --replication-factor 1 --if-not-exists --zookeeper zookeeper:2181
```
Go to Confluent admin panel and create a topic **topic4test**

### Push a message
Launch **com.tfedorov.ProducerApp**

### Console consumer
[Admin UI](http://localhost:9021/clusters/d2gti2QWQ8-g_CfJp5llzQ/management/topics/topic4test/message-viewer) for looking messages in the topics.

Run inside broker container
```sh
docker-compose exec broker ./usr/bin/kafka-console-consumer --topic topic4test --bootstrap-server :9092
```
