package de.dualuse.commons.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class CopyOnWriteArray<T> implements Collection<T> {

	public T[] elements;
	
	public CopyOnWriteArray(T... initialElements) {
		this.elements = initialElements.clone();
	}
	
	
	@Override
	public boolean add(T object) {
		T[] elements = this.elements;
		T[] newElements = Arrays.copyOf(elements, elements.length+1);
		
		newElements[elements.length] = object;
		this.elements = newElements;
		
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		T[] elements = this.elements;
		T[] newElements = Arrays.copyOf(elements, elements.length+collection.size());

		System.arraycopy(collection.toArray(), 0, newElements, elements.length, collection.size());
		this.elements = newElements;
		
		return true;
	}

	@Override
	public void clear() {
		elements = Arrays.copyOf(elements, 0);
	}

	@Override
	public boolean contains(Object object) {
		T[] elements = this.elements;
		for (int i=0;i<elements.length;i++)
			if (elements[i].equals(object))
				return true;
		
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		return Arrays.asList(elements).containsAll(collection);
	}

	@Override
	public boolean isEmpty() {
		return elements.length==0;
	}

	@Override
	public Iterator<T> iterator() {
		return Arrays.asList(elements).iterator();
	}

	@Override
	public boolean remove(Object object) {
		T[] elements = this.elements;
		for (int i=0;i<elements.length;i++)
			if (elements[i].equals(object)) {
				T[] newElements = Arrays.copyOf(elements, elements.length-1);
				System.arraycopy(elements, i+1, newElements, i, newElements.length-i);
				this.elements = newElements;
				return true;
			}
		
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		T[] elements = this.elements;
		ArrayList<T> temp = new ArrayList<T>(Arrays.asList(elements));
		temp.removeAll(collection);
		
		this.elements = temp.toArray(Arrays.copyOf(elements, temp.size())); 
		
		return elements.length>temp.size();		
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		T[] elements = this.elements;
		ArrayList<T> temp = new ArrayList<T>(Arrays.asList(elements));
		temp.retainAll(collection);
		
		this.elements = temp.toArray(Arrays.copyOf(elements, temp.size())); 
		
		return elements.length>temp.size();		
	}
	
	@Override
	public int size() {
		return elements.length;
	}
	
	@Override
	public Object[] toArray() {
		return elements.clone();
	}
	
	@Override
	public <S> S[] toArray(S[] array) {
		T[] elements = this.elements;
		if (array.length<elements.length)
			array = Arrays.copyOf(array, elements.length);
		
		System.arraycopy(elements, 0, array, 0, elements.length);
		return array;
	}
	
}



