package com.lithium.streams.compliance.sample;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class HelloWorldTopology {

	public HelloWorldTopology() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		TopologyBuilder topologyBuilder = new TopologyBuilder();
		topologyBuilder.setSpout("randomHelloWorld", new HelloWorldSpout(), 10);
		topologyBuilder.setBolt("helloWorldBolt", new HelloWorldBolt(), 10).shuffleGrouping("randomHelloWorld");
		Config config = new Config();
		config.setDebug(true);
		if (args != null && args.length > 0) {
			config.setNumWorkers(20);
			try {
				StormSubmitter.submitTopology(args[0], config, topologyBuilder.createTopology());
			} catch (AlreadyAliveException | InvalidTopologyException e) {
				e.printStackTrace();
			}
		} else {
			LocalCluster localCluster = new LocalCluster();
			localCluster.submitTopology("sample", config, topologyBuilder.createTopology());
			//Utils.sleep(10000);
			//localCluster.killTopology("sample");
			//localCluster.shutdown();
		}
	}
}
