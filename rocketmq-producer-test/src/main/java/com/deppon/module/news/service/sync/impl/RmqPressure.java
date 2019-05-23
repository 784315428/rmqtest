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
package com.deppon.module.news.service.sync.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.deppon.dpboot.module.common.serializer.factory.impl.GenericsHessianFactory;
import com.deppon.dpboot.module.mq.client.consumer.GenericsMessageConsumer;
import com.deppon.dpboot.module.mq.client.consumer.impl.GenericsConcurrentlyConsumer;
import com.deppon.dpboot.module.mq.client.producer.impl.GenericsConcurrentlyProducer;
import com.deppon.module.news.domain.RmqInfoEntity;
import com.deppon.module.news.service.RmqConsumer;
import com.deppon.module.news.service.RmqProducer;
import com.deppon.module.news.service.sync.IRmqPressure;

/**
 * TODO(描述类的职责)
 * <p style="display:none">modifyRecord</p>
 * <p style="display:none">version:V1.0,author:345948,date:2019-3-30 上午9:32:44,content:TODO </p>
 * @author 345948
 * @date 2019-3-30 上午9:32:44
 */
@Controller
public class RmqPressure implements IRmqPressure,GenericsMessageConsumer<String> {

	//日志
	private static Logger logger = LoggerFactory.getLogger(RmqPressure.class);
    private static long lastHandelTime=0l;
    private static long currentTimeP =0l;
    private static long currentTimeC =0l;


	//启动参数
	private static String namesrvAddr,producerGroup,consumerGroup,topic,tag,consumeWhere;
	//如果不传默认测营销域两台生产机器
	private static final String topicStr = "RMQ_YINGXIAO_TOPIC",cgroupStr = "RMQ_PASS_CGROUP",pgroupStr = "RMQ_PASS_PGROUP",
								tagStr = "RMQ_PASS_TAG",namesrvStr = "10.248.132.21:9876;10.248.132.22:9876";



	/**
	 * url方式请求
	 */
	public @ResponseBody RmqInfoEntity rmqProducerParam(String namesrv,
			String topic, String cgroup, int times, String body,String consumeWhere) {
		RmqInfoEntity re =initEntity(namesrv,topic,cgroup,times,body,consumeWhere);
		return goProducer(re);
	}
	/**
	 * body方式请求
	 */
	public @ResponseBody RmqInfoEntity rmqConsumerParam(String namesrv,
			String topic, String cgroup, int times, String body,String consumeWhere) {
		return goConsumer(initEntity(namesrv,topic,cgroup,times,body,consumeWhere));
	}
	/**
	 *触发发送
	 */
	@Override
	public @ResponseBody RmqInfoEntity rmqProducerBody(@RequestBody RmqInfoEntity re) {
		return goProducer(re);
	}

	/**
	 * 触发消费
	 */
	public @ResponseBody RmqInfoEntity rmqConsumerBody(@RequestBody RmqInfoEntity re) {
		return goConsumer(re);
	}

	/**
	 * 获取上次执行的时间
	 */
	public @ResponseBody String getRmqHandleTime(String key,String producerTime,
			String consumerTime, boolean onOrMore) {
		return "当前执行耗时: "+lastHandelTime+" ms";
	}

	//消费方法
	public void onConsume(String arg0) {
		logger.info("接收数据开始 ...打印message:" + arg0);
		//记录消费执行时间
		lastHandelTime = System.currentTimeMillis()-currentTimeC;
	}

	/**
	 * 根据参数启动consumer
	 */
	public GenericsConcurrentlyConsumer<String> initConsumerStartParam(RmqInfoEntity re){
		GenericsConcurrentlyConsumer<String> consumer = new GenericsConcurrentlyConsumer<String>();
		//初始化rmq参数
		initRmqParam(re);
		consumer.setNamesrvAddr(namesrvAddr);
		consumer.setConsumerGroup(consumerGroup);
		if(StringUtils.isEmpty(consumeWhere)){
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
		}else if("CONSUME_FROM_FIRST_OFFSET".equals(consumeWhere)){
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		}else if("CONSUME_FROM_TIMESTAMP".equals(consumeWhere)){
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_TIMESTAMP);
		}else{
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
		}

		consumer.setTopic(topic);
		consumer.setTag(tag);
		//producer.setVipChannelEnabled(true);
		consumer.setSerializer(new GenericsHessianFactory<String>().getSerializer());
		consumer.setMessageConsumer(this);

		try {
			consumer.startup();
			currentTimeC = System.currentTimeMillis();//启动成功开始计时
			logger.info("rmqConsumer启动成功参数信息:"+"InstanceName"+"/"+consumer.getInstanceName()+re.toString());
		} catch (Throwable e) {
			logger.error("rmqConsumer start fail,", e);
		}
		return consumer;
	}

	/**
	 * 根据参数启动producer
	 * @param re
	 */
	public GenericsConcurrentlyProducer<String> initProducerStartParam(RmqInfoEntity re){

		GenericsConcurrentlyProducer<String> producer = new GenericsConcurrentlyProducer<String>();
		//getSingletonProducer();
		//初始化rmq参数
		initRmqParam(re);
		producer.setProducerGroup(producerGroup);
		producer.setNamesrvAddr(namesrvAddr);
		producer.setSerializer(new GenericsHessianFactory<String>().getSerializer());
		producer.setRetryTimesWhenSendFailed(1);
		//producer.setVipChannelEnabled(true);
		try {
			producer.startup();
			currentTimeP = System.currentTimeMillis();//启动成功开始计时
			logger.info("rmqProducer启动参数信息:producer--"+producer.getInstanceName()+"/"+re.toString());
		} catch (Throwable e) {
			logger.error("rmqProducer start fail,", e);
		}
		return producer;
	}

	/**
	 * 发送方法
	 * @param jsonStr
	 * @return
	 */
	public Boolean toProducer(String jsonStr,GenericsConcurrentlyProducer<String> producer) {
		return producer.send(topic, tag, UUID.randomUUID().toString(), jsonStr, null);
	}

	/**
	 * 初始化rmq参数
	 * @param re
	 */
	public void initRmqParam(RmqInfoEntity re){
		this.topic=StringUtils.isEmpty(re.getTopic())?topicStr:re.getTopic();
        this.namesrvAddr=StringUtils.isEmpty(re.getNamesrv())?namesrvStr:re.getNamesrv();
        this.producerGroup=StringUtils.isEmpty(re.getPgroup())?pgroupStr:re.getPgroup();
        this.consumerGroup=StringUtils.isEmpty(re.getCgroup())?cgroupStr:re.getCgroup();
        this.tag=StringUtils.isEmpty(re.getTag())?tagStr:re.getTag();
        this.consumeWhere=StringUtils.isEmpty(re.getConsumeWhere())?"CONSUME_FROM_LAST_OFFSET":re.getConsumeWhere();
	}

	//goConsumer
	public RmqInfoEntity goConsumer(RmqInfoEntity re){
		try {
			//if(null==consumer){
				//根据参数启动rmq,每次请求启动一个实例
			GenericsConcurrentlyConsumer<String> consumer = initConsumerStartParam(re);
			//}
			re.setMsg("rmqConsumer启动成功--"+consumer.getInstanceName()+"正在消费中...");
		} catch (Exception e) {
			re.setMsg("请求失败");
		}finally{
			//单个实例启动，不做关闭
		}
        return re;
	}
	//goProducer
	public RmqInfoEntity goProducer(RmqInfoEntity re){
		try {
			//初始化参数
			int count = 0;
			int times = re.getTimes();
			//if (null == producer) {
			GenericsConcurrentlyProducer<String> producer=initProducerStartParam(re);//根据参数启动rmq
			//}
			//当前时间
			long currentTime = System.currentTimeMillis();
			//转json
			String jsonStr = JSONObject.toJSONString(re);
			if (StringUtils.isEmpty(re.getBody())) {
				re.setMsg("body设置为默认值");
				re.setBody("{'body':'body设置为默认值','times':10,'topic':'RMQ_YINGXIAO_TOPIC','namesrv':'10.230.50.158:9876;10.230.50.162:9876;10.230.50.180:9876'}");
			}
			for (int i = 0; i < times; i++) {
				if (toProducer(jsonStr,producer)) {
					count++;
					logger.info("rmqProducer推送:"+jsonStr);
					//记录消费执行时间
					lastHandelTime = System.currentTimeMillis() - currentTime;
				}
			}
			re.setRspTime("耗时(ms):" + lastHandelTime);
			re.setMsg("成功次数:" + count);
			logger.info(re.toString());
		} catch (Exception e) {
			re.setMsg("请求失败");
		}finally{
		}
		return re;
	}

	public static RmqInfoEntity initEntity(String namesrv,String topic, String cgroup, int times, String body,String consumerWhere){
		RmqInfoEntity re = new RmqInfoEntity();
		re.setNamesrv(namesrv);
		re.setTopic(topic);
		re.setCgroup(cgroup);
		re.setTimes(times);
		re.setBody(body);
		re.setConsumeWhere(consumerWhere);
		return re;
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
	public String getConsumerGroup() {
		return consumerGroup;
	}
	public void setConsumerGroup(String consumerGroup) {
		this.consumerGroup = consumerGroup;
	}

}
