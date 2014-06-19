package com.lithium.streams.compliance.bolt;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.lithium.streams.compliance.exception.TransformationException;
import com.lithium.streams.compliance.service.FilteringService;
import com.lithium.streams.compliance.service.impl.FilteringServiceImpl;

public class TopicNameBolt extends BaseRichBolt {
	private static final long serialVersionUID = -13585387950438323L;
	private static final String INPUT_FIELD_DATA = "kafkaTxEvent";
	private static final String OUTPUT_FIELD_DATA = "kafkaCommunityEvent";
	private static final String OUTPUT_FIELD_DEST = "topicName";
	private static final Logger log = LoggerFactory.getLogger(TopicNameBolt.class);
	private static FilteringService filteringService = null;
	private OutputCollector boltOutputCollector = null;

	public TopicNameBolt() {
		super();
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		filteringService = new FilteringServiceImpl();
		boltOutputCollector = collector;
	}

	@Override
	public void execute(Tuple input) {
		String incomingEvent = input.getStringByField(INPUT_FIELD_DATA);
		try {
			String topicName = filteringService.parseIncomingsJsonStreamForCommunityId(incomingEvent);
			boltOutputCollector.emit(new Values(incomingEvent, topicName));
			log.info(">>> Found the Topic from Event : " + topicName);
			boltOutputCollector.ack(input);
		} catch (IOException e) {
			e.printStackTrace();
			throw new TransformationException("Unable to Find Topic Name 'Source:' tag from the Kafka Event.", e);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(OUTPUT_FIELD_DATA, OUTPUT_FIELD_DEST));

	}

}
