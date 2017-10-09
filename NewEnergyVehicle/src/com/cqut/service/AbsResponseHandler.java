package com.cqut.service;

import com.cqut.tool.Parameter;


public abstract class AbsResponseHandler implements Parameter {
	private Class<?> resultClass;
	private JsonType typeClass;

	public AbsResponseHandler(Class<?> resultClass, JsonType typeClass){
		this.resultClass = resultClass;
		this.typeClass = typeClass;
	}
	
	public Class<?> getResultClass() {
		return resultClass;
	}

	public JsonType getTypeClass() {
		return typeClass;
	}

	abstract public void onSuccess(Object o);
	
	abstract public void onFailure(Throwable error);
	
}
