package de.dualuse.commons.util.geometry;

import java.util.Map;

public interface CartesianMap<T> extends Map<CartesianMap.Coordinate, T> {
	static public interface Coordinate {
		public double x();
		public double y();
	}

}
