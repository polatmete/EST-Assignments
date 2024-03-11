package zest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MedianOfArraysTest {
    private final MedianOfArrays medianOfArrays = new MedianOfArrays();

    @Test
    void nullArray() {
        int[] nums1 = {1, 2};
        assertEquals(0.0, medianOfArrays.findMedianSortedArrays(nums1, null));
    }

    @Test
    void unsortedArray() {
        int[] nums1 = {1, 2};
        int[] nums2 = {4, 3};
        assertEquals(0.0, medianOfArrays.findMedianSortedArrays(nums1, nums2));
    }

    @Test
    void bothArraysEmpty() {
        assertEquals(0.0, medianOfArrays.findMedianSortedArrays(new int[]{}, new int[]{}));
    }

    @Test
    void oneArrayEmpty() {
        int[] nums1 = {1, 2, 3};
        assertEquals(2.0, medianOfArrays.findMedianSortedArrays(nums1, new int[]{}));
    }

    @Test
    void oneElement() {
        int[] nums1 = {4};
        int[] nums2 = {2};
        assertEquals(3.0, medianOfArrays.findMedianSortedArrays(nums1, nums2));
    }

    @Test
    void multipleElements() {
        int[] nums1a = {1, 3};
        int[] nums2a = {2};
        int[] nums1b = {1, 2};
        int[] nums2b = {3, 4};
        assertEquals(2.0, medianOfArrays.findMedianSortedArrays(nums1a, nums2a));
        assertEquals(2.5, medianOfArrays.findMedianSortedArrays(nums1b, nums2b));
    }
}