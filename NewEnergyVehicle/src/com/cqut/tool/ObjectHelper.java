package com.cqut.tool;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;


public class ObjectHelper{
	public ObjectHelper(){
	}

	public static boolean isEmpty(Object obj){
		if (obj == null)
			return true;
		if (obj instanceof Collection<?>)
			return ((Collection<?>)obj).isEmpty();
		if (obj instanceof String)
			return ((String)obj).equalsIgnoreCase("null") || ("".equals(((String)obj).trim()));
		if (obj instanceof StringBuffer)
			return ((StringBuffer)obj).length() == 0;
		if (obj.getClass().isArray()){
			boolean b = true;
			Object a[] = (Object[])obj;
			int len = a.length;
			int i = 0;
			do{
				if (i >= len)
					break;
				Object o = a[i];
				b = isEmpty(o);
				if (b)
					break;
				i++;
			} while (true);
			return b;
		}
		return false;
	}

	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	public static String[] getFiledName(Object o) {
		String fieldNames[];
		Field fields[] = o.getClass().getDeclaredFields();
		fieldNames = new String[fields.length];
		for (int i = 0; i < fields.length; i++){
			fieldNames[i] = fields[i].getName();
		}
			
		return fieldNames;
	}

	public static boolean isFiledName(Object o, String exp) {
		if (o == null)
			return false;
		String fildeName[] = getFiledName(o);
		for (int i = 0; i < fildeName.length; i++){
			if (fildeName[i].equals(exp)){
				return true;
			}
		}
			
		return false;
	}

	public static String getIdentityHexString(Object obj) {
		return Integer.toHexString(System.identityHashCode(obj));
	}

	public static int getObjectSize(Object value) {
		if (value == null)
			return 0;
		if (value instanceof Collection<?>)
			return ((Collection<?>)value).size();
		if (value instanceof Map<?,?>)
			return ((Map<?,?>)value).size();
		else
			return 1;
	}
}
