package zest;

import net.jqwik.api.*;
import net.jqwik.api.constraints.Size;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LongestIncreasingSubsequenceTest {

    @Test
    void happyCase() {
        int[] nums = {1, 3, 4, 2, 2, 3};
        int result = LongestIncreasingSubsequence.lengthOfLIS(nums);
        assertEquals(result, 3);

        int[] nums2 = {-4, -3, 4, 2, 2, 3};
        int result2 = LongestIncreasingSubsequence.lengthOfLIS(nums2);
        assertEquals(result2, 4);
    }

    @Test
    void uniqueNumbers() {
        int[] nums = {1, 1, 1, 1, 1, 1, 1};
        int result = LongestIncreasingSubsequence.lengthOfLIS(nums);
        assertEquals(result, 1);
    }

    @Test
    void decreasingNumbers() {
        int[] nums = {6, 5, 4, 3, 2, 1};
        int result = LongestIncreasingSubsequence.lengthOfLIS(nums);
        assertEquals(result, 1);

        int[] numsNegative = {0, -1, -2, -3, -4, -5};
        int resultNegative = LongestIncreasingSubsequence.lengthOfLIS(numsNegative);
        assertEquals(resultNegative, 1);
    }

    @Test
    void singleElementArray() {
        int[] nums = {1};
        int result = LongestIncreasingSubsequence.lengthOfLIS(nums);
        assertEquals(result, 1);
    }

    @Test
    void nullArray() {
        int[] nums = null;
        int result = LongestIncreasingSubsequence.lengthOfLIS(nums);
        assertEquals(result, 0);
    }

    @Test
    void emptyArray() {
        int[] nums = {};
        int result = LongestIncreasingSubsequence.lengthOfLIS(nums);
        assertEquals(result, 0);
    }

    @Property
    void resultGreaterEqualOne(@ForAll @Size(min = 1) int[] nums) {
        int result = LongestIncreasingSubsequence.lengthOfLIS(nums);
        assertTrue(result >= 1);
    }

    @Property
    void propertyBasedTest(@ForAll("arrProvider") int[] nums) {
        int result = LongestIncreasingSubsequence.lengthOfLIS(nums);
        assertEquals(result, nums.length);
    }

    @Provide
    Arbitrary<int[]> arrProvider() {
        Arbitrary<Integer> integerArbitrary = Arbitraries.integers().between(-100, 100);
        return integerArbitrary
                .set().ofMinSize(0).ofMaxSize(30)
                .map(set -> {
                    int[] uniqueArray = set.stream().mapToInt(Integer::intValue).toArray();
                    Arrays.sort(uniqueArray);
                    return uniqueArray;
                });
    }
}