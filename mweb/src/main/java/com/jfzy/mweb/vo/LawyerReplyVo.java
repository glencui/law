package com.jfzy.mweb.vo;

public class LawyerReplyVo {
	private int id;
	private int orderId;
	private int lawyerId;
	private String createTime;
	private String updateTime;
	private String productCode;
	private String simpleReply;
	private String replySummary;
	private String replySuggests;
	private String replyRules;
	private String hasHukou;
	private int status;
	private double score;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getLawyerId() {
		return lawyerId;
	}

	public void setLawyerId(int lawyerId) {
		this.lawyerId = lawyerId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getSimpleReply() {
		return simpleReply;
	}

	public void setSimpleReply(String simpleReply) {
		this.simpleReply = simpleReply;
	}

	public String getReplySummary() {
		return replySummary;
	}

	public void setReplySummary(String replySummary) {
		this.replySummary = replySummary;
	}

	public String getReplySuggests() {
		return replySuggests;
	}

	public void setReplySuggests(String replySuggests) {
		this.replySuggests = replySuggests;
	}

	public String getReplyRules() {
		return replyRules;
	}

	public void setReplyRules(String replyRules) {
		this.replyRules = replyRules;
	}

	public String getHasHukou() {
		return hasHukou;
	}

	public void setHasHukou(String hasHukou) {
		this.hasHukou = hasHukou;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

}
