package de.dualuse.commons.util.concurrent;

import java.lang.reflect.Method;



public class ThreadLocalCopy<T extends Cloneable> extends ThreadLocal<T> {
	static Method cloneMethod;
	static {
		try {
			cloneMethod = Object.class.getDeclaredMethod("clone");
			cloneMethod.setAccessible(true);
		} catch (Exception ex) {
			throw new Error(ex);
		}
	}
	
	
	final public T prototype;
	
	public<Q extends T> ThreadLocalCopy(Q prototype) { this.prototype = prototype; }
	
	@SuppressWarnings("unchecked") 
	protected T initialValue() {
		if (prototype instanceof Replicable)
			return ((Replicable<T>)prototype). clone();
			
		try {
			return (T) cloneMethod.invoke(prototype);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	static public interface Replicable<T> extends Cloneable {
		public T clone();
	}
	
}