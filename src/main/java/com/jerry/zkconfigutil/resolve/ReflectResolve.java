package com.jerry.zkconfigutil.resolve;

import java.lang.reflect.Field;

import com.jerry.zkconfigutil.visual.VisualType;

public final class ReflectResolve extends AbstractResolve {

	private final Class<?> cla;
	private final Field field;

	public ReflectResolve(Class<?> cla, Field field) {
		this.cla = cla;
		this.field = field;
	}

	@Override
	public String resolve() {
		try {
			return field.get(cla).toString();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public void dResolve(String src) {
		Object value = null;
		Class<?> type = field.getType();
		if (type == String.class) {
			value = src;
		} else if (type == Boolean.class || type == boolean.class) {
			value = Boolean.valueOf(src);
		} else if (type == Integer.class || type == int.class) {
			value = Integer.valueOf(src);
		} else if (type == Long.class || type == long.class) {
			value = Long.valueOf(src);
		} else if (type == Double.class || type == double.class) {
			value = Double.valueOf(src);
		} else if (type == Float.class || type == float.class) {
			value = Float.valueOf(src);
		} else if (type == Short.class || type == short.class) {
			value = Short.valueOf(src);
		} else if (VisualType.class.isAssignableFrom(type)) {
			try {
				value = ((VisualType) field.get(cla)).valueOf(src);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else {
			logger.warn("dosent have this type and return : "
					+ type.getSimpleName());
			return;
		}
		try {
			field.setAccessible(true);
			field.set(cla, value);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
