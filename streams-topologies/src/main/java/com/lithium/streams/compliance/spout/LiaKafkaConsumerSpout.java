package com.lithium.streams.compliance.spout;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;

import com.lithium.streams.compliance.service.KafkaConsumerGroup;

public class LiaKafkaConsumerSpout implements IRichSpout {

	private static final long serialVersionUID = -7738897064142088285L;
	private static final String LIA_STORM_KAFKA_CONSUMER = "Lia-Storm-Kafka-Consumer-Spout";
	//private static final String KAFKA_LOCALHOST_BROKER_URL = "localhost:9092";
	//DEV
	//private static final String ZOOKEEPER_HOST_URL = "10.240.163.94:2181";
	//PROD
	private static final String ZOOKEEPER_HOST_URL = "10.220.186.232:2181";
	//private static final String ZOOKEEPER_HOST_URL = "10.240.163.94:2181";
	private static final String ZOOKEEPER_CONNECTION_TIMEOUT = "5000";
	private static final String OUTPUT_FIELD = "kafkaEvent";

	private KafkaConsumerGroup consumerGroup = null;
	private SpoutOutputCollector collector = null;
	private TopologyContext topologyContext = null;
	private Map config = null;

	private static final Logger log = LoggerFactory.getLogger(LiaKafkaConsumerSpout.class);

	public LiaKafkaConsumerSpout() {
		super();
	}

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		try {
			if (consumerGroup == null)
				consumerGroup = new KafkaConsumerGroup(ZOOKEEPER_HOST_URL, LIA_STORM_KAFKA_CONSUMER,
						ZOOKEEPER_CONNECTION_TIMEOUT, collector);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		this.collector = collector;
		this.topologyContext = context;
	}

	@Override
	public void close() {
		consumerGroup.shutdown();
	}

	@Override
	public void activate() {
		if (consumerGroup == null)
			try {
				consumerGroup = new KafkaConsumerGroup(ZOOKEEPER_HOST_URL, LIA_STORM_KAFKA_CONSUMER,
						ZOOKEEPER_CONNECTION_TIMEOUT, collector);
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Override
	public void deactivate() {
		consumerGroup.shutdown();
	}

	@Override
	public void nextTuple() {
		//Sleep for 1 sec to give other threads a breather.
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ie) {
			ie.getLocalizedMessage();
		}
	}

	@Override
	public void ack(Object msgId) {
		log.info(">>> KafkaSpout: In Ack. Completed Processing " + msgId);
	}

	@Override
	public void fail(Object msgId) {
		log.info("<<< ??? KafkaSpout: In fail. Failed Processing " + msgId);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(OUTPUT_FIELD));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}
