package com.jerry.zkconfigutil.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * annotation target class
 * @author jerry
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface TypeZkConfigurable {
	/**
	 * base path on zookeeper
	 * @return
	 */
	@Deprecated
	String path() default "";
	/**
	 * if use this zookeeper server default false
	 * @return
	 */
	boolean useOwnZkServer() default false;
	/**
	 * own zookeeper server default ""
	 * @return
	 */
	String server() default "";
}