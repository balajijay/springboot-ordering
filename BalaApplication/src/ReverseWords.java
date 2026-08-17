
import java.util.*;
import java.util.stream.*;

public class ReverseWords {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String name = "Balaji Narasimhan";
		String[] arr = name.split("");
		String reverseString = Arrays.stream(arr)
				.sorted(String.CASE_INSENSITIVE_ORDER.reversed()).collect(Collectors.joining());
		System.out.println(name);
		System.out.println("");
		System.out.println(reverseString);
	}

}
