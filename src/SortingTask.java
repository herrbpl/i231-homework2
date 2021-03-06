
import java.util.*;

public class SortingTask {

	public enum SortMethod {
		 NA, INSERTION_SORT, BINARY_INSERTION_SORT, QUICKSORT, ARRAYS, COLLECTIONS
	}
	

	public static class Result {
		public SortMethod method = SortMethod.NA;
		public int items = 0;
		public int runtime = 0;
		public Result(SortMethod method, int items, int runtime ) {
			// TODO Auto-generated constructor stub
			this.method = method;
			this.runtime = runtime;
			this.items = items;
		}
		
	}

	public static final int MAX = 512000;
	//public static final int MAX = 30;

	/** Main method. */
	static public void main(String[] args) {
		List<Integer> randlist = new ArrayList<Integer>(MAX); // original
		Random generaator = new Random();
		int maxKey = Math.min(1000, (MAX + 32) / 16);
		//int maxKey = 50;
		for (int i = 0; i < MAX; i++) {
			randlist.add(new Integer(generaator.nextInt(maxKey)));
		}

		List<Result> results  = new ArrayList<Result>();		
		
		int rightLimit = randlist.size() / 16;

		// Start a competition
		for (int round = 0; round < 4; round++) {
			rightLimit = 2 * rightLimit;
			System.out.println("\nLength: " + String.valueOf(rightLimit));

			List<Integer> copy1 = new ArrayList<Integer>(randlist);
			long stime = new Date().getTime();
			insertionSort(copy1, 0, rightLimit);
			long ftime = new Date().getTime();
			int diff = new Long(ftime - stime).intValue();
			System.out.println("\nInsertion sort");
			System.out.println("Time (ms): " + String.valueOf(diff));
			
			results.add(new Result(SortMethod.INSERTION_SORT, rightLimit, diff));
			
			if (!checkOrder(copy1, 0, rightLimit))
				throw new RuntimeException("Wrong order!!!");

			// Binary insertion sort
			List<Integer> copy2 = new ArrayList<Integer>(randlist);
			System.out.println(String.format("List size: %d", copy2.size()));

			stime = new Date().getTime();
			binaryInsertionSort(copy2, 0, rightLimit);
			ftime = new Date().getTime();
			diff = new Long(ftime - stime).intValue();
			System.out.println("\nBinary insertion sort");
			System.out.println("Time (ms): " + String.valueOf(diff));
			
			results.add(new Result(SortMethod.BINARY_INSERTION_SORT, rightLimit, diff));
			
			
			if (!checkOrder(copy2, 0, rightLimit))
				throw new RuntimeException("Wrong order!!!");

			 List<Pair> opairs = new ArrayList<Pair>(MAX);
			 for (int i = 0; i < randlist.size(); i++) {
			 opairs.add(new Pair(randlist.get(i), i));
			 }
			 binaryInsertionSort(opairs, 0, rightLimit);
			 if (!checkStability(opairs, 0, rightLimit))
			 throw new RuntimeException("Method not stable!");
			
			 // quicksort
			
			
			 List<Integer> copy3 = new ArrayList<Integer>(randlist);
			 System.out.println(String.format("List size: %d", copy3.size()));
			 stime = new Date().getTime();
			 qsort(copy3, 0, rightLimit);
			 
			 ftime = new Date().getTime();
			 diff = new Long(ftime - stime).intValue();
			 System.out.println("\nQuicksort");
			 System.out.println("Time (ms): " + String.valueOf(diff));
			 
			 results.add(new Result(SortMethod.QUICKSORT, rightLimit, diff));
				
			 if (!checkOrder(copy3, 0, rightLimit))
			 throw new RuntimeException("Wrong order!!!");
			 
			 List<Integer> copy4 = new ArrayList<Integer>(randlist);
			 Integer[] sarray = new Integer[rightLimit];
			 sarray = (Integer[]) copy4.toArray(sarray);
			 System.out.println(String.format("List size: %d", sarray.length));
			 stime = new Date().getTime();
			 
			 Arrays.sort(sarray, 0, rightLimit);
			 ftime = new Date().getTime();
			 copy4 = Arrays.asList(sarray);
			 diff = new Long(ftime - stime).intValue();
			 System.out.println("\njava.util.Arrays");
			 System.out.println("Time (ms): " + String.valueOf(diff));
			 
			 results.add(new Result(SortMethod.ARRAYS, rightLimit, diff));
				
			 if (!checkOrder(copy4, 0, rightLimit))
			 throw new RuntimeException("Wrong order!!!");
			 List<Integer> copy5 = new ArrayList<Integer>(randlist);
			 copy5 = copy5.subList(0, rightLimit);
			 
			 stime = new Date().getTime();
			 Collections.sort(copy5);
			 ftime = new Date().getTime();
			 diff = new Long(ftime - stime).intValue();
			 System.out.println("\njava.util.Collections");
			 System.out.println("Time (ms): " + String.valueOf(diff));
			 
			 results.add(new Result(SortMethod.COLLECTIONS, rightLimit, diff));
				
			 
			 if (!checkOrder(copy5, 0, rightLimit))
			 throw new RuntimeException("Wrong order!!!");
		}
		
		System.out.println("METHOD,ITEMCOUNT,RUNTIME\n");
		
		for (Result result : results) {
			System.out.printf("%s,%d,%d\n", result.method.name(), result.runtime, result.runtime);
		}
		
	} // main()

	/**
	 * Sort a part of the list using quicksort method.
	 * 
	 * @param array
	 *            list
	 * @param l
	 *            starting index (included)
	 * @param r
	 *            ending index (excluded)
	 */
	static public <T extends Object & Comparable<? super T>> void qsort(List<T> array, int l, int r) {
		if (array.size() < 2)
			return;
		if ((r - l) < 2)
			return;
		int i = l;
		int j = r - 1;
		T x = array.get((i + j) / 2);
		do {
			while (array.get(i).compareTo(x) < 0)
				i++;
			while (x.compareTo(array.get(j)) < 0)
				j--;
			if (i <= j) {
				T tmp = array.get(i);
				array.set(i, array.get(j));
				array.set(j, tmp);
				i++;
				j--;
			}
		} while (i < j);
		if (l < j)
			qsort(array, l, j + 1); // recursion for left part
		if (i < r - 1)
			qsort(array, i, r); // recursion for right part
	} // qsort()

	/**
	 * @see http://jeffreystedfast.blogspot.com.ee/2007/02/binary-insertion-sort.html
	 * 
	 * @param a - List of T
	 * @param value -  value to search position for 
	 * @param left - left index, inclusive
	 * @param right - right index, inclusive
	 * @return
	 */
	public static <T extends Object & Comparable<? super T>> int binarySearch(List<T> a, T value, int left, int right) {
		
		int mid = left + ((right - left) / 2);
	    if (left == right) {
//	    	System.out.printf("Searching for %s\n", value.toString());
//	    	System.out.println("\n"+dumpArray(a));
//	    	System.out.printf("1: L:%d R:%d M:%d\n", left, right, mid);
	    	return left;
	    }
	        
	    
	   
	    
	    int i = value.compareTo(a.get(mid)); // if value > a[mid], =1
	    
	    
	    if (i > 0) // (i > a[mid])
	        return binarySearch (a, value, mid + 1, right);
	    else if (i < 0) //(key < a[mid])
	        return binarySearch (a, value, left, mid);

//	    System.out.printf("Searching for %s\n", value.toString());
//	    System.out.println("\n"+dumpArray(a));
//	    System.out.printf("2: L:%d R:%d M:%d\n", left, right, mid);

	    // 
	    int j;
	    for(j = mid; j < right; j++) {
//	    	if (j > control) {
//	    		System.out.printf("AAA %d %d", control, j);
//	    	}
	    	i = value.compareTo(a.get(j)); // if value > a[mid], =1
	    	if (i < 0) {
	    		break; 
	    	}
	    }
//	    if (j > control) {
//    		System.out.printf("BBB %d %d", control, j);
//    	}
	    return j;
	}
	
	

	/**
	 * creates nice string from array
	 * @param a - array
	 * @return Nice array
	 */
	public static <T extends Object & Comparable<? super T>> String dumpArray(List<T> a) {
		int maxlen = 0;
		for (T t : a) {
			int i = t.toString().length();
			if (i > maxlen) {
				maxlen = i;
			}
		}
		
		
		StringBuilder s1 = new StringBuilder();
		StringBuilder s2 = new StringBuilder();
		String x = "", y="";
		for (int i = 0; i < a.size(); i++) {
			
			y = a.get(i).toString();
			x = Integer.toString(i);
			
			
			for (int m = x.length(); m < Math.max(x.length(), y.length()); m++ ) {
				s1.append(' ');
			}
			s1.append(x);
			s1.append(" |");
			for (int m = y.length(); m < Math.max(x.length(), y.length()); m++ ) {
				s2.append(' ');
			}
			
			s2.append(y);
			s2.append(" |");
			
											
			//System.out.printf("%d==>%s ", i, a.get(i).toString());
		}
		StringBuilder s3 = new StringBuilder();
		s3.append("L:"+a.size()+"\n");
		s3.append("K:" + s1.toString()).append("\nV:" + s2.toString());
		return s3.toString();
	
	}
	
	
	/**
	 * Sort a part of the list using binary insertion sort method in a stable
	 * manner.
	 * 
	 * @param a
	 *            list
	 * @param left
	 *            starting position (included)
	 * @param right
	 *            ending position (excluded)
	 */
	public static <T extends Object & Comparable<? super T>> void binaryInsertionSort(List<T> a, int left, int right) {
		

		
		// TODO!!! Your code here!
		if (a.size() < 2)
			return;
		if ((right - left) < 2)
			return;
		

		
		for (int i = left + 1; i < right; i++) {
			
			T b = a.remove(i); 

			
			int j;
			
			// Now the search part. List before i should be sorted so we can use
			// binary search
 
			if (b.compareTo(a.get(i-1)) >=0) {
				
				j = i;
				
			} else  {
				j = binarySearch(a, b, left, i);
			}
		
						
			a.add(j, b); // insert b to position j
			
		}

	} // binaryInsertionSort()

	/**
	 * Sort a part of the list of comparable elements using insertion sort.
	 * 
	 * @param a
	 *            list
	 * @param l
	 *            starting position (included)
	 * @param r
	 *            ending position (excluded)
	 */
	static public <T extends Object & Comparable<? super T>> void insertionSort(List<T> a, int l, int r) {
		if (a.size() < 2)
			return;
		if ((r - l) < 2)
			return;
		for (int i = l + 1; i < r; i++) {
			T b = a.remove(i);
			int j;
			for (j = l; j < i; j++) {
				if (b.compareTo(a.get(j)) < 0)
					break;
			}
			a.add(j, b); // insert b to position j
		}
	} // insertionSort()

	/**
	 * Check whether a given interval in the list is ordered.
	 * 
	 * @param a
	 *            sorted (?) list.
	 * @param l
	 *            left index (included)
	 * @param r
	 *            right index (excluded)
	 * @return interval is ordered?
	 */
	static <T extends Object & Comparable<? super T>> boolean checkOrder(List<T> a, int l, int r) {
		if (a == null)
			throw new IllegalArgumentException();
		if (a.size() < 2)
			return true;
		if (l < 0 || r > a.size() || l >= r)
			throw new IllegalArgumentException();
		if (r - l < 2)
			return true;
		for (int i = l; i < r - 1; i++) {
			if (a.get(i).compareTo(a.get(i + 1)) > 0)
				return false;
		} // for
		return true;
	} // checkOrder()

	/** Is the list sorted in a stable manner. */
	static boolean checkStability(List<Pair> a, int l, int r) {
		if (a == null)
			throw new IllegalArgumentException();
		if (a.size() < 2)
			return true;
		if (l < 0 || r > a.size() || l >= r)
			throw new IllegalArgumentException();
		if (r - l < 2)
			return true;
		for (int i = l; i < r - 1; i++) {
			if (a.get(i).compareTo(a.get(i + 1)) > 0)
				return false;
			else if (a.get(i).compareTo(a.get(i + 1)) == 0) {
				// System.out.print("=");
				if (((Pair) a.get(i)).getSecundo() > ((Pair) a.get(i + 1)).getSecundo()) {
					System.out.println("Method is not stable. First error: " + i + ". " + a.get(i) + " <--> " + (i + 1)
							+ ". " + a.get(i + 1));
					return false;
				}
			}
		} // for
		return true;
	}
}

/** Local class to test stability. */
class Pair implements Comparable<Pair> {

	private int primo;
	private int secundo;

	Pair(int a, int b) {
		primo = a;
		secundo = b;
	}

	public int getPrimo() {
		return primo;
	}

	public int getSecundo() {
		return secundo;
	}

	@Override
	public String toString() {
		return "(" + String.valueOf(primo) + "," + String.valueOf(secundo) + ")";
	}

	public int compareTo(Pair o) {
		int esim = this.primo;
		int teine = ((Pair) o).primo;
		if (esim > teine)
			return 1;
		else if (esim < teine)
			return -1;
		else
			return 0;
	}
} // Pair
