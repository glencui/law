package com.jfzy.service.bo;

public enum PropertyTypeEnum {

	ROLE(1), SIGN(2);

	private int id;

	private PropertyTypeEnum(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
