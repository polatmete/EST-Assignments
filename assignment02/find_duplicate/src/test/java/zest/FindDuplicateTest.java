package zest;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static zest.FindDuplicate.findDuplicate;

class FindDuplicateTest {

    //code coverage
    @Test
    void oneDuplicate() {
        int[] nums = {1, 3, 4, 2, 2};
        int result = findDuplicate(nums);
        assertEquals(result, 2);
    }

    @Test
    void hasSameNumberMoreThanTwice() {
        int[] nums = {1, 3, 4, 2, 2};
        int result = findDuplicate(nums);
        assertEquals(result, 2);
    }

    @Test
    void multipleDuplicates() {
        int[] nums = {1, 3, 4, 2, 2, 3};
        int result = findDuplicate(nums);
        assertEquals(result, 2);

        int[] nums2 = {1, 1, 2, 2};
        int result2 = findDuplicate(nums2);
        assertEquals(result2, 1);
    }

    //test pre- and post-conditions
    @Test
    void nullArray() {
        assertThrows(AssertionError.class, () -> findDuplicate(null));
    }

    @Test
    void oneElementArray() {
        assertThrows(AssertionError.class, () -> {
            int[] nums = {1};
            findDuplicate(nums);
        });
    }

    @Test
    void elementOutOfRange() {
        assertThrows(AssertionError.class, () -> {
            int[] nums = {1, 3, 4, 2, 2, 7};
            findDuplicate(nums);
        });
        assertThrows(AssertionError.class, () -> {
            int[] nums = {0, 3, 4, 2, 2, 3};
            findDuplicate(nums);
        });
        assertThrows(AssertionError.class, () -> {
            int[] nums = {-1, 3, 4, 2, 2, 3};
            findDuplicate(nums);
        });
    }

    //property based testing
    @Property
    void propertyBasedTestOneDuplicate(
            @ForAll @IntRange(max = 8) int idxDuplicate,
            @ForAll @IntRange(max = 9) int idxToInsertDuplicate
    ) {
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        int duplicate = nums[idxDuplicate % nums.length];

        //replace 10 as it is bigger than n
        nums[nums.length - 1] = duplicate;

        if (idxDuplicate == idxToInsertDuplicate) {
            idxToInsertDuplicate = (idxToInsertDuplicate + 1) % nums.length;
        }
        nums[idxToInsertDuplicate] = duplicate;

        int result = findDuplicate(nums);
        assertEquals(result, duplicate);
    }

    @Property
    void propertyBasedTestMultipleDuplicates(
            @ForAll @IntRange(min = 0, max = 4) int idxDuplicate,
            @ForAll @IntRange(min = 0, max = 4) int idxToInsertDuplicate,
            @ForAll @IntRange(min = 5, max = 8) int idxDuplicate2,
            @ForAll @IntRange(min = 5, max = 8) int idxToInsertDuplicate2
    ) {
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

        int duplicate = nums[idxDuplicate];
        int duplicate2 = nums[idxDuplicate2];

        //replace 10 & 11 as it is bigger than n
        nums[nums.length - 2] = duplicate;
        nums[nums.length - 1] = duplicate2;

        nums[idxToInsertDuplicate2] = duplicate2;

        if (idxDuplicate == idxToInsertDuplicate) {
            idxToInsertDuplicate = (idxToInsertDuplicate + 1) % nums.length;
        }
        nums[idxToInsertDuplicate] = duplicate;


        int result = findDuplicate(nums);
        assertTrue(result == duplicate || result == duplicate2);
    }
}