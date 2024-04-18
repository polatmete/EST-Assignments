package zest;

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
        assertEquals(BigIntegerMath.binomial(100, 100), UniquePaths.uniquePaths(100, 100));
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
}