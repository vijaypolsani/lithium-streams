package com.lithium.streams.compliance.sample;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class HelloWorldSpout extends BaseRichSpout {

	/**
	 * Generated Serial ID
	 */
	private static final long serialVersionUID = 4447202260193410951L;
	private final int random;
	private SpoutOutputCollector spoutOutputCollector;

	public HelloWorldSpout() {
		super();
		random = ThreadLocalRandom.current().nextInt();
	}

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.spoutOutputCollector = collector;

	}

	@Override
	public void nextTuple() {
		Utils.sleep(1000);
		if (random == ThreadLocalRandom.current().nextInt()) {
			spoutOutputCollector.emit(new Values("Hello World"));
		} else
			spoutOutputCollector.emit(new Values("Another Random World"));

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("sentence"));

	}

}
