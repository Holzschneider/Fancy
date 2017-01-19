package de.dualuse.commons.util;
import java.io.*;
import java.util.*;

//XXX TODO make it work on Android!
@SuppressWarnings("unchecked")
public class Sticky<T extends Serializable> implements Serializable {
	static public interface Getter<Q extends Serializable> {
		public Q get();
	}

	static private class Passthrough<R extends Serializable> implements Getter<R> {
		Sticky<R> reference;
		
		public Passthrough(Sticky<R> reference) {
			this.reference = reference;
		}
		
		@Override
		public R get() {
			return reference.value;
		}
	}
	
	
	
	
	
	private static final long serialVersionUID = 1L;

	static String APP = new LinkedList<Class<?>>(Arrays.asList(new SecurityManager() { protected Class<?>[] getClassContext() { return super.getClassContext(); } }.getClassContext())).getLast().getName();
	
	static LinkedHashMap<String, List<Sticky<? extends Serializable>>> restored = new LinkedHashMap<String, List<Sticky<? extends Serializable>>>();
	static final LinkedHashMap<String, List<Sticky<? extends Serializable>>> registered = new LinkedHashMap<String, List<Sticky<? extends Serializable>>>();
	
	static final String COOL_FILE_NAME = APP+".sticky.blob";
	public static final File persistentStore = new File(System.getProperty("java.io.tmpdir"),COOL_FILE_NAME);
	
	static final Thread saver = new Thread("Sticky-Thread") {
		public void run() {
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(persistentStore));
				
				for (Collection<Sticky<?>> cs: registered.values())
					for (Sticky<?> s: cs)
						s.update();

				oos.writeObject(registered);
				oos.close();
			} catch (Exception ex) {
				System.err.println("Error: couldn't write "+COOL_FILE_NAME+" to "+persistentStore+": "+ex);
			}
		}
	};
	
	static {
		if (persistentStore.exists())
			try {
				ObjectInputStream oos = new ObjectInputStream(new FileInputStream(persistentStore));
				
				restored = (LinkedHashMap<String, List<Sticky<? extends Serializable>>>) oos.readObject();
				oos.close();
			} catch (Exception ex) {
				System.err.println("Error: couldn't read "+COOL_FILE_NAME+" from "+persistentStore+": "+ex);
			}
			Runtime.getRuntime().addShutdownHook(saver); 
	}
	

	public Sticky(Getter<T> ret) { this(ret.get(), ret); }
	public Sticky(T defaultValue) { this(identifierForStackTrace(), defaultValue); }
	public Sticky(T defaultValue, Getter<T> ret) { this(defaultValue); this.ret = ret; }
	public Sticky(String identifier, Getter<T> ret) { this(identifier, ret.get(), ret);  }
	public Sticky(String identifier, T defaultValue, Getter<T> ret) { this(identifier, defaultValue); this.ret = ret; }	
	public Sticky(String identifier, T defaultValue) {
		List<Sticky<? extends Serializable>> dupes = registered.get(identifier);
		if (dupes == null)
			dupes = new LinkedList<Sticky<? extends Serializable>>();
		
		dupes.add(this);
		registered.put(identifier,dupes);

		try {
			value = restore(identifier, value = initial = defaultValue);
		} catch (Exception ex) { } 
	}

	T restore(String identifier, T defaultValue) throws Exception {
		Class<T> type = (Class<T>)defaultValue.getClass();
		value = defaultValue;
		
		List<Sticky<? extends Serializable>> sources = restored.get(identifier);
		if (sources!=null) 
			value = type.cast(sources.get(registered.get(identifier).size()-1).value);
		
		return value; 
	}

	private T value = null;
	private T initial = null;
	private transient Getter<T> ret = new Passthrough<T>(this);

	public Sticky<T> update() { value = ret.get(); return this; }
	public Sticky<T> reset() { value = initial; return this; }
	public Sticky<T> set(T value) { this.value = value; return this; }
	public T get() { return this.value; }
	
	public static<T extends Serializable> T value(Getter<T> v) { return new Sticky<T>(v).get(); }
	public static<T extends Serializable> T value(T v) { return new Sticky<T>(v).get(); }
	public static<T extends Serializable> T value(String identifier, T v) { return new Sticky<T>(identifier, v).get(); }
	public static<T extends Serializable> T value(String identifier, Getter<T> v) { return new Sticky<T>(identifier, v).get(); }
	
	static public String identifierForStackTrace() {
		String identifier = "";
		for (StackTraceElement ste: new Exception().getStackTrace())
			identifier += ste.getClassName()+".";
		
		return identifier;
	}

}
