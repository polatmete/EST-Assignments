package zest;

import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        assertEquals(12, SumOfTwoIntegers.getSum(23, -11));
    }

    @Test
    void bitIntegerRange() {
        assertEquals(-1, SumOfTwoIntegers.getSum(Integer.MIN_VALUE, Integer.MAX_VALUE));
        assertEquals(-1, SumOfTwoIntegers.getSum(Integer.MAX_VALUE, Integer.MIN_VALUE));
    }

    @Test
    void bitIntegerOutOfRange() {
        assertThrows(ArithmeticException.class, () -> SumOfTwoIntegers.getSum(Integer.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> SumOfTwoIntegers.getSum(1, Integer.MAX_VALUE));
        assertThrows(ArithmeticException.class, () -> SumOfTwoIntegers.getSum(Integer.MAX_VALUE, Integer.MAX_VALUE));
        assertThrows(ArithmeticException.class, () -> SumOfTwoIntegers.getSum(Integer.MIN_VALUE, -1));
        assertThrows(ArithmeticException.class, () -> SumOfTwoIntegers.getSum(-1, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> SumOfTwoIntegers.getSum(Integer.MIN_VALUE, Integer.MIN_VALUE));
    }

    @Property
    void validRange(
            @ForAll
            @IntRange(min = Integer.MIN_VALUE / 2, max = Integer.MAX_VALUE / 2) int a,
            @ForAll
            @IntRange(min = Integer.MIN_VALUE / 2, max = Integer.MAX_VALUE / 2) int b
    ) {
        assertEquals(a + b, SumOfTwoIntegers.getSum(a, b));
    }

    @Property
    void invalidPositiveRange(
            @ForAll("invalidPositiveRange") int a,
            @ForAll("invalidPositiveRange") int b
    ) {
        assertThrows(ArithmeticException.class, () -> SumOfTwoIntegers.getSum(a, b));
    }

    @Property
    void invalidNegativeRange(
            @ForAll("invalidNegativeRange") int a,
            @ForAll("invalidNegativeRange") int b
    ) {
        assertThrows(ArithmeticException.class, () -> SumOfTwoIntegers.getSum(a, b));
    }

    @Provide
    private Arbitrary<Integer> invalidPositiveRange() {
        return Arbitraries.integers().greaterOrEqual(Integer.MAX_VALUE / 2 + 1);
    }

    @Provide
    private Arbitrary<Integer> invalidNegativeRange() {
        return Arbitraries.integers().lessOrEqual(Integer.MIN_VALUE / 2 - 1);
    }
}
