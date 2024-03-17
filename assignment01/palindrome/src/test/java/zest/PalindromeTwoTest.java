package zest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PalindromeTwoTest {
    @Test
    void isPalindrome() {
        assertTrue(PalindromeTwo.isPalindrome(12321));
        assertTrue(PalindromeTwo.isPalindrome(123321));
        assertTrue(PalindromeTwo.isPalindrome(121121));
        assertTrue(PalindromeTwo.isPalindrome(11));
        assertTrue(PalindromeTwo.isPalindrome(131));
    }

    @Test
    void inputIsNegative() {
        assertFalse(PalindromeTwo.isPalindrome(-1));
        assertFalse(PalindromeTwo.isPalindrome(-123));
        assertFalse(PalindromeTwo.isPalindrome(-1441));
    }

    @Test
    void inputOfLengthOne() {
        assertTrue(PalindromeTwo.isPalindrome(1));
        assertTrue(PalindromeTwo.isPalindrome(0));
    }

    @Test
    void isNotPalindrome() {
        assertFalse(PalindromeTwo.isPalindrome(127821));
        assertFalse(PalindromeTwo.isPalindrome(511119));
        assertFalse(PalindromeTwo.isPalindrome(1231));
        assertFalse(PalindromeTwo.isPalindrome(13));
        assertFalse(PalindromeTwo.isPalindrome(1100));
        assertFalse(PalindromeOne.isPalindrome(00121));
    }

    @Test
    void outOfRange() {
        // on points
        assertFalse(PalindromeTwo.isPalindrome((int) Math.pow(2, 20)));
        assertFalse(PalindromeTwo.isPalindrome((int) -Math.pow(2, 20)));
        // off points -> illegal argument exception expected
        assertThrows(IllegalArgumentException.class, () -> PalindromeTwo.isPalindrome((int) Math.pow(2, 20) + 1));
        assertThrows(IllegalArgumentException.class, () -> PalindromeTwo.isPalindrome((int) -Math.pow(2, 20) - 1));
    }
}