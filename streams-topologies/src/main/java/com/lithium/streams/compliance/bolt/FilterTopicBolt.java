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

import com.lithium.streams.compliance.exception.FilterException;
import com.lithium.streams.compliance.service.FilteringService;
import com.lithium.streams.compliance.service.impl.FilteringServiceImpl;

/**
 * 
 * @author vijay.polsani
 *	NOT USED in the TOPOLOGY.
 */
public class FilterTopicBolt extends BaseRichBolt {
	private static final long serialVersionUID = -13585387950438323L;
	//This data need to be present for processing.
	private static final String TOPIC_NAME = "sathi.qa";
	private static final String INPUT_FIELD_DATA = "kafkaTxEvent";
	private static final String OUTPUT_FIELD_DATA = "kafkaCommunityEvent";
	private static final String OUTPUT_FIELD_DEST = "topicName";

	private FilteringService filteringService = null;
	private OutputCollector boltOutputCollector = null;

	private static final Logger log = LoggerFactory.getLogger(FilterTopicBolt.class);

	public FilterTopicBolt() {
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		filteringService = new FilteringServiceImpl();
		this.boltOutputCollector = collector;
	}

	@Override
	public void execute(Tuple input) {
		String incomingData = input.getStringByField(INPUT_FIELD_DATA);
		try {
			if (filteringService.isLiaEventBelongsToId(incomingData, TOPIC_NAME)) {
				boltOutputCollector.emit(new Values(filteringService, TOPIC_NAME));
				boltOutputCollector.ack(input);
				log.info(">>> Found the Topic from Event : " + TOPIC_NAME);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new FilterException("Filtering failed on the topic name " + TOPIC_NAME + e);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(OUTPUT_FIELD_DATA, OUTPUT_FIELD_DEST));
	}
}
