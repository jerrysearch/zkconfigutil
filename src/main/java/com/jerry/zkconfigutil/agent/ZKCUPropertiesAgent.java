package com.jerry.zkconfigutil.agent;

import java.lang.instrument.Instrumentation;

import org.apache.log4j.Logger;

import com.jerry.zkconfigutil.app.ZkConfigUtil;
import com.jerry.zkconfigutil.app.ZkConfigUtilFactory;

public class ZKCUPropertiesAgent {

	private static final Logger logger = Logger
			.getLogger(ZKCUPropertiesAgent.class);

	/**
	 * @param agentOps
	 *            -DZK=LOCALHOST:2181 -DCLASS=",,,,,"
	 * @param inst
	 * 
	 *	 java agent
	 */
	public static void premain(String agentOps, Instrumentation inst) {
		logger.info("premain into ...");

		String zkServer = System.getProperty("ZK", "").trim();
		String claz = System.getProperty("CLASS", "").trim();
		if (null == zkServer || zkServer.isEmpty()) {
			logger.warn("please input agentOps of zk and now do nothing");
			return;
		}
		if (null == claz || "".equals(claz)) {
			logger.warn("please input agentOps for class and now do nothing");
			return;
		}
		String[] clazs = claz.split(",");

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
