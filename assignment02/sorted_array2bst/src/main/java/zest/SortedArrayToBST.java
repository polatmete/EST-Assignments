package zest;

import java.util.*;

class TreeNode {
    int val;
    zest.TreeNode left;
    zest.TreeNode right;

    TreeNode(int x) {
        val = x;
    }
}

public class SortedArrayToBST {

    public zest.TreeNode sortedArrayToBST(int[] nums) {
        precondition(nums);
        TreeNode tree = constructBSTRecursive(nums, 0, nums.length - 1);
        postcondition(nums, tree);

        return tree;
    }

    private zest.TreeNode constructBSTRecursive(int[] nums, int left, int right) {
        if (left > right) {
            return null;
        }

        int mid = left + (right - left) / 2;
        zest.TreeNode node = new zest.TreeNode(nums[mid]);
        node.left = constructBSTRecursive(nums, left, mid - 1);
        node.right = constructBSTRecursive(nums, mid + 1, right);

        invariant(node);

        return node;
    }

    public List<Integer> levelOrderTraversal(zest.TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Queue<zest.TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            zest.TreeNode current = queue.poll();
            result.add(current.val);

            if (current.left != null) {
                queue.offer(current.left);
            }
            if (current.right != null) {
                queue.offer(current.right);
            }
        }

        return result;
    }

    private void precondition(int[] nums) {
        Set<Integer> hashSet = new HashSet<>();

        assert nums != null : "Input can't be null!";
        //assert nums.length >= 0 : "nums must be greater than 0"; --> An array is always greater than 0
        assert nums.length <= 10000 : "nums must be smaller or equal to (10^4)";

        for (int i = 0; i < nums.length - 1; i++) {
            assert nums[i] <= nums[i + 1] : "Input must be in ascending order";
            if (!hashSet.add(nums[i])) throw new AssertionError("Input must contain unique integers");
        }
        if (nums.length > 0 && !hashSet.add(nums[nums.length - 1]))
            throw new AssertionError("Input must contain unique integers");
    }

    void postcondition(int[] nums, TreeNode resultBSTTree) {
        assert nums.length == levelOrderTraversal(resultBSTTree).size() : "Input and resulting tree must have the same size";
    }

    void invariant(zest.TreeNode node) {
        assert node.left == null || node.left.val < node.val : "Left child is larger than parent";
        assert node.right == null || node.right.val > node.val : "Right child is smaller or equal to parent";
        assert Math.abs(getDepth(node.right) - getDepth(node.left)) <= 1 : "Tree is not balanced";
    }

    int getDepth(TreeNode node) {
        if (node == null) return 0;
        return 1 + Math.max(getDepth(node.left), getDepth(node.right));
    }
}
