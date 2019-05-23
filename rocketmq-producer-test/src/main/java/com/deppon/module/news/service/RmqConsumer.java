package com.deppon.module.news.service;


import org.apache.log4j.Logger;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.deppon.dpboot.module.mq.client.consumer.GenericsMessageConsumer;
import com.deppon.dpboot.module.mq.client.consumer.impl.GenericsConcurrentlyConsumer;


/**
 * @title: 监听类
 * @description: (这里用一句话描述这个方法的作用)
 * @param: 入参列表
 * @return: ${return_type} 返回类型
 * @author ryan
 * @date: ${date}
 * @version:1.0
 */
public class RmqConsumer implements GenericsMessageConsumer<String> {

	private static final Logger LOGGER = Logger.getLogger(RmqConsumer.class);

	protected GenericsConcurrentlyConsumer<String> consumer = new GenericsConcurrentlyConsumer<String>();
	private String namesrvAddr;
	private String consumerGroup;
	private String topic;
	private String tag;

	public void startup() {
		consumer.setNamesrvAddr(namesrvAddr);
		consumer.setConsumerGroup(consumerGroup);
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
		consumer.setTopic(topic);
		consumer.setTag(tag);
		consumer.setMessageConsumer(this);
		try {
			consumer.startup();
			LOGGER.info("rmq启动成功");
		} catch (Throwable e) {
			LOGGER.error("RmqListenter start fail,", e);
		}
	}

	@Override
	public void onConsume(String arg0) {
		// TODO Auto-generated method stub
		LOGGER.info("接收数据开始 ...打印message:" + arg0);

	}


	public String getNamesrvAddr() {
		return namesrvAddr;
	}

	public void setNamesrvAddr(String namesrvAddr) {
		this.namesrvAddr = namesrvAddr;
	}

	public String getConsumerGroup() {
		return consumerGroup;
	}

	public void setConsumerGroup(String consumerGroup) {
		this.consumerGroup = consumerGroup;

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

	public RmqConsumer() {
	}

	public void shutdown() {
		consumer.shutdown();
	}
}
