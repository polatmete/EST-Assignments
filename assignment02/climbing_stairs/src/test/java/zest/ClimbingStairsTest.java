package zest;


import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClimbingStairsTest {

    @Test
    void negative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                ClimbingStairs.climbStairs(-1));

        assertEquals("Input value must be positive.", exception.getMessage());
    }

    @Test
    void zero() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                ClimbingStairs.climbStairs(0));

        assertEquals("Input value must be positive.", exception.getMessage());
    }

    @Test
    void lowPositive() {
        assertEquals(2, ClimbingStairs.climbStairs(2));
    }

    @Test
    void largePositive() {
        assertEquals(5, ClimbingStairs.climbStairs(4));
    }

    @Property
    void validRange(
            @ForAll
            @IntRange(min = 1, max = 91) int n
    ) {
        long[] f = new long[n + 1];
        int i;

        f[0] = 1;

        if (n > 0) {
            f[1] = 1;
            for (i = 2; i <= n; i++) {
                f[i] = f[i - 1] + f[i - 2];
            }
        }
        assertEquals(f[n], ClimbingStairs.climbStairs(n));
    }

    @Property
    void invalidLowRange(
            @ForAll
            @IntRange(min = Integer.MIN_VALUE, max = 0) int n
    ) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                ClimbingStairs.climbStairs(n));
        assertEquals("Input value must be positive.", exception.getMessage());
    }

    @Property
    void invalidHighRange(
            @ForAll
            @IntRange(min = 92) int n
    ) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                ClimbingStairs.climbStairs(n));
        assertEquals("Input value is to high for the return to be stored on a 'long'.", exception.getMessage());
    }
}


