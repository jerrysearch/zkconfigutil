package com.jerry.zkconfigutil.resolve;

import org.apache.log4j.Logger;

/**
 * just for class<? extends Resolve>.newInstance()
 * provide noarg constructor
 * @author jerry
 *
 */
public abstract class AbstractResolve implements Resolve {

	public Logger logger = Logger.getLogger(getClass());
	public AbstractResolve(){}
}
