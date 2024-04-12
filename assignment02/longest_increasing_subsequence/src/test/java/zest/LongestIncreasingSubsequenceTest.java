package zest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LongestIncreasingSubsequenceTest {

    // code coverage
    @Test
    public void happyCase(){
        int[] nums = {1,3,4,2,2,3};
        int result = LongestIncreasingSubsequence.lengthOfLIS(nums);
        assertEquals(result, 3);
    }

    @Test
    public void uniqueNumbers(){
        int[] nums = {1,1,1,1,1,1,1};
        int result = LongestIncreasingSubsequence.lengthOfLIS(nums);
        assertEquals(result, 1);
    }

    @Test
    public void decreasingNumbers(){
        int[] nums = {6,5,4,3,2,1};
        int result = LongestIncreasingSubsequence.lengthOfLIS(nums);
        assertEquals(result, 1);
    }

    @Test
    public void singleElementArray(){
        int[] nums = {1};
        int result = LongestIncreasingSubsequence.lengthOfLIS(nums);
        assertEquals(result, 1);
    }

    // test pre- and post-conditions
    @Test
    public void nullArray(){
        int[] nums = null;
        int result = LongestIncreasingSubsequence.lengthOfLIS(nums);
        assertEquals(result, 0);
    }

    @Test
    public void emptyArray(){
        int[] nums = {};
        int result = LongestIncreasingSubsequence.lengthOfLIS(nums);
        assertEquals(result, 0);
    }



    // property-based testing





 
}