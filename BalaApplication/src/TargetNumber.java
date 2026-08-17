import java.util.*;

public class TargetNumber {

	public static void main(String[] args) {
		int[] arr = {2, 7, 11, 15};
		int[] result = targetNumberIndices(arr, 9);
		System.out.println(result[0] + ", " + result[1]);
	}
	
	public static int [] targetNumberIndices(int[] arr, int targetNumber) {
		HashMap<Integer, Integer> seen = new HashMap<>();
		int n = arr.length;
		int[] result = new int[2];
		for (int i=0; i<n; i++) {
			int compliment = targetNumber - arr[i];
			if (seen.containsKey(compliment)) {
				return new int[] {seen.get(compliment), i};
			}
			seen.put(arr[i], i);
		}
		return result;
	}

}
