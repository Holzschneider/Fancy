package de.dualuse.commons.util;

import java.util.Iterator;

public abstract class Converter<Q,E> implements Iterator<Q> {
	final Iterator<E> inner;
	public Converter(Iterator<E> wrapped) { this.inner = wrapped; }
	public Converter() { this.inner = null; }

	public boolean hasNext() { return inner.hasNext(); }
	public Q next() { return convert(inner.next()); }
	public void remove() { inner.remove(); }
	
	abstract public Q convert(E e);
	
	
	
	static public<A,B>  Converter<A,B> on(Iterator<B> i, final Conversion<A,B> c) {
		return new Converter<A, B>(i) {
			public A convert(B b) {
				return c.convert(b);
			}
		};
	}
	
	public static interface Conversion<Q,E> {
		public Q convert(E e);
	}
}
