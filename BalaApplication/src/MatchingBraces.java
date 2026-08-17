
import java.util.*;

public class MatchingBraces {

	public static void main(String[] args) {
		
		String str1 = "{([])}";
		String str2 = ")[](";
		String str3 = "[)}";
		
		System.out.println(str1 + " balancing brackets is " + balancingBrackets(str1));
		
		System.out.println(str2 + " balancing brackets is " + balancingBrackets(str2));
	
		System.out.println(str3 + " balancing brackets is " + balancingBrackets(str3));
	}
	
	static boolean balancingBrackets(String str) {
		if (str.length() % 2 != 0) {
			return false;
		}
		Deque<Character> stack = new ArrayDeque<>();
		for (int i=0; i<str.length();i++) {
			Character curr = str.charAt(i);
			if (curr == '{' || curr == '[' || curr == '(') {
				stack.push(curr);
				continue;
			}
			
			if (curr == '}' || curr == ']' || curr == ')') {
				
				if (stack.isEmpty()) {
					return false;
				}
				
				Character lastOpened = stack.pop();
				
				if (curr == '}' && lastOpened != '{') {	return false; }
				
				if (curr == ']' && lastOpened != '[') {	return false; }
				
				if (curr == ')' && lastOpened != '(') {	return false; }
					
			}
			
		}
		
		return stack.isEmpty();
	}
}
