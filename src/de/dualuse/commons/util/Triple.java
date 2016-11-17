package de.dualuse.commons.util;


public class Triple<X,Y,Z> {
	public final X a;
	public final Y b;
	public final Z c;

	public Triple(X a, Y b, Z c) {
		this.a=a;
		this.b=b;
		this.c=c;
	}
	
	static public<A,B,C> Triple<A,B,C> of(A a, B b, C c) {
		return new Triple<A,B,C>(a,b,c);
	}
	
	@Override
	public boolean equals(Object obj) {
		Triple<?,?,?> that = (Triple<?,?,?>)obj;
		return this.a.equals(that.a) && this.b.equals(that.b) && this.c.equals(that.c);
	}
	
	@Override
	public int hashCode() {
		return a.hashCode()^b.hashCode()^c.hashCode();
	}
}