package de.dualuse.commons.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

public class ArrayCollection<E> extends ArrayList<E> {
	private static final long serialVersionUID = -6679886491630033551L;

	public ArrayCollection() {
		super();
	}

	public ArrayCollection(E... c) {
		super(c.length);
		for (int i=0,l=c.length;i<l;i++)
			this.add(c[i]);
	}

	public ArrayCollection(E c) {
		super(1);
		this.add(c);
	}

	public ArrayCollection(Collection<? extends E> c) {
		super(c);
	}

	public ArrayCollection(ArrayCollection<? extends E> ac) {
		super(ac.size());
		this.addAll(ac);
	}

	public ArrayCollection(int initialCapacity) {
		super(initialCapacity);
	}

	public boolean addAll(ArrayCollection<? extends E> ac) {
		return this.addAll(0, ac);
	}

	public boolean addAll(int index, ArrayCollection<? extends E> ac) {
		for (int i = 0, l = ac.size(); i < l; i++)
			this.add(index++, ac.get(i));

		return true;
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		for (E e : c)
			add(index++, e);

		return true;
	}

	public void add(int index, E element) {
		if (index < size()) {
			E temp = super.get(index);
			modCount++;
			super.set(index, element);
			super.add(temp);
		} else {
			super.add(index,element);
		}
	}

	public E remove(int index) {
		int lastIndex = super.size() - 1;
		E temp = super.get(index);
		modCount++;
		super.set(index, super.get(lastIndex));
		super.remove(lastIndex);
		return temp;
	}

	public boolean remove(Object o) {
		int elementIndex = super.indexOf(o);
		if (elementIndex == -1)
			return false;
		modCount++;
		this.remove(elementIndex);
		return true;
	}

	public boolean removeAll(Collection<?> c) {
		boolean changed = false;
		int l = this.size(), m = l;
		for (int i = 0; i < l; i++) {
			E element = super.get(i);
			if (!c.contains(element))
				continue;
			modCount++;
			super.set(i, this.get(--l));
			changed = true;
		}

		while (l < m)
			super.remove(--m);

		return changed;
	}

	public boolean retainAll(Collection<?> c) {
		boolean changed = false;

		int l = this.size(), m = l;
		for (int i = 0; i < l; i++) {
			E element = super.get(i);
			if (c.contains(element))
				continue;
			modCount++;
			super.set(i--, this.get(--l));
			changed = true;
		}

		while (l < m)
			super.remove(--m);

		return changed;
	}

	public Iterator<E> iterator() {
		return new Iterator<E>() {
			int modCount = ArrayCollection.super.modCount;
			int i = 0;
			boolean itemRemoved = false;

			public boolean hasNext() {
				return i < ArrayCollection.super.size();
			}

			public E next() {
				itemRemoved = false;
				if (ArrayCollection.super.modCount != modCount)
					throw new ConcurrentModificationException();
				return ArrayCollection.super.get(i++);
			}

			public void remove() {
				if (ArrayCollection.super.modCount != modCount)
					throw new ConcurrentModificationException();
				// commit removal using ArrayCollection's remove function
				// lastElement -> i, size() decreased
				if (itemRemoved)
					throw new IllegalStateException();

				itemRemoved = true;

				ArrayCollection.this.remove(--i);
				// accept modified version as unmodified state
				modCount = ArrayCollection.super.modCount;
			}
		};
	}

	public static void main(String args[]) {
		// List<Integer> ac = new ArrayCollection<Integer>();
		List<Integer> ac = new ArrayList<Integer>();

		for (int i = 0; i < 10; i++)
			ac.add(i);

		Iterator<Integer> it = ac.iterator();
		it.next();
		it.next();
		it.remove();
		it.remove();

		System.out.println(ac);

		/*
		 * 
		 * ArrayList<Integer> victims = new ArrayList<Integer>();
		 * victims.add(3); victims.add(5);
		 * 
		 * ac.retainAll( victims ); ac.remove((Integer)5);
		 * 
		 * System.out.println(ac);
		 */

	}
}
