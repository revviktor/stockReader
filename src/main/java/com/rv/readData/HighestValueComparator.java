package com.rv.readData;

import java.util.Comparator;
import java.util.Map;

public class HighestValueComparator<T> implements Comparator<T> {
	Map<T, Double> base;

	public HighestValueComparator(Map<T, Double> base) {
		this.base = base;
	}

	// Note: this comparator imposes orderings that are inconsistent with
	// equals.
	public int compare(T a, T b) {
		if (base.get(a) > base.get(b)) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}
}