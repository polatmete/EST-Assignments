package zest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CombinationSum {
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();

        if (candidates == null) {
            return result;
        }

        // Logic for deleting 0's from the candidates
        int count = 0;
        for (int candidate : candidates) {
            if (candidate != 0) {
                count++;
            }
        }

        int[] sanitizedCandidates = new int[count];

        int index = 0;
        for (int candidate : candidates) {
            if (candidate != 0) {
                sanitizedCandidates[index++] = candidate;
            }
        }

        if (sanitizedCandidates.length == 0) {
            return result;
        }

        Arrays.sort(sanitizedCandidates);

        // Return if one the following combinations exists: negative AND positive candidates, negative candidates and
        // positive target, positive candidates and negative target
        if ((sanitizedCandidates[0] < 0 && sanitizedCandidates[sanitizedCandidates.length - 1] > 0) ||
                (sanitizedCandidates[sanitizedCandidates.length - 1] < 0 && target > 0) ||
                (sanitizedCandidates[0] > 0 && target < 0)) {
            return result;
        }


        if (target >= 0) {
            getResultForNonNegativeTarget(result, new ArrayList<>(), sanitizedCandidates, target, 0);
        } else {
            getResultForNegativeTarget(result, new ArrayList<>(), sanitizedCandidates, target, candidates.length - 1);
        }

        return result;
    }

    private static void getResultForNonNegativeTarget(List<List<Integer>> result, List<Integer> cur, int[] candidates, int target, int start) {
        if (result.size() < 150) {
            if (target > 0) {
                for (int i = start; i < candidates.length && target >= candidates[i]; i++) {
                    cur.add(candidates[i]);
                    getResultForNonNegativeTarget(result, cur, candidates, target - candidates[i], i);
                    cur.remove(cur.size() - 1);
                }
            } else if (target == 0) {
                result.add(new ArrayList<>(cur));
            }
        }
    }

    private static void getResultForNegativeTarget(List<List<Integer>> result, List<Integer> cur, int[] candidates, int target, int start) {
        if (result.size() < 150) {
            if (target < 0) {
                for (int i = start; i >= 0 && target <= candidates[i]; i--) {
                    cur.add(candidates[i]);
                    getResultForNegativeTarget(result, cur, candidates, target - candidates[i], i);
                    cur.remove(cur.size() - 1);
                }
            } else if (target == 0) {
                result.add(new ArrayList<>(cur));
            }
        }
    }
}
