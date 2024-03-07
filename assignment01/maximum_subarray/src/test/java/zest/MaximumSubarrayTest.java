package zest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class MaximumSubarrayTest {
    @Test
    void emptyArray() {
        int[] a = new int[0];
        assertEquals(0, MaximumSubarray.maxSubArray(a));
    }
}