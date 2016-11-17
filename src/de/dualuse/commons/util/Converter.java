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
}
