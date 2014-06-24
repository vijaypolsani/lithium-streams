package com.lithium.streams.compliance.topology;

import java.util.concurrent.ExecutionException;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

import com.lithium.streams.compliance.bolt.KafkaProducerBolt;
import com.lithium.streams.compliance.bolt.TopicNameBolt;
import com.lithium.streams.compliance.bolt.TransformActivityStreams1Bolt;
import com.lithium.streams.compliance.exception.ComplianceTopologyException;
import com.lithium.streams.compliance.spout.LiaKafkaConsumerSpout;

public class ComplianceTopology {

	//private static final String LOCAL_DEPLOYMENT_TOPOLOGY = "/Users/vijay.polsani/_eclipseworkspace/lithium-streams/streams-topologies/target/streams-topologies-0.0.1-jar-with-dependencies.jar";
	private static final String LOCAL_AWS_TOPOLOGY = "/home/user/app/apache-storm-0.9.1-incubating/deploy/streams-topologies-0.0.1-jar-with-dependencies.jar";

	public ComplianceTopology() {
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		TopologyBuilder topologyBuilder = new TopologyBuilder();
		topologyBuilder.setSpout("kafkaConsumerSpout", new LiaKafkaConsumerSpout(), 4);
		topologyBuilder.setBolt("transformActivityStreams1Bolt", new TransformActivityStreams1Bolt(), 4)
				.shuffleGrouping("kafkaConsumerSpout");
		topologyBuilder.setBolt("topicNameBolt", new TopicNameBolt(), 4).shuffleGrouping(
				"transformActivityStreams1Bolt");
		topologyBuilder.setBolt("kafkaProducerBolt", new KafkaProducerBolt(), 4).shuffleGrouping("topicNameBolt");
		Config config = new Config();
		//System.setProperty("storm.jar", LOCAL_DEPLOYMENT_TOPOLOGY);
		System.setProperty("storm.jar", LOCAL_AWS_TOPOLOGY);
		config.setDebug(true);
		config.setNumWorkers(4);
		//config.setFallBackOnJavaSerialization(true);
		try {
			StormSubmitter.submitTopology("compliance", config, topologyBuilder.createTopology());
		} catch (InvalidTopologyException e) {
			e.printStackTrace();
			throw new ComplianceTopologyException("Compliance topology could not be submitted to STORM.", e);
		} catch (AlreadyAliveException e) {
			e.printStackTrace();
			throw new ComplianceTopologyException("Compliance topology *Already RUNNING* in STORM.", e);
		}

		if (args != null && args.length > 0) {
			config.setNumWorkers(4);
			try {
				StormSubmitter.submitTopology(args[0], config, topologyBuilder.createTopology());
			} catch (InvalidTopologyException e) {
				e.printStackTrace();
			} catch (AlreadyAliveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			LocalCluster localCluster = new LocalCluster();
			localCluster.submitTopology("compliance", config, topologyBuilder.createTopology());
			//Utils.sleep(10000);
			//localCluster.killTopology("sample");
			//localCluster.shutdown();
		}
	}
}
