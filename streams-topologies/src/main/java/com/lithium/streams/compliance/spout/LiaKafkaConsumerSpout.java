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
import backtype.storm.tuple.Values;

import com.lithium.streams.compliance.exception.ComplianceMessageConsumerException;
import com.lithium.streams.compliance.service.KafkaConsumerGroup;

public class LiaKafkaConsumerSpout implements IRichSpout {

	private static final long serialVersionUID = -7738897064142088285L;
	private static final String LIA_STORM_KAFKA_CONSUMER = "Lia-Storm-Kafka-Consumer-Spout";
	private static final String ZOOKEEPER_HOST_URL = "10.240.163.94:2181";
	private static final String ZOOKEEPER_CONNECTION_TIMEOUT = "5000";
	private static final String OUTPUT_FIELD = "kafkaEvent";

	private KafkaConsumerGroup consumerGroup = null;
	private SpoutOutputCollector spoutOutputCollector = null;
	private TopologyContext topologyContext = null;
	private Map config = null;

	private static final Logger log = LoggerFactory.getLogger(LiaKafkaConsumerSpout.class);

	public LiaKafkaConsumerSpout() {
		super();
	}

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		try {
			consumerGroup = new KafkaConsumerGroup(ZOOKEEPER_HOST_URL, LIA_STORM_KAFKA_CONSUMER,
					ZOOKEEPER_CONNECTION_TIMEOUT);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.spoutOutputCollector = collector;
		this.topologyContext = context;
		this.config = config;
	}

	@Override
	public void close() {
	}

	@Override
	public void activate() {
	}

	@Override
	public void deactivate() {
	}

	@Override
	public void nextTuple() {
		try {
			synchronized (consumerGroup.getLock()) {
				String data = consumerGroup.getLock().getJsonContent();
				log.info(">>> Spout reading next data from the ConsumerGroup: " + data);
				if (data != null)
					spoutOutputCollector.emit(new Values(data));
				try {
					consumerGroup.getLock().wait();
				} catch (InterruptedException e) {
					log.info("<<< In ConsumerGroup: Interrupted Message");
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			throw new ComplianceMessageConsumerException("<<< Spout unable to retrieve message from Kafka. ", e);
		}
	}

	@Override
	public void ack(Object msgId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fail(Object msgId) {
		// TODO Auto-generated method stub

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
