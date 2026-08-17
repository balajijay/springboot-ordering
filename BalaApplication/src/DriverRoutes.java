
import java.util.*;

public class DriverRoutes {

	public static void main(String[] args) {
		int [][] arr = { {1,5}, {2,6}, {4,12}, {8, 10} };

		int result = minTrucks(arr);
		
		System.out.println("Min number of trucks is " + result);
	}
	/*  routes = [[1,5],[2,6],[5,9],[8,10]]
		Truck 1: [1,5] -> [5,9]   (touching is fine)
		Truck 2: [2,6] -> [8,10]
		Output: 2
		Constraints:
		1 <= routes.length <= 10^5
		0 <= start < end <= 10^9 
		Think about what happens if you sort by start time vs. end time, 
		and what data structure lets you efficiently find "is there a truck free right now?"
	 */

	public static int minTrucks(int[][] routes) {
	    Arrays.sort(routes, Comparator.comparingInt(a -> a[0]));
	    PriorityQueue<Integer> endTimes = new PriorityQueue<>();

	    for (int[] route : routes) {
	        if (!endTimes.isEmpty() && endTimes.peek() <= route[0]) {
	            endTimes.poll(); // reuse the truck that's now free
	        }
	        endTimes.offer(route[1]);
	    }

	    return endTimes.size();
	}
}
