package zest;

import java.math.BigInteger;

public class UniquePaths {
    public static BigInteger uniquePaths(int m, int n) {
        if (m > 100 || m <= 0 || n > 100 || n <= 0) {
            return BigInteger.valueOf(-1);
        }

        BigInteger[][] dp = new BigInteger[m][n];

        // Initialize the first row and column to 1 since there's only one way to reach any cell in the first row or column
        for (int i = 0; i < m; i++) {
            dp[i][0] = BigInteger.ONE;
        }
        for (int j = 0; j < n; j++) {
            dp[0][j] = BigInteger.ONE;
        }

        // Fill the DP table
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j].add(dp[i][j - 1]); // The number of paths to the current cell is the sum of the paths to the cell above and to the left
            }
        }

        return dp[m - 1][n - 1]; // The bottom-right cell contains the total number of unique paths
    }
}
