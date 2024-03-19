package zest;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CombinationSumTest {
    @Test
    void nullCandidates() {
        int[] candidates = null;
        int target = 4;
        List<List<Integer>> expected = Collections.emptyList();
        List<List<Integer>> result = CombinationSum.combinationSum(candidates, target);
        assertEquals(expected, result);
    }

    @Test
    void emptyCandidates() {
        int[] candidates = {};
        int target = 4;
        List<List<Integer>> expected = Collections.emptyList();
        List<List<Integer>> result = CombinationSum.combinationSum(candidates, target);
        assertEquals(expected, result);
    }

    @Test
    void oneCandidateWithResult() {
        int[] candidates = {2};
        int target = 4;
        List<List<Integer>> expected = Collections.singletonList(Arrays.asList(2, 2));
        List<List<Integer>> result = CombinationSum.combinationSum(candidates, target);
        assertEquals(expected, result);
    }
    @Test
    void oneCandidateWithoutResult() {
        int[] candidates = {2};
        int target = 5;
        List<List<Integer>> expected = Collections.emptyList();
        List<List<Integer>> result = CombinationSum.combinationSum(candidates, target);
        assertEquals(expected, result);
    }
    @Test
    void multipleCandidates() {
        int[] candidates = {1, 2, 3};
        int target = 4;
        List<List<Integer>> expected = Arrays.asList(Arrays.asList(1, 1, 1, 1), Arrays.asList(1, 1, 2), Arrays.asList(2, 2), Arrays.asList(1, 3));
        List<List<Integer>> result = CombinationSum.combinationSum(candidates, target);
        assertTrue(expected.size() == result.size() && expected.containsAll(result) && result.containsAll(expected));
    }

    @Test
    void negativeCandidatesPositiveTarget() {
        int[] candidates = {-1, -2, -3};
        int target = 4;
        List<List<Integer>> expected = Collections.emptyList();
        List<List<Integer>> result = CombinationSum.combinationSum(candidates, target);
        assertTrue(result.size() == 0 && expected.containsAll(result) && result.containsAll(expected));
    }

    @Test
    void negativeCandidatesNegativeTarget() {
        int[] candidates = {-1, -2, -3};
        int target = -4;
        List<List<Integer>> expected = Arrays.asList(Arrays.asList(-1, -1, -1, -1), Arrays.asList(-1, -1, -2), Arrays.asList(-2, -2), Arrays.asList(-1, -3));
        List<List<Integer>> result = CombinationSum.combinationSum(candidates, target);
        assertTrue(expected.size() == result.size() && expected.containsAll(result) && result.containsAll(expected));
    }

    @Test
    void zeroCandidateDifferentTarget() {
        int[] candidates = {0, 1, 2, 3};
        int target = 4;
        List<List<Integer>> expected = Arrays.asList(Arrays.asList(1, 1, 1, 1), Arrays.asList(1, 1, 2), Arrays.asList(2, 2), Arrays.asList(1, 3));
        List<List<Integer>> result = CombinationSum.combinationSum(candidates, target);
        assertTrue(expected.size() == result.size() && expected.containsAll(result) && result.containsAll(expected));
    }

    @Test
    void onlyZeroCandidate() {
        int[] candidates = {0};
        int target = 2;
        List<List<Integer>> expected = Collections.emptyList();
        List<List<Integer>> result = CombinationSum.combinationSum(candidates, target);
        assertTrue(result.size() == 0 && expected.containsAll(result) && result.containsAll(expected));
    }

    @Test
    void positiveCandidatesNegativeTarget() {
        int[] candidates = {1, 2, 3};
        int target = -4;
        List<List<Integer>> expected = Collections.emptyList();
        List<List<Integer>> result = CombinationSum.combinationSum(candidates, target);
        assertTrue(result.size() == 0 && expected.containsAll(result) && result.containsAll(expected));
    }

    @Test
    void negativeAndPositiveCandidates() {
        int[] candidates = {-2, 1, 3};
        int target = 3;
        List<List<Integer>> expected = Collections.emptyList();
        List<List<Integer>> result = CombinationSum.combinationSum(candidates, target);
        assertTrue(result.size() == 0 && expected.containsAll(result) && result.containsAll(expected));
    }

    @Test
    void tooManyPositiveResults() {
        int[] candidates = {1, 2, 3};
        int target = 1000;
        List<List<Integer>> result = CombinationSum.combinationSum(candidates, target);
        assertTrue(result.size() <= 150);
    }

    @Test
    void tooManyNegativeResults() {
        int[] candidates = {-1, -2, -3};
        int target = -1000;
        List<List<Integer>> result = CombinationSum.combinationSum(candidates, target);
        assertTrue(result.size() <= 150);
    }

    @Test
    void zeroTargetPositiveCandidates() {
        int[] candidates = {1, 2, 3};
        int target = 0;
        List<List<Integer>> expected = Collections.singletonList(Collections.emptyList());
        List<List<Integer>> result = CombinationSum.combinationSum(candidates, target);
        assertTrue(expected.size() == result.size() && expected.containsAll(result) && result.containsAll(expected));
    }
}