package zest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SumOfTwoIntegersTest {
    @Test
    void zeroInputs() {
        assertEquals(0, SumOfTwoIntegers.getSum(0, 0));
    }

    @Test
    void singleZeroInput() {
        assertEquals(1, SumOfTwoIntegers.getSum(0, 1));
        assertEquals(1, SumOfTwoIntegers.getSum(1, 0));
        assertEquals(-1, SumOfTwoIntegers.getSum(0, -1));
        assertEquals(-1, SumOfTwoIntegers.getSum(-1, 0));
    }

    @Test
    void negativeInputs() {
        assertEquals(-3, SumOfTwoIntegers.getSum(-1, -2));
        assertEquals(-12, SumOfTwoIntegers.getSum(-9, -3));
        assertEquals(-12, SumOfTwoIntegers.getSum(-4, -8));
    }

    @Test
    void positiveInputs() {
        assertEquals(3, SumOfTwoIntegers.getSum(1, 2));
        assertEquals(12, SumOfTwoIntegers.getSum(9, 3));
        assertEquals(12, SumOfTwoIntegers.getSum(4, 8));
    }

    @Test
    void negativePositiveInput() {
        assertEquals(4, SumOfTwoIntegers.getSum(-5, 9));
        assertEquals(4, SumOfTwoIntegers.getSum(9, -5));
        assertEquals(12, SumOfTwoIntegers.getSum(-11, 23));
        assertEquals(12, SumOfTwoIntegers.getSum(23, 11));
    }

    @Test
    void bitIntegerRange() {
        assertEquals(-1, SumOfTwoIntegers.getSum(-2147483648, 2147483647));
        assertEquals(-1, SumOfTwoIntegers.getSum(2147483647, -2147483648));
    }

}
