import java.util.*;

public class OverlapAndMergeItems {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[][] arr = {{1,3}, {2,6}, {8,10}, {15, 18}};
		int[][] result = mergeOverLapItems(arr);
		for (int i=0; i<result.length; i++) {
			int[] tArr = result[i];
			System.out.println(tArr[0] + ", "+ tArr[1]);
		}

	}
	
	public static int[][] mergeOverLapItems(int arr[][]) {
	    Arrays.sort(arr, Comparator.comparingInt(a -> a[0]));
	    List<int[]> list = new ArrayList<>();
	    int[] current = arr[0];
	    list.add(current);
	    for (int i=1; i<arr.length; i++) {
	    	int[] next = arr[i];
	    	if (next[0] <= current[1]) {
	    		current[1] = Math.max(current[1], next[1]);
	    	}
	    	else {
	    		current = next;
	    		list.add(current);
	    	}
	    }
		
		return list.toArray(new int[list.size()][]);
	}

}
