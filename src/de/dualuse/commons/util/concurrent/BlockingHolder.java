package de.dualuse.commons.util.concurrent;


import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BlockingHolder<V> implements Future<V> {
	private V value;
	public synchronized void put(V value) {
		this.value = value;
		this.notifyAll();
	}

	public void clear() {
		this.value = null;
	}
	
	@Override public boolean cancel(boolean mayInterruptIfRunning) { return false; }
	@Override public boolean isCancelled() { return false; }
	@Override public boolean isDone() { return value != null; }

	@Override public V get() throws InterruptedException {
		V copy = value;
		while (copy== null) synchronized (this) {
			this.wait();
			copy = value;
		}
		
		return copy;
	}

	public V get(long timeoutMs) throws InterruptedException, TimeoutException {
		return this.get(timeoutMs, TimeUnit.MILLISECONDS);
	}

	
	@Override public V get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
		V copy = value;
		while (copy== null) synchronized (this) {
			unit.timedWait(this, timeout);
			copy = value;
		}
		return copy;
	}

}
