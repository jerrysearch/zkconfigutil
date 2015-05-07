package com.jerry.zkconfigutil.util;

import java.util.HashMap;
import java.util.Map;

import com.jerry.zkconfigutil.resolve.Resolve;

public final class Updater {
	
	
	private static final Map<String, Resolve> map = new HashMap<String, Resolve>();
	
	
	public static void register(String fieldPath, Resolve resolve){
		map.put(fieldPath, resolve);
	}
	
	public static void update(String fieldPath, String value){
		Resolve resolve = map.get(fieldPath);
		
		resolve.dResolve(value);
	}
}
