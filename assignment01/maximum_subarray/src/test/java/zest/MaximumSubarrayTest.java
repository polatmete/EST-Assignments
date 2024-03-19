package zest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MaximumSubarrayTest {
    @Test
    void nullArray() {
        assertEquals(0, MaximumSubarray.maxSubArray(null));
    }

    @Test
    void emptyArray() {
        assertEquals(0, MaximumSubarray.maxSubArray(new int[]{}));
    }

    @Test
    void oneElement() {
        assertEquals(1, MaximumSubarray.maxSubArray(new int[]{1}));
    }

    @Test
    void multipleElements() {
        int[] a1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int[] a2 = {5, 4, -1, 7, 8};
        assertEquals(6, MaximumSubarray.maxSubArray(a1));
        assertEquals(23, MaximumSubarray.maxSubArray(a2));
    }
}