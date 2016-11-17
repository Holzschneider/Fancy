package de.dualuse.commons.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.RandomAccess;

import de.dualuse.commons.Algorithms;

public class SortedArrayList<T> implements Collection<T>, RandomAccess {

	private ArrayList<T> w = new ArrayList<T>();

	private final Comparator<? super T> c;

	public SortedArrayList() {
		this(new Comparator<T>() {
			@SuppressWarnings("unchecked")
			public int compare(T o1, T o2) {
				return ((Comparable<T>) o1).compareTo(o2);
			};
		});
	}

	public SortedArrayList(Collection<T> fill) {
		this(new Comparator<T>() {
			@SuppressWarnings("unchecked")
			public int compare(T o1, T o2) {
				return ((Comparable<T>) o1).compareTo(o2);
			};
		});
		
		addAll(fill);
	}

	public SortedArrayList(Collection<T> source, Comparator<? super T> c) { this.c = c; addAll(source);}
	public SortedArrayList(Comparator<? super T> c) { this.c = c; }

	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean changed = w.addAll(c);
//		Collections.sort(w, this.c);
		Algorithms.quicksort(w, this.c);
		
		return changed;
	}

	@Override
	public void clear() {
		w.clear();
	}

	@Override
	public boolean contains(Object o) {
		return w.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return w.containsAll(c);
	}

	public T get(int index) {
		return w.get(index);
	}

	@Override
	public boolean isEmpty() {
		return w.isEmpty();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return w.removeAll(c);
	}

	@Override
	public Object[] toArray() {
		return w.toArray();
	}

	public <U extends Object> U[] toArray(U[] a) {
		return w.toArray(a);
	};

	@Override
	public boolean retainAll(Collection<?> c) {
		return w.retainAll(c);
	}

	@Override
	public String toString() {
		return w.toString();
	}

	@Override
	public int hashCode() {
		return w.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return w.equals(obj);
	}

	@Override
	public boolean remove(Object o) {
		// return w.remove(o);
		int index = indexOf(o);

		if (index == -1)
			return false;
		else
			w.remove(index);

		return true;
	}

	public T remove(int index) {
		return w.remove(index);
	}

	@SuppressWarnings("unchecked")
	public int indexOf(Object o) {

		int index = indexedBinarySearch(w, (T) o, c);
		if (index >= 0)
			return index;
		else
			return -1;

	}

	@Override
	public Iterator<T> iterator() {
		return w.iterator();
	}

	@Override
	public int size() {
		return w.size();
	}

	public boolean add(T e) {

		if (w.size() == 0)
			w.add(e);
		else {
			int index = indexedBinarySearch(w, e, c);
			if (index >= 0)
				w.add(index, e);
			else
				w.add(-(index + 1), e);
		}

		return true;
	};

	private static <T> int indexedBinarySearch(List<? extends T> l, T key, Comparator<? super T> c) {
		int low = 0;
		int high = l.size() - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			T midVal = l.get(mid);
			int cmp = c.compare(midVal, key);

			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else
				return mid; // key found
		}
		return -(low + 1); // key not found
	}

	public static void main(String[] args) {

		SortedArrayList<Integer> test = new SortedArrayList<Integer>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
		});

		Random r = new Random(0);

		for (int i = 0; i < 200; i++)
			test.add(r.nextInt(10));

		System.out.println(test);
	}

}
