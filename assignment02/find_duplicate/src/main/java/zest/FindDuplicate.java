package zest;

public class FindDuplicate {

    public static int findDuplicate( int[] nums) {

        //pre-conditions 
        assert nums != null : "array cannot be null";
        assert nums.length > 1 : "array needs to have at least two elements";
        int n = nums.length -1;
        for (int num : nums) {
            assert num >= 1 && num <= n : "All elements in the array must be in the range [1, n]";
        }

        //implementation
        int tortoise = nums[0];
        int hare = nums[0];
        // Phase 1: Finding the intersection point of the two runners.
        do {
            tortoise = nums[tortoise];
            hare = nums[nums[hare]];
        } while (tortoise != hare);

        // Phase 2: Finding the "entrance" to the cycle.
        tortoise = nums[0];
        while (tortoise != hare) {
            tortoise = nums[tortoise];
            hare = nums[hare];
        }

        return hare;
    }
}
