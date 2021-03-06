package de.dualuse.commons.util;

import java.io.Serializable;


/**
 * Android compatible Pair implementation ; ) nicht geklaut!
 */
public class Pair<F, S> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public final F first;
    public final S second;

    /**
     * Constructor for a Pair.
     *
     * @param first the first object in the Pair
     * @param second the second object in the pair
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }
    
    public static boolean equal(Object a, Object b) {
    	return a == b || (a != null && a.equals(b));
    }

    public static int hashCode(Object o) {
    	return (o == null) ? 0 : o.hashCode();
    }
    
    /**
     * Checks the two objects for equality by delegating to their respective
     * {@link Object#equals(Object)} methods.
     *
     * @param o the {@link Pair} to which this one is to be checked for equality
     * @return true if the underlying objects of the Pair are both considered
     *         equal
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair))
            return false;
        Pair<?, ?> p = (Pair<?, ?>) o;
        
        return equal(p.first, first) && equal(p.second, second);
    }

    /**
     * Compute a hash code using the hash codes of the underlying objects
     *
     * @return a hashcode of the Pair
     */
    @Override
    public int hashCode() {
        return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode());
    }

    /**
     * Convenience method for creating an appropriately typed pair.
     * @param a the first object in the Pair
     * @param b the second object in the pair
     * @return a Pair that is templatized with the types of a and b
     */
    public static <A, B> Pair <A, B> create(A a, B b) {
        return new Pair<A, B>(a, b);

    }
    

    final public static <A, B> Pair <A, B> of(A a, B b) {
        return create(a, b);
    }
    
    
    
    
    public String toString() { return "Pair("+first.toString()+","+second.toString()+")"; }
}
