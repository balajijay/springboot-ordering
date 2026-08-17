
public class MathPalindrome {

	public static void main(String[] args) {
		
		Integer inputNumber = 99199;
		MathPalindrome mp = new MathPalindrome();
		Integer length = mp.numberOfDigits(inputNumber);
		Integer reverseValue = mp.reverseNumber(inputNumber, length);
		if (inputNumber.compareTo(reverseValue) == 0 ) {
			System.out.println("The input number is a Palindrome");
		}
		else {
			System.out.println("The input number is NOT a Palindrome");
		}
	}
	
	public Integer reverseNumber(Integer number, Integer length) {
		Integer reverseNumber = 0;
		for (int i=0; i < length; i++) {
			int tenPower = (int) Math.pow(10, length-i-1);
			//System.out.println("tenpower = " + tenPower);
			int multiplier = ((int) Math.pow(10, length - 1)) / tenPower;
			reverseNumber = reverseNumber + (number / tenPower) * multiplier;
			//System.out.println("reverseNumber = " + reverseNumber);
			number = number % tenPower;
			//System.out.println("number = " + number);

		}
		System.out.println("reverseNumber = " + reverseNumber);
		return reverseNumber;
	}
	
	public Integer numberOfDigits(Integer number) {
		Integer length = 0;
		while (number % 10 >= 1) {
			//System.out.println("number is " + number);
			number = number / 10;
			length++;
		}
		System.out.println("Number of digits is " + length);
		return length;
	}

}
