package com.lithium.streams.compliance.bolt;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lithium.streams.compliance.exception.TransformationException;
import com.lithium.streams.compliance.service.TransformationService;
import com.lithium.streams.compliance.service.impl.TransformationServiceImpl;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class TransformActivityStreams1Bolt extends BaseRichBolt {
	private static final long serialVersionUID = -13585387950438323L;
	private static final Logger log = LoggerFactory.getLogger(TransformActivityStreams1Bolt.class);
	private static TransformationServiceImpl transformationService = null;
	private OutputCollector boltOutputCollector = null;

	public TransformActivityStreams1Bolt() {
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		transformationService = new TransformationServiceImpl();
		boltOutputCollector = collector;
	}

	@Override
	public void execute(Tuple input) {
		String incomingData = input.getStringByField("kafkaEvent");
		try {
			String kafkaTxEvent = transformationService.transformToActivityStreams1(incomingData);
			boltOutputCollector.emit(new Values(kafkaTxEvent));
		} catch (IOException e) {
			e.printStackTrace();
			throw new TransformationException("Unable to transform the Kafka Event to Activity Streams 1.0.", e);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("kafkaTxEvent"));

	}

}
