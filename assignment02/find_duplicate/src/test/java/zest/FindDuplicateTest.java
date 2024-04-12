package zest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static zest.FindDuplicate.findDuplicate;

class FindDuplicateTest {

    //code coverage
    @Test
    public void hasSameNumberTwice(){
        int[] nums = {1,3,4,2,2};
        int result = findDuplicate(nums);
        assertEquals(result, 2);
    }
    @Test
    public void hasSameNumberMoreThanTwice(){
        int[] nums = {1,3,4,2,2,3};
        int result = findDuplicate(nums);
        assertEquals(result, 2);

    }

    @Test
    public void onlyTwoElements(){
        int[] nums = {1,1};
        int result = findDuplicate(nums);
        assertEquals(result, 1);
    }

    //test pre-/post-conditions and invariants
    //TODO: implement



    //property based testing
    //TODO: implement
}