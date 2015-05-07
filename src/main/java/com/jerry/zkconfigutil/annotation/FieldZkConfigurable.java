package com.jerry.zkconfigutil.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jerry.zkconfigutil.resolve.AbstractResolve;
import com.jerry.zkconfigutil.resolve.ReflectResolve;
/**
 * annotation target field
 * @author jerry
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldZkConfigurable {
	/**
	 * path on zookeeper default ""
	 * @return
	 */
	String path() default "";

	/**
	 * is need dynamic update by zookeeper default false
	 * @return
	 */
	boolean dynamicUpdate() default false;
	
	Class<? extends AbstractResolve> resolve() default ReflectResolve.class;
	
	/**
	 * useless
	 * @return
	 * 
	 * @deprecated
	 */
	@Deprecated()
	String defaultValue() default "";
}