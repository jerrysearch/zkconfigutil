package com.jerry.zkconfigutil.resolve;

/**
 * field assignment Interface
 * @author jerry
 *
 */
public interface Resolve {
	String resolve();
	void dResolve(String src);
}
