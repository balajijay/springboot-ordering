
import java.util.*;

public class LongestSubstring {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(longestsubstr("abcabcbb"));
	}
	
	public static int longestsubstr(String str) {
		int left = 0;
		int right = 0;
		int maxLength = 0;
		HashSet<Character> set = new HashSet<>();
		for (right = 0; right < str.length();right ++) {
			if (set.contains(str.charAt(right))) {
				set.remove(str.charAt(left));
				left++;
			}
			set.add(str.charAt(right));
			maxLength = Math.max(maxLength, right-left+1);
			}
		
		return maxLength ;
	}

}
