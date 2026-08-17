
public class SuspiciousAcc {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] amounts = {200, 500, 400, 100, 50};
		int [] timestamps = {0, 10, 15, 90, 95}; 
		System.out.println("" + isSuspicious(amounts, timestamps));
				
	}
	/*
	 * Flag an account as suspicious if there exist 3 or more transactions within any 60-second window 
	 *  where the sum of amounts in that window exceeds 1000.
	 * amounts    = [200, 500, 400, 100, 50]   
	 * timestamps = [0,   10,  15,  90,  95]
	 * 
	 * 
	 */
	    public static boolean isSuspicious(int[] amounts, int[] timestamps) {
	        // amounts[i] and timestamps[i] correspond to the same transaction
	        // timestamps is strictly increasing
	    	int right = 0;
	    	int left = 0;
	    	long sum = 0;
	    	int n = amounts.length;
	    	for (right = 0; right < n; right++) {
	    		sum += amounts[right];
	    		while (timestamps[right] - timestamps[left] > 60) {
	    			sum -= amounts[left];
	    			left ++;
	    		}
	    		int count = right - left + 1;
	    		if (count >= 3 && sum > 1000 ) {
	    			return true;
	    		}
	    	}
	    	
	    	return false;
	    }

}
