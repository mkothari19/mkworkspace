#!/bin/bash
$KAFKA_HOME/bin/kafka-console-consumer.sh --topic streaming-events --from-beginning --bootstrap-server localhost:9092
