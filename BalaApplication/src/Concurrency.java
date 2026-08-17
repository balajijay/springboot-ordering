import java.util.*;

public class Concurrency {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Integer> nums = new ArrayList<>();
		nums.add(20);
		nums.add(10);
		nums.add(40);
		nums.add(70);
		nums.add(60);
		nums.add(50);
		System.out.println(" Second largest number is " + secondLargestNumber(nums));
	}
	
	public static int secondLargestNumber(List<Integer> nums) {
		
		nums.sort(Comparator.comparingInt(Integer::intValue).reversed());
		return nums.stream().skip(1).limit(1).findAny().get();
	}

}
