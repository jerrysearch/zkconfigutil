package com.jerry.zkconfigutil.app;

import java.util.HashMap;
import java.util.Map;

public final class ZkConfigUtilFactory {

	private static final Map<String, ZkConfigUtil> cache = new HashMap<String, ZkConfigUtil>();

	public static synchronized ZkConfigUtil getZkConfigUtil(String servers) {
		if (cache.containsKey(servers)) {
			return cache.get(servers);
		} else {
			ZkConfigUtil zkConfigUtil = new ZkConfigUtil(servers);
			cache.put(servers, zkConfigUtil);
			return zkConfigUtil;
		}
	}
}
