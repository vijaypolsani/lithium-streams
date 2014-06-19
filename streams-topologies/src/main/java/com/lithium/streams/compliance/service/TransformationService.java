package com.lithium.streams.compliance.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;

public interface TransformationService {

	public static final String SAMPLE_INPUT = "{\"event\":{\"id\":32250902,\"type\":\"ActionStart\",\"time\":1400014566536,\"frameId\":32250849,\"version\":\"14.5.0\",\"service\":\"lia\",\"source\":\"lsw.qa\",\"node\":\"A65670F2\"},\"payload\":{\"actionId\":32250901,\"actionKey\":\"kudos.give-message-kudo\",\"user\":{\"type$model\":\"user\",\"type\":\"user\",\"uid\":7,\"registrationStatus\":\"fully-registered\",\"registration.time\":\"2014-05-13T20:56:04.715Z\",\"login\":\"testuserdemo1\",\"email\":\"megha.meshram@lithium.com\",\"rankings\":[{\"ranking\":{\"type$model\":\"ranking\",\"type\":\"ranking\",\"uid\":23,\"name\":\"New Member\"},\"node\":{\"type$model\":\"node\",\"type\":\"community\",\"uid\":1}}],\"roles\":[]}}}";

	public static final String SAMPLE_OUTPUT = "	{\"items\" : [{\"published\": \"2014-05-10T15:04:55Z\",\"foo\": \"some extension property\",\"generator\": {\"url\": \"http://lithium.org/activities-app\"},        \"provider\": {          \"url\": \"http://lithium.org/activity-stream\"},        \"title\": \"Vijay posted a new video to his album.\",        \"actor\": {          \"url\": \"http://lithium.org/roger\",         \"objectType\": \"person\",          \"id\": \"tag:lithium.org,2011:roger\",          \"foo2\": \"some other extension property\",          \"image\": {            \"url\": \"http://lithium.org/roger/image\",            \"width\": 250,            \"height\": 250          },         \"displayName\": \"roger Smith\"        },        \"verb\": \"post\",        \"object\" : {          \"url\": \"http://lithium.org/album/my_fluffy_cat.jpg\",          \"objectType\": \"photo\",          \"id\": \"tag:lithium.org,2011:look_the_great\",          \"image\": {            \"url\": \"http://lithium.org/album/my_fluffy_cat_thumb.jpg\",            \"width\": 250,            \"height\": 250          }        },        \"target\": {          \"url\": \"http://lithium.org/album/\",          \"objectType\": \"photo-album\",          \"id\": \"tag:lithium.org,2011:abc123\",          \"displayName\": \"roger's Photo Album\",          \"image\": {            \"url\": \"http://lithium.org/album/thumbnail.jpg\",            \"width\": 250,            \"height\": 250          }        }      } ]}";

	public abstract String transformToActivityStreams1(String rawEvent) throws JsonParseException, IOException;

	public abstract String transformToActivityStreams2(String rawEvent) throws JsonParseException, IOException;
}
