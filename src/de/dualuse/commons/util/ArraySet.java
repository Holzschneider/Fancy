package de.dualuse.commons.util;

import java.util.Collection;
import java.util.Set;

public class ArraySet<T> extends ArrayCollection<T> implements Set<T> {
	private static final long serialVersionUID = 1L;

	@Override
	public boolean add(T object) {
		if (!super.contains(object))
			return super.add(object);
		else
			return false;
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		boolean changed = false;
		for (T o: collection)
			changed = this.add(o) | changed;
			
		return changed;
	}
	
	
	
}
