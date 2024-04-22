package zest;

import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import com.google.common.math.BigIntegerMath;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UniquePathsTest {
    @Test
    void exampleInput() {
        assertEquals(BigInteger.valueOf(28), UniquePaths.uniquePaths(3, 7));
        assertEquals(BigInteger.valueOf(35), UniquePaths.uniquePaths(5, 4));
        assertEquals(BigInteger.valueOf(3), UniquePaths.uniquePaths(2, 3));
        assertEquals(BigInteger.valueOf(6), UniquePaths.uniquePaths(3, 3));
    }

    @Test
    void onePathOnly() {
        assertEquals(BigInteger.ONE, UniquePaths.uniquePaths(1, 100));
        assertEquals(BigInteger.ONE, UniquePaths.uniquePaths(100, 1));
        assertEquals(BigInteger.ONE, UniquePaths.uniquePaths(1, 1));
    }

    @Test
    void maxInput() {
        int m = 100;
        int n = 100;
        assertEquals(BigIntegerMath.binomial(m + n - 2, m - 1), UniquePaths.uniquePaths(m, n));
    }

    @Test
    void zeroInput() {
        assertEquals(BigInteger.valueOf(-1), UniquePaths.uniquePaths(0, 5));
        assertEquals(BigInteger.valueOf(-1), UniquePaths.uniquePaths(5, 0));
    }

    @Test
    void negativeInput() {
        assertEquals(BigInteger.valueOf(-1), UniquePaths.uniquePaths(-2, 5));
        assertEquals(BigInteger.valueOf(-1), UniquePaths.uniquePaths(5, -2));
        assertEquals(BigInteger.valueOf(-1), UniquePaths.uniquePaths(-2, -2));
    }

    @Test
    void greaterInput() {
        assertEquals(BigInteger.valueOf(-1), UniquePaths.uniquePaths(5, 101));
        assertEquals(BigInteger.valueOf(-1), UniquePaths.uniquePaths(101, 5));
    }

    @Property
    void validInput(
            @ForAll
            @IntRange(min = 1, max = 100) int m,
            @ForAll
            @IntRange(min = 1, max = 100) int n
    ) {
        assertEquals(BigIntegerMath.binomial(m + n - 2, m - 1), UniquePaths.uniquePaths(m, n));
    }

    @Property
    void invalidInput(
            @ForAll("invalidInput") int m,
            @ForAll("invalidInput") int n
    ) {
        assertEquals(BigInteger.valueOf(-1), UniquePaths.uniquePaths(m, n));
    }

    @Provide
    private Arbitrary<Integer> invalidInput() {
        return Arbitraries.oneOf(
                Arbitraries.integers().lessOrEqual(0),
                Arbitraries.integers().greaterOrEqual(101)
        );
    }
}