package com.lithium.streams.compliance.bolt;

import java.util.Map;
import java.util.Properties;

import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lithium.streams.compliance.exception.ComplianceMessageSendException;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class KafkaProducerBolt extends BaseRichBolt {
	private static final long serialVersionUID = -13585387950438323L;
	private static final String INTPUT_FIELD_DATA = "kafkaCommunityEvent";
	private static final String INTPUT_FIELD_DEST = "topicName";
	private static final String KAFKA_AWS_BROKER_URL = "10.240.163.94:9092";
	//private static final String KAFKA_LOCALHOST_BROKER_URL = "localhost:9092";

	private static final Logger log = LoggerFactory.getLogger(KafkaProducerBolt.class);
	private static kafka.javaapi.producer.Producer<Integer, String> producer = null;
	private OutputCollector boltOutputCollector = null;

	public KafkaProducerBolt() {
		super();
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.boltOutputCollector = collector;
		Properties props = new Properties();
		//props.put("metadata.broker.list", KAFKA_LOCALHOST_BROKER_URL); 
		props.put("metadata.broker.list", KAFKA_AWS_BROKER_URL); //AWS Instance
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		if (producer == null)
			producer = new kafka.javaapi.producer.Producer<Integer, String>(new ProducerConfig(props));
	}

	@Override
	public void execute(Tuple input) {
		String incomingData = input.getStringByField(INTPUT_FIELD_DATA);
		String topicName = input.getStringByField(INTPUT_FIELD_DEST);
		if (incomingData != null && topicName != null) {
			try {
				producer.send(new KeyedMessage<Integer, String>(topicName, incomingData));
				//log.info(">>> **** Sent to KAFKA: to Topic: **** " + topicName + " with data " + incomingData);
				log.info(">>> $$$ Sent to KAFKA: to Topic: **** " + topicName);
				boltOutputCollector.ack(input);
			} catch (Exception e) {
				e.printStackTrace();
				boltOutputCollector.fail(input);
				throw new ComplianceMessageSendException("Could not send Kafka event to topic: " + topicName, e);
			}
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

}
