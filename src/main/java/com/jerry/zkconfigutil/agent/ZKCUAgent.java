package com.jerry.zkconfigutil.agent;

import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jerry.zkconfigutil.app.ZkConfigUtil;
import com.jerry.zkconfigutil.app.ZkConfigUtilFactory;

public class ZKCUAgent {

	private static final Logger logger = Logger.getLogger(ZKCUAgent.class);

	/**
	 * @param agentOps
	 *            zk@s1,s2#class@c1,c2,c3
	 * @param inst
	 * 
	 * java agent
	 */
	public static void premain(String agentOps, Instrumentation inst) {
		logger.info("premain into ...");
		logger.info(agentOps);
		agentOps = agentOps.replaceAll("\\s", "");
		String[] args = agentOps.split("#");
		Map<String, String> map = new HashMap<String, String>();
		for (String s : args) {
			String[] as = s.split("@");
			if (null != as && as.length >= 2)
				map.put(as[0], as[1]);
		}
		String zkServer = map.get("zk");
		if (null == zkServer || zkServer.isEmpty()) {
			logger.warn("please input agentOps of zk and now do nothing");
			return;
		}
		String claz = map.get("class");
		String[] clazs = claz.split(",");
		if (null == clazs || clazs.length == 0) {
			logger.warn("please input agentOps for class and now do nothing");
			return;
		}
		ZkConfigUtil zkConfigUtil = ZkConfigUtilFactory
				.getZkConfigUtil(zkServer);
		for (String s : clazs) {
			try {
				Class<?> cla = Class.forName(s);
				zkConfigUtil.register(cla, true);
			} catch (ClassNotFoundException e) {
				logger.error("can't find calss : " + s, e);
			} catch (Exception e) {
				logger.error("zkConfigUtil register exception", e);
			}
		}
		logger.info("premain end ...");
	}

}
