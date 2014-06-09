package org.streams.topologies.sample;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class HelloWorldBolt extends BaseRichBolt {
	private static final long serialVersionUID = -13585387950438323L;
	private static final Logger log = LoggerFactory.getLogger(HelloWorldBolt.class);
	private int hitCount = 0;
	private int missCount = 0;

	public HelloWorldBolt() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute(Tuple input) {
		String incomingData = input.getStringByField("sentence");
		if (incomingData.equals("Hello World")) {
			hitCount++;
			log.info("Success found Hello World, count  : " + hitCount);
		} else {
			missCount++;
			log.info("Random selection did not match Hello World, count  : " + missCount);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("hitCount", "missCount"));

	}

}
