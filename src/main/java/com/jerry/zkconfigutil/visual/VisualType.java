package com.jerry.zkconfigutil.visual;

public abstract class VisualType {
	public abstract VisualType valueOf(String src);
	
	@Override
	public abstract String toString();
}
