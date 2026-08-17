import java.util.*;

public class MaxSubArraySum {

	public static void main(String[] args) {
		int[] inputValues = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
				//{700, -500, 100, 1200, 900, -600, 1350, 1500, 600};
		int result = maxSubArraySum(inputValues);
		System.out.println(result);
	}
	/* 
	 Input: [-2, 1, -3, 4, -1, 2, 1, -5, 4]
	 Output: 6   (subarray [4, -1, 2, 1])
	 Constraints: 1 <= n <= 10^5  	  */
	
	public static int maxSubArraySum(int[] arr) {
		int n = arr.length;
		int best = arr[0];
		int bestEndingHere = arr[0];
		for (int i=1; i<n;i++) {
			bestEndingHere = Math.max(arr[i], bestEndingHere+arr[i]);
			best = Math.max(best, bestEndingHere);
		}
		
		return best;
	}

}
