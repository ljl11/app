package com.cqut.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonHelperImpl implements JsonHelper{
	ObjectMapper mapper;
	private static JsonHelperImpl jh;
	public synchronized static JsonHelperImpl getInstance() {
		if(jh==null) {
			jh = new JsonHelperImpl();
		}
		return jh;
	}
	private JsonHelperImpl(){
		mapper  = new ObjectMapper();
		mapper.getDeserializationConfig().set(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
	}

	public String getJson(Object o) {
		String result = null;
		try {
			result = mapper.writeValueAsString(o);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}

	public <E> E getBean(String json, Class<E> clazz) {
		try {
			E bean = mapper.readValue(json, clazz);
			return bean;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}


	public <E, V> Map<E, V> getBeanMap(String json, Class<E> key, Class<V> val) {
		try {
			Map<E, V> bean = mapper.readValue(json,

			TypeFactory.mapType(Map.class, key, val));
			return bean;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public  <E> List<E> getBeanList(String json, Class<E> ee) {
		try {
			List<E> bean = mapper.readValue(json,
			TypeFactory.collectionType(List.class, ee));
			return bean;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
