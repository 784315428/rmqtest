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
package com.deppon.module.news.service.sync;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.deppon.module.news.domain.RmqInfoEntity;

/**
 * TODO(描述类的职责)
 * <p style="display:none">
 * modifyRecord
 * </p>
 * <p style="display:none">
 * version:V1.0,author:345948,date:2018-3-30 上午9:28:59,content:TODO
 * </p>
 * 
 * @author 345948
 * @date 2018-3-30 上午9:28:59
 * @since
 * @version
 */
@RequestMapping("/")
public interface IRmqPressure {

	/**
	 * 获取参数,启动,并进行发送
	 * @param re
	 * @return
	 */
	@RequestMapping(value = "rmqProducerBody", method = RequestMethod.GET,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public RmqInfoEntity rmqProducerBody(RmqInfoEntity re);
	
	/**
	 * 获取参数,启动,并监听
	 * @param re
	 * @return
	 */
	@RequestMapping(value = "rmqConsumerBody", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public RmqInfoEntity rmqConsumerBody(RmqInfoEntity re);
	
	@RequestMapping(value = "rmqProducerParam", method = RequestMethod.GET)
	public RmqInfoEntity rmqProducerParam(
					@RequestParam String namesrv,
					@RequestParam String topic,
					@RequestParam String cgroup,
					@RequestParam int times,
					@RequestParam String body,
					@RequestParam String consumeWhere
			);
	
	@RequestMapping(value = "rmqConsumerParam", method = RequestMethod.GET)
	public RmqInfoEntity rmqConsumerParam(
					@RequestParam String namesrv,
					@RequestParam String topic,
					@RequestParam String cgroup,
					@RequestParam int times,
					@RequestParam String body,
					@RequestParam String consumeWhere
			);
	
	@RequestMapping(value = "getRmqHandleTime", method = RequestMethod.GET)
	public String getRmqHandleTime(
					@RequestParam String key,
					@RequestParam String producerTime,
					@RequestParam String consumerTime,
					@RequestParam boolean onOrMore
			);

}
