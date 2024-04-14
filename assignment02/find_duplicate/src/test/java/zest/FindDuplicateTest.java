package zest;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    //test pre- and post-conditions
    @Test
    public void nullArray(){
        assertThrows(AssertionError.class, () -> findDuplicate(null));
    }

    @Test
    public void oneElementArray(){
        assertThrows(AssertionError.class, () -> {
            int[] nums = {1};
            findDuplicate(nums);
        });
    }

    @Test
    public void elementOutOfRange(){
        assertThrows(AssertionError.class, () -> {
            int[] nums = {1,3,4,2,2,7};
            findDuplicate(nums);
        });
        assertThrows(AssertionError.class, () -> {
            int[] nums = {0,3,4,2,2,3};
            findDuplicate(nums);
        });
        assertThrows(AssertionError.class, () -> {
            int[] nums = {-1,3,4,2,2,3};
            findDuplicate(nums);
        });
    }

    //property based testing
    @Property
    void propertyBasedTest(
            @ForAll @IntRange(max = 8) int idxDuplicate,
            @ForAll @IntRange(max = 9) int idxToInsertDuplicate) {

        int[] nums = {1,2,3,4,5,6,7,8,9,10};

        int duplicate = nums[idxDuplicate % nums.length];

        //replace 10 as it is bigger than n
        nums[nums.length-1] = duplicate;

        if(idxDuplicate == idxToInsertDuplicate){
            idxToInsertDuplicate = (idxToInsertDuplicate + 1) % nums.length;
        }
        nums[idxToInsertDuplicate] = duplicate;

        int result = findDuplicate(nums);
        assertEquals(result, duplicate);

    }



}