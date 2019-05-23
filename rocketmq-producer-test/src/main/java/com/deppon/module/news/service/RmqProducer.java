/*
 * Copyright by Deppon and the original author or authors.
 * 
 * This document only allow internal use ,Any of your behaviors using the file
 * not internal will pay legal responsibility.
 *
 * You may learn more information about Deppon from
 *
 *      http://www.deppon.com
 *
 */
package com.deppon.module.news.service;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.deppon.dpboot.module.common.serializer.factory.impl.GenericsHessianFactory;
import com.deppon.dpboot.module.mq.client.producer.impl.GenericsConcurrentlyProducer;
import com.esotericsoftware.kryo.Serializer;

/**
 * <p style="display:none">
 * modifyRecord
 * </p>
 * <p style="display:none">
 * version:V1.0,author:345948,date:2019-05-11 上午10:03:47,content:TODO
 * </p>
 * @author 345948
 * @since
 * @version
 * @param <T>
 */
public class RmqProducer implements Serializable{

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 1L;
	public static Logger logger = LoggerFactory.getLogger(RmqProducer.class);
	private  String namesrvAddr;
	private  String producerGroup;
	private  String topic;
	private  String tag;
	protected GenericsConcurrentlyProducer<String> producer = new GenericsConcurrentlyProducer<String>();

	public void startup() {
		producer.setProducerGroup(producerGroup);
		producer.setNamesrvAddr(namesrvAddr);
		//序列化,跟deppon-mq-client默认的序列化方式保持一致
		producer.setSerializer(new GenericsHessianFactory<String>().getSerializer());
		// 设置重试次数
		producer.setRetryTimesWhenSendFailed(2);
		try {
			producer.startup();
		} catch (Throwable e) {
			logger.error("rmqProducer start fail,", e);
		}
	}

	public void shutdown() {
		producer.shutdown();
	}

	public Boolean send(String jsonStr) {
		return producer.send(topic, tag, UUID.randomUUID().toString(), jsonStr,null);
	}

	public String getNamesrvAddr() {
		return namesrvAddr;
	}
	public void setNamesrvAddr(String namesrvAddr) {
		this.namesrvAddr = namesrvAddr;
	}
	public String getProducerGroup() {
		return producerGroup;
	}
	public void setProducerGroup(String producerGroup) {
		this.producerGroup = producerGroup;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public RmqProducer() {
	}
}
