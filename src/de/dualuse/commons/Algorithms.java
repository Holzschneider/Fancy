package de.dualuse.commons;

import java.nio.BufferUnderflowException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


public class Algorithms {

	private Algorithms() { }


	private static <T> void swap(List<T> x, int a, int b) {
		T t = x.get(a);
		x.set(a, x.get(b));
		x.set(b, t);

		// Object t = x[a];
		// x[a] = x[b];
		// x[b] = t;
	}

	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////// MERGE SORT /////////////////////////////////////////////////////////////
	
	private static final int INSERTIONSORT_THRESHOLD = 7;
	
	public static <T> void mergeSort(List<T> src, List<T> dest, int low, int high, int off, Comparator<T> c) {
		int length = high - low;

		// Insertion sort on smallest arrays
		if (length < INSERTIONSORT_THRESHOLD) {
			for (int i = low; i < high; i++)
				for (int j = i; j > low && c.compare(dest.get(j - 1), dest.get(j)) > 0; j--)
					swap(dest, j, j - 1);
			return;
		}

		// Recursively sort halves of dest into src
		int destLow = low;
		int destHigh = high;
		low += off;
		high += off;
		int mid = (low + high) >>> 1;
		mergeSort(dest, src, low, mid, -off, c);
		mergeSort(dest, src, mid, high, -off, c);

		// If list is already sorted, just copy from src to dest. This is an
		// optimization that results in faster sorts for nearly ordered lists.
		if (c.compare(src.get(mid - 1), src.get(mid)) <= 0) {

			// System.arraycopy(src, low, dest, destLow, length);
			return;
		}

		// Merge sorted halves (now in src) into dest
		for (int i = destLow, p = low, q = mid; i < destHigh; i++) {
			if (q >= high || p < mid && c.compare(src.get(p), src.get(q)) <= 0)
				dest.set(i, src.get(p++));
			else
				dest.set(i, src.get(q++));
		}
	}

	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////// QUICK SORT /////////////////////////////////////////////////////////////

	public interface IntComparator {
		int compare(int o1, int o2);		
	}
	

	public static <T extends Comparable<? super T>> void quicksort(List<T> elements) { quicksort(elements, 0, elements.size()-1); }
	public static <T extends Comparable<? super T>> void quicksort(List<T> elements, int low, int high) {
		int i = low, j = high;
		T pivot = elements.get((low + high) / 2);

		while (i <= j) {
			while (elements.get(i).compareTo(pivot)<0)  i++;
			while (elements.get(j).compareTo(pivot)>0)  j--;
			if (!(i <= j)) continue;
			T t = elements.get(i); 
			elements.set(i, elements.get(j)); 
			elements.set(j, t); 
			i++; j--;
		}
		if (low < j) quicksort(elements,low,j);
		if (i < high) quicksort(elements,i,high);
	}
	
	public static <T> void quicksort(List<T> elements, Comparator<? super T> c) { quicksort(elements, 0, elements.size()-1, c); }
	public static <T> void quicksort(List<T> elements, int low, int high, Comparator<? super T> c) {
		int i = low, j = high;
		T pivot = elements.get((low + high) / 2);

		while (i <= j) {
			while (c.compare(elements.get(i),pivot)<0)  i++;
			while (c.compare(elements.get(j),pivot)>0)  j--;
			if (!(i <= j)) continue;
			T t = elements.get(i); 
			elements.set(i, elements.get(j)); 
			elements.set(j, t); 
			i++; j--;
		}
		if (low < j) quicksort(elements,low,j,c);
		if (i < high) quicksort(elements,i,high,c);
	}

	public static <T> void quicksort(T[] elements, Comparator<? super T> c) { quicksort(elements, 0, elements.length-1, c); }
	public static <T> void quicksort(T[] elements, int low, int high, Comparator<? super T> c) {
		int i = low, j = high;
		T pivot = elements[(low + high) / 2];

		while (i <= j) {
			while (c.compare(elements[i],pivot)<0) i++;
			while (c.compare(elements[j],pivot)>0) j--;
			if (!(i <= j)) continue;
			T t = elements[i];
			elements[i]=elements[j];
			elements[j]=t;
			i++;
			j--;
		}
		if (low < j) quicksort(elements,low,j,c);
		if (i < high) quicksort(elements,i,high,c);
	}
	
	public static void quicksort(int[] elements, IntComparator c) { quicksort(elements, 0, elements.length-1, c); }
	public static void quicksort(int[] elements, int low, int high, IntComparator c) {
		int i = low, j = high;
		int pivot = elements[(low + high) / 2];

		while (i <= j) {
			while (c.compare(elements[i],pivot)<0) i++;
			while (c.compare(elements[j],pivot)>0) j--;
			if (!(i <= j)) continue;
			int t = elements[i];
			elements[i]=elements[j];
			elements[j]=t;
			i++;
			j--;
		}
		if (low < j) quicksort(elements,low,j,c);
		if (i < high) quicksort(elements,i,high,c);
	}

	public static <T extends Comparable<? super T>> void quicksort(T[] elements) { quicksort(elements, 0, elements.length-1); }
	public static <T extends Comparable<? super T>> void quicksort(T[] elements, int low, int high) {
		int i = low, j = high;
		// Get the pivot element from the middle of the list
		T pivot = elements[(low + high) / 2];

		// Divide into two lists
		while (i <= j) {
			// If the current value from the left list is smaller then the pivot
			// element then get the next element from the left list
			while (elements[i].compareTo(pivot)<0) {
				i++;
			}
			// If the current value from the right list is larger then the pivot
			// element then get the next element from the right list
			while (elements[j].compareTo(pivot)>0) {
				j--;
			}

			// If we have found a values in the left list which is larger then
			// the pivot element and if we have found a value in the right list
			// which is smaller then the pivot element then we exchange the
			// values.
			// As we are done we can increase i and j
			if (i <= j) {
				T t = elements[i];
				elements[i]=elements[j];
				elements[j]=t;
//				exchange(numbers,i, j);
				i++;
				j--;
			}
		}
		// Recursion
		if (low < j)
			quicksort(elements,low,j);
		if (i < high)
			quicksort(elements,i,high);
	}
	
	

	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////// MIN HEAP ///////////////////////////////////////////////////////////////
	
	public static <T extends Comparable<? super T>> T pile(T[] queue, T key, int size) {
		int k = size;
		
		while (k > 0) {
			int parent = (k - 1) >>> 1;
			T e = queue[parent];
			if (key.compareTo(e) >= 0)
				break;
			queue[k] = e;
			k = parent;
		}
		queue[k] = key;
		
		return queue[0];
	}
	
	
	public static <T extends Comparable<? super T>> T poll(T[] queue, int size) {
		T result = queue[0];
		
		T key = queue[--size];
        queue[size] = null;
        
        int half = size >>> 1, k = 0;        // loop while a non-leaf
        while (k < half) {
            int child = (k << 1) + 1; // assume left child is least
            T c = queue[child];
            int right = child + 1;
            if (right < size && c.compareTo(queue[right]) > 0)
                c = queue[child = right];
            if (key.compareTo(c) <= 0)
                break;
            
            queue[k] = c;
            k = child;
        }
        queue[k] = key;
        
		return result;
	}

	
	public static <T> T pile(T[] queue, T key, int size, Comparator<T> cmp) {
		int k = size;
		
		while (k > 0) {
			int parent = (k - 1) >>> 1;
			T e = queue[parent];
			if (cmp.compare(key,e) >= 0)
				break;
			queue[k] = e;
			k = parent;
		}
		queue[k] = key;
		
		return queue[0];
	}
	
	public static <T> T poll(T[] queue, int size, Comparator<T> cmp) {
		T result = queue[0];
		
		T key = queue[--size];
        queue[size] = null;
        
        int half = size >>> 1, k = 0;        // loop while a non-leaf
        while (k < half) {
            int child = (k << 1) + 1; // assume left child is least
            T c = queue[child];
            int right = child + 1;
            if (right < size && cmp.compare(c,queue[right]) > 0)
                c = queue[child = right];
            if (cmp.compare(key,c) <= 0)
                break;
            
            queue[k] = c;
            k = child;
        }
        queue[k] = key;
        
		return result;
	}

	

	public static int pile(int[] queue, int key, int size, IntComparator cmp) {
		int k = size;
		
		while (k > 0) {
			int parent = (k - 1) >>> 1;
			int e = queue[parent];
			if (cmp.compare(key,e) >= 0)
				break;
			queue[k] = e;
			k = parent;
		}
		queue[k] = key;
		
		return queue[0];
	}
	
	public static int poll(int[] queue, int size, IntComparator cmp) {
		int result = queue[0];
		
		int key = queue[--size];
        queue[size] = 0;
        
        int half = size >>> 1, k = 0;        // loop while a non-leaf
        while (k < half) {
            int child = (k << 1) + 1; // assume left child is least
            int c = queue[child];
            int right = child + 1;
            if (right < size && cmp.compare(c,queue[right]) > 0)
                c = queue[child = right];
            if (cmp.compare(key,c) <= 0)
                break;
            
            queue[k] = c;
            k = child;
        }
        queue[k] = key;
        
		return result;
	}

	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////// MIN MAX HEAP ////////////////////////////////////////////////////////////
	
	
	public static int peekMin(int[] queue, int size, IntComparator cmp) {
		return (size >= 1) ? queue[0] : -1;
	}
	
	public static int peekMax(int[] queue, int size, IntComparator cmp) {
		if (size < 1)  	return -1; 
		if (size == 1)  return queue[0]; 
		if (size == 2)  return queue[1];
		return cmp.compare(queue[1], queue[2]) < 0 ? queue[2] : queue[1]; 
	}
	
	public static int pollMin(int[] queue, int size, IntComparator cmp) {
		// exception if empty
		if (size < 1) throw new ArrayIndexOutOfBoundsException(0); //new BufferUnderflowException();
		// if one element
		if (size == 1) return queue[0];
		int min = peekMin(queue, size, cmp);
		// if two elements
		if (size == 2) queue[0] = queue[1];
		// if three elements
		else if (size == 3) {
			queue[0] = cmp.compare(queue[1], queue[2]) < 0 ? queue[1] : queue[2]; 
			queue[1] = cmp.compare(queue[1], queue[2]) > 0 ? queue[1] : queue[2];
		}
		else {
			int x = queue[size-1];
//			System.out.println("x is " + x);
			int m = 3;
			int i = 0;
			for (i = 0; i < size;) {
				int minLevel = isMinLevel(i)?1:-1;
				// min child
				int last = i*2+2;
				int first = i*2+1;
				if (first >= size) break;
				m = first;
				for (int j = first+1; j <= last; j++) {
					if (j >= size) break;
					if (minLevel*cmp.compare(queue[j], queue[m]) < 0) {
						m = j;
					}
				}
				// min grandchild
				last = i*4+6;
				first = i*4+3;
//				System.out.println("find smallest grandchild of " + i + "(value: "+queue[i]+")");
				for (int j = first; j <= last; j++) {
					if (j >= size) break;
					if (minLevel*cmp.compare(queue[j], queue[m]) < 0) {
						m = j;
					}
				}
//				System.out.println("m is: " + m + " from: " + first + " to: " + last);
				if (minLevel*cmp.compare(x, queue[m]) < 0) {
					break;
				} else {
					queue[i] = queue[m];
					minLevel = isMinLevel(m)?1:-1;
					int parent = (m - 1) >>> 1;
					if (minLevel*cmp.compare(x, queue[parent]) > 0) {
						int tmp = queue[parent];
						queue[parent] = x;
						x = tmp;
					}
					i = m;
				}
			}
//			System.out.println("set queue["+i+"]("+queue[i]+") = " + x);
			queue[i] = x;
		}
		return min;
	}

	public static int pollMax(int[] queue, int size, IntComparator cmp) {
		// if queue is empty
		if (size < 1) throw new BufferUnderflowException();
		// if has one element, it is max
		if (size == 1) return queue[0];
		// if has two elements, the second is max
		if (size == 2) return queue[1];
		// else max is max of root children
		int max = peekMax(queue, size, cmp);
	
		// 
		if (size == 3) queue[1] = cmp.compare(queue[1], queue[2]) < 0 ? queue[1] : queue[2]; //Math.min(queue[1], queue[2]);
		else {
			int x = queue[size-1];
//			System.out.println("x is " + x);
			int m = 3;
			int i = 0;
			for (i = cmp.compare(queue[1], queue[2]) < 0 ? 2 : 1; i < size;) {
				int minLevel = isMinLevel(i)?1:-1;
				// min child
				int last = i*2+2;
				int first = i*2+1;
				if (first >= size) break;
				m = first;
				for (int j = first+1; j <= last; j++) {
					if (j >= size) break;
					if (minLevel*cmp.compare(queue[j], queue[m]) < 0) {
						m = j;
					}
				}
				// min grandchild
				last = i*4+6;
				first = i*4+3;
//				System.out.println("find smallest grandchild of " + i + "(value: "+queue[i]+")");
				for (int j = first; j <= last; j++) {
					if (j >= size) break;
					if (minLevel*cmp.compare(queue[j], queue[m]) < 0) {
						m = j;
					}
				}
//				System.out.println("m is: " + m);
				if (minLevel*cmp.compare(x, queue[m]) < 0) {
					break;
				} else {
					queue[i] = queue[m];
					minLevel = isMinLevel(m)?1:-1;
					int parent = (m - 1) >>> 1;
					if (minLevel*cmp.compare(x, queue[parent]) > 0) {
						int tmp = queue[parent];
						queue[parent] = x;
						x = tmp;
					}
					i = m;
				}
			}
//			System.out.println("set queue["+i+"]("+queue[i]+") = " + x);
			queue[i] = x;
		}
//		System.out.println("max is " + max);
		return max;
	}
	
	
	public static int pileMinMax(int[] queue, int key, int size, IntComparator cmp) {
		if (size == 0) {
			queue[0] = key;
			return 1;
		}
		if (size == queue.length) {
			pollMax(queue, size--, cmp);
        }
		int tmp;
		int index = size;
		queue[index] = key;
		int minLevel = isMinLevel(index)?1:-1;
			int parent = (index - 1) >>> 1;
			int grandparent = (parent - 1) >>> 1;
			if (minLevel*cmp.compare(queue[index], queue[parent]) > 0) {
				do {
					tmp = queue[parent];
					queue[parent] = queue[index];
					queue[index] = tmp;
					index = parent;
					if (index < 3) break;
					parent = (((index-1)>>>1) - 1) >>> 1;
				} while(minLevel*cmp.compare(queue[index], queue[parent]) > 0);
			} 
			else if (index > 2 && minLevel*cmp.compare(queue[index], queue[grandparent]) < 0) {
				do {
					tmp = queue[grandparent];
					queue[grandparent] = queue[index];
					queue[index] = tmp;
					index = grandparent;
					if (index < 3) break;
					grandparent = (((index-1)>>>1) - 1) >>> 1;
				} while (minLevel*cmp.compare(queue[index], queue[grandparent]) < 0);
			}
		return size+1;
	}
	
	private static boolean isMinLevel(int index) {
		boolean isMin = true;
		int border = 0;
		while (border < index) {
			border = (border << 1) +2;
			isMin = !isMin;
		}
		return isMin;
	}
	
//	static int min(int x, int y, IntComparator cmp) {
//		return cmp.compare(x, y) < 0 ? x : y;
//	}
//	
//	static int max(int x, int y, IntComparator cmp) {
//		return cmp.compare(x, y) > 0 ? x : y;
//	}
//	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	public static void main(String[] args) {
//		
//		Random rng = new Random(1);
//		
//		Integer numbers[] = new Integer[10];
//		int size = 0;
//		
//		
//		for (int i=0;i<numbers.length;i++)
//			pile(numbers, rng.nextInt(20), size++, new Comparator<Integer>() {
//				public int compare(Integer o1, Integer o2) {
//					return o1-o2;
//				}
//			} );
////			numbers[i] = rng.nextInt(20);
//		
//		
//		System.out.println(Arrays.toString(numbers));
//		
//		for (int i=0;i<numbers.length;i++)
//			System.out.println( poll(numbers, size--,  new Comparator<Integer>() {
//				public int compare(Integer o1, Integer o2) {
//					return o1-o2;
//				}
//			}));
//				
//	}
}














