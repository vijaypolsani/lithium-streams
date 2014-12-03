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
import com.lithium.streams.compliance.exception.ComplianceTopologyException;
import com.lithium.streams.compliance.spout.LiaKafkaConsumerSpout;

public class ComplianceTopology {

	//private static final String LOCAL_DEPLOYMENT_TOPOLOGY = "/Users/vijay.polsani/_eclipseworkspace/lithium-streams/streams-topologies/target/streams-topologies-0.0.1-jar-with-dependencies.jar";
	private static final String LOCAL_AWS_TOPOLOGY = "/opt/app/storm/apache-storm-0.9.3/deploy/streams-topologies-0.0.1-jar-with-dependencies.jar";

	public ComplianceTopology() {
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		TopologyBuilder topologyBuilder = new TopologyBuilder();
		/*
		 * This Includes Transformation
		topologyBuilder.setSpout("kafkaConsumerSpout", new LiaKafkaConsumerSpout(), 4);
		topologyBuilder.setBolt("transformActivityStreams1Bolt", new TransformActivityStreams1Bolt(), 4).shuffleGrouping("kafkaConsumerSpout");
		topologyBuilder.setBolt("topicNameBolt", new TopicNameBolt(), 4).shuffleGrouping("transformActivityStreams1Bolt");
		topologyBuilder.setBolt("kafkaProducerBolt", new KafkaProducerBolt(), 4).shuffleGrouping("topicNameBolt");
		*/
		topologyBuilder.setSpout("kafkaConsumerSpout", new LiaKafkaConsumerSpout(), 1);
		topologyBuilder.setBolt("topicNameBolt", new TopicNameBolt(), 2).shuffleGrouping("kafkaConsumerSpout");
		topologyBuilder.setBolt("kafkaProducerBolt", new KafkaProducerBolt(), 2).shuffleGrouping("topicNameBolt");
		Config config = new Config();
		//System.setProperty("storm.jar", LOCAL_DEPLOYMENT_TOPOLOGY);
		System.setProperty("storm.jar", LOCAL_AWS_TOPOLOGY);
		config.setDebug(false);
		config.setNumWorkers(1);
		//config.setFallBackOnJavaSerialization(true);
		try {
			StormSubmitter.submitTopology("Compliance", config, topologyBuilder.createTopology());
		} catch (InvalidTopologyException e) {
			e.printStackTrace();
			throw new ComplianceTopologyException("<<< Compliance topology could not be submitted to STORM.", e);
		} catch (AlreadyAliveException e) {
			e.printStackTrace();
			throw new ComplianceTopologyException("<<< Compliance topology *Already RUNNING* in STORM.", e);
		}
	}
}
