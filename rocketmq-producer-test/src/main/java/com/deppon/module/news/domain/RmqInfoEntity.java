package com.deppon.module.news.domain;

/**
 * 通用响应entity
 * 
 * @Title: .java
 * @Package com.deppon.module.news.domain
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 345948
 * @date 2018-3-15
 * @version V1.0
 */
public class RmqInfoEntity {
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;

	private int times = 0;

	/**
	 * 响应信息 msg:success/fail
	 */
	private String msg;
	
	private String body;
	
	private String rspTime;

	private String namesrv;
	
	private String topic;
	
	private String cgroup;
	
	private String pgroup;
	
	private String tag;
	
	private String consumeWhere;
	
	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getRspTime() {
		return rspTime;
	}

	public void setRspTime(String rspTime) {
		this.rspTime = rspTime;
	}

	
	public String getNamesrv() {
		return namesrv;
	}

	public void setNamesrv(String namesrv) {
		this.namesrv = namesrv;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getCgroup() {
		return cgroup;
	}

	public void setCgroup(String cgroup) {
		this.cgroup = cgroup;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getPgroup() {
		return pgroup;
	}

	public void setPgroup(String pgroup) {
		this.pgroup = pgroup;
	}
	
	public String getConsumeWhere() {
		return consumeWhere;
	}

	public void setConsumeWhere(String consumeWhere) {
		this.consumeWhere = consumeWhere;
	}

	@Override
	public String toString() {
		return "RmqInfoEntity [times=" + times + ", msg=" + msg + ", body="
				+ body + ", rspTime=" + rspTime + ", namesrv=" + namesrv
				+ ", topic=" + topic + ", cgroup=" + cgroup + ", pgroup="
				+ pgroup + ", tag=" + tag + ", consumeWhere=" + consumeWhere + "]";
	}

	
}
