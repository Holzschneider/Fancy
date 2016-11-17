package de.dualuse.commons.util.concurrent;


import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;

public class Cascade implements Runnable, Condition {
	
	private Cascade[] cs = new Cascade[0];
	public Cascade() {}
	
	public Cascade(Cascade... parents) {
		this( parents.length==1?parents[0]:new Both(new Cascade(Arrays.copyOf(parents, parents.length-1)), parents[parents.length-1]));
	}
	
	public Cascade(Cascade parent) {
		parent.append(this);
	}
	
	private void append(Cascade target) {
		(cs = Arrays.copyOf(cs,cs.length+1))[cs.length-1] = target;
	}
	
	void runBy(Cascade parent) {
		run();
	}
	
	public void run() {
		synchronized(this) { notifyAll(); }
		
		for (Cascade c: cs)
			c.runBy(this);
	}
	
	
	////////////////////////////////////////////////////////////////////////////
	
	public static class Either extends Cascade {
		public Either(Cascade a, Cascade b) {
			a.append(this);
			b.append(this);
		}
	}
	
	public static class Both extends Either {
		final AtomicInteger i = new AtomicInteger(); 
		final Cascade a, b;
		public Both(Cascade a, Cascade b) {
			super(a,b);
			
			this.a=a;
			this.b=b;
		}
		
		@Override public void runBy(Cascade parent) {
			//only fire if both sources have been fired
			if (parent==a && i.getAndIncrement()<0) run();
			else if (parent==b && i.getAndDecrement()>0) run();
		}
	}
	
	public Cascade and(Cascade b) { return new Both(this, b); }
	public Cascade or(Cascade b) { return new Either(this, b); }
	
	
	// -[ Condition Method implementations ] -----------------------------------------------
	
	public void await() throws InterruptedException {
		wait();
	}
	
	@Override public void awaitUninterruptibly() {
		while(true) try {
			await();
			break;
		} catch (InterruptedException ex) {}
	}

	@Override public long awaitNanos(long nanosTimeout) throws InterruptedException {
		long start = System.nanoTime();
		wait((int)(nanosTimeout/1000000), (int)(nanosTimeout%1000000));
		return System.nanoTime()-start;
	}

	@Override public boolean await(long time, TimeUnit unit) throws InterruptedException {
		if (time<=0) return false;
		wait(unit.toMillis(time));
		return true;
	}

	@Override public boolean awaitUntil(Date deadline) throws InterruptedException {
		long millis = deadline.getTime()-System.currentTimeMillis();
		if (millis<=0) return false;
		wait(millis);
		return true;
	}

	@Override public void signalAll() {
		run();
	}

	@Override public void signal() {
		signalAll();
	}
}



