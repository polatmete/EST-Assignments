package zest;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.FloatRange;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Size;
import net.jqwik.api.constraints.UniqueElements;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

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
        assertThrows(AssertionError.class, () -> {
            findDuplicate(null);
        });
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



}