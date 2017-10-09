package com.cqut.json;

import java.util.List;
import java.util.Map;

public interface JsonHelper {
	String getJson(Object o);
	
	<E> E getBean(String json, Class<E> clazz);
	
	<E, V> Map<E, V> getBeanMap(String json, Class<E> key, Class<V> val);
	
	<E> List<E> getBeanList(String json, Class<E> ee) ;
}
