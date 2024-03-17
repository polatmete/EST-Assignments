package zest;

public class PalindromeOne {

    public static boolean isPalindrome(int x) {
        if (x < -Math.pow(2, 20) || x > Math.pow(2, 20)) {
            throw new IllegalArgumentException("Input is out of range: " + x + ". It has to be in the range of -2^20 <= x <= 2^20 - 1");
        }

        // convert input into an array and rest is nothing but a simple two pointer solution
        char[] numbers = String.valueOf(x).toCharArray();

        int start = 0;
        int end = numbers.length - 1;
        while (start < end) {
            if (numbers[start] != numbers[end]) return false;
            start++;
            end--;
        }
        return true;
    }
}
