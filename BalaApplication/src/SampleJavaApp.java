import java.util.*;

public class SampleJavaApp {

	    @FunctionalInterface
	    interface NumberMultiplier {
	        int multiply(List<Integer> numbers);  // step 1 define
	    }

	    public static void main(String[] args) {
	        // Implement the interface using a lambda expression
	        NumberMultiplier product = (numbers) -> numbers.stream().reduce(1, (a, b) -> a*b);  // step 2 implement
	        
	        // Pass parameters into the interface method
	        List<Integer> numList = new ArrayList<>();
	        numList.add(2);
	        numList.add(3);
	        numList.add(4);
	        numList.add(5);

	        int result = product.multiply(numList); // step 3 actual method call

	        // Print out the results
	        System.out.println("The product of the four numbers is: " + result);
	    }
	}
