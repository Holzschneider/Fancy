package de.dualuse.commons.util;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class ArrayMap<K,V> implements Map<K,V>, Serializable {
	private static final long serialVersionUID = 1L;
	
	public final ArraySet<Map.Entry<K, V>> entries = new ArraySet<Map.Entry<K,V>>();
	public final Set<K> keys = new Keys(); 
	public final Collection<V> values= new Values(); 

	public Set<Map.Entry<K, V>> entrySet() { return entries; }
	public Set<K> keySet() { return keys; }
	public Collection<V> values() { return values; }
	
	private final class Keys extends AbstractSet<K> {
		public Iterator<K> iterator() { return new Converter<K, Map.Entry<K,V>>() { public K convert(Map.Entry<K,V> e) { return e.getKey(); } }; };

		public int size() { return entries.size(); }
		public boolean isEmpty() { return entries.isEmpty(); }

		public boolean contains(Object o) { return ArrayMap.this.containsKey(o); }; 
		public boolean remove(Object o) { return ArrayMap.this.remove(o) != null; }
		public void clear() { ArrayMap.this.clear(); }

	};
	
	private final class Values extends AbstractCollection<V> {
		public Iterator<V> iterator() { return new Converter<V, Map.Entry<K,V>>() { public V convert(Map.Entry<K,V> e) { return e.getValue(); } }; };
        public int size() { return entries.size(); }
        public boolean isEmpty() { return entries.isEmpty(); }
        public boolean contains(Object o) { return ArrayMap.this.containsValue(o); }
        public void clear() { ArrayMap.this.clear(); }
    }
	

	public boolean containsKey(Object key) {
		for (int i=0;i<entries.size();i++) 
			if (entries.get(i).getKey().equals(key))
				return true;
		
		return false;
	}

	public boolean containsValue(Object value) {
		for (int i=0;i<entries.size();i++) 
			if (entries.get(i).getValue().equals(value))
				return true;
		
		return false;
	}


	public V get(Object key) {
		for (int i=0;i<entries.size();i++) 
			if (entries.get(i).getKey().equals(key))
				return entries.get(i).getValue(); 
						
		return null;
	}

	public V put(K key, V value) {
		V v = this.remove(key);
		entries.add(new AbstractMap.SimpleEntry<K,V>(key,value));
		return v;
	}

	public void putAll(Map<? extends K, ? extends V> map) {
		for (Map.Entry<? extends K, ? extends V> e: map.entrySet())
			put(e.getKey(),e.getValue());
	}

	public void clear() { entries.clear(); }
	public boolean isEmpty() { return entries.isEmpty(); }
	public int size() { return entries.size(); }

	public V remove(Object key) {
		for (int i=0;i<entries.size();i++) 
			if (entries.get(i).getKey().equals(key))
				return entries.remove(i).getValue(); 
						
		return null;
	}

	
}
