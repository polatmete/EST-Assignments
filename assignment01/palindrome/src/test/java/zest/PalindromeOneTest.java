package zest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PalindromeOneTest {

    @Test
    void isPalindrome() {
        assertTrue(PalindromeOne.isPalindrome(12321));
        assertTrue(PalindromeOne.isPalindrome(123321));
        assertTrue(PalindromeOne.isPalindrome(121121));
        assertTrue(PalindromeOne.isPalindrome(11));
        assertTrue(PalindromeOne.isPalindrome(131));
    }

    @Test
    void inputIsNegative() {
        assertFalse(PalindromeOne.isPalindrome(-1));
        assertFalse(PalindromeOne.isPalindrome(-123));
        assertFalse(PalindromeOne.isPalindrome(-1441));
    }

    @Test
    void inputOfLengthOne() {
        assertTrue(PalindromeOne.isPalindrome(1));
        assertTrue(PalindromeOne.isPalindrome(0));
    }

    @Test
    void isNotPalindrome() {
        assertFalse(PalindromeOne.isPalindrome(127821));
        assertFalse(PalindromeOne.isPalindrome(511119));
        assertFalse(PalindromeOne.isPalindrome(1231));
        assertFalse(PalindromeOne.isPalindrome(13));
        assertFalse(PalindromeTwo.isPalindrome(1100));
        assertFalse(PalindromeOne.isPalindrome(00121));
    }

    @Test
    void outOfRange() {
        // on points
        assertFalse(PalindromeOne.isPalindrome((int) Math.pow(2, 20)));
        assertFalse(PalindromeOne.isPalindrome((int) -Math.pow(2, 20)));
        // off points -> illegal argument exception expected
        assertThrows(IllegalArgumentException.class, () -> PalindromeOne.isPalindrome((int) Math.pow(2, 20) + 1));
        assertThrows(IllegalArgumentException.class, () -> PalindromeOne.isPalindrome((int) -Math.pow(2, 20) - 1));
    }


}