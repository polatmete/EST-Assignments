package zest;

public class ClimbingStairs {
    public static long climbStairs(int n) {
        // Pre-condition: n must be positive
        if (n <= 0) {
            throw new IllegalArgumentException("Input value must be positive.");
        }

        // Pre-condition: return value must be non-negative, which equals to input value
        // must be below xxx
        if (n > 91) {
            throw new IllegalArgumentException("Input value is to high for the return to be stored on a 'long'.");
        }

        if (n <= 2) {
            return n;
        }
        long oneStepBefore = 2;
        long twoStepsBefore = 1;
        long allWays = 0;

        for (int i = 2; i < n; i++) {
            allWays = oneStepBefore + twoStepsBefore;
            twoStepsBefore = oneStepBefore;
            oneStepBefore = allWays;
        }
        return allWays;
    }
}
