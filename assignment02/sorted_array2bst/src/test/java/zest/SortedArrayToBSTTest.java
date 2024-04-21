package zest;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


class SortedArrayToBSTTest {
    private static int[] createArray(int size) {
        int[] nums = new int[size];
        for (int i = 0; i < nums.length; i++) nums[i] = i;
        return nums;
    }

    @Test
    void testNull() {
        assertThrows(AssertionError.class, () -> new SortedArrayToBST().sortedArrayToBST(null));
        assertEquals(new ArrayList<>(), new SortedArrayToBST().levelOrderTraversal(null));
    }

    @Test
    void testEmptyList() {
        TreeNode resTree = new SortedArrayToBST().sortedArrayToBST(new int[]{});
        List<Integer> resList = new SortedArrayToBST().levelOrderTraversal(new SortedArrayToBST().sortedArrayToBST(new int[]{}));

        assertNull(resTree);
        assertEquals(0, resList.size());
    }

    @Test
    void testOne() {
        List<Integer> resList = new SortedArrayToBST().levelOrderTraversal(new SortedArrayToBST().sortedArrayToBST(new int[]{1}));

        assertEquals(1, resList.size());
        assertEquals(1, resList.get(0));
    }

    @Test
    void testExample() {
        TreeNode resTree = new SortedArrayToBST().sortedArrayToBST(new int[]{1, 2, 3, 4, 5});
        List<Integer> resList = new SortedArrayToBST().levelOrderTraversal(resTree);
        assertEquals(3, resList.get(0));
    }

    @Test
    void testMaxSize() {
        int[] nums = createArray(10000);
        TreeNode treeResult = new SortedArrayToBST().sortedArrayToBST(nums);
        List<Integer> listResult = new SortedArrayToBST().levelOrderTraversal(treeResult);

        assertEquals(10000, listResult.size());
    }

    @Test
    void testTooLong() {
        assertThrows(AssertionError.class, () -> new SortedArrayToBST().sortedArrayToBST(createArray(10001)));
    }

    @Test
    void testNotSorted() {
        assertThrows(AssertionError.class, () -> new SortedArrayToBST().sortedArrayToBST(new int[]{1, 2, 3, 5, 4}));
    }

    @Test
    void testNotUnique() {
        assertThrows(AssertionError.class, () -> new SortedArrayToBST().sortedArrayToBST(new int[]{1, 2, 3, 3, 4}));
        assertThrows(AssertionError.class, () -> new SortedArrayToBST().sortedArrayToBST(new int[]{1, 2, 3, 4, 4}));
    }

    @Test
    void testPostcondition() {
        TreeNode n1 = new TreeNode(2);
        n1.left = new TreeNode(1);
        n1.right = new TreeNode(3);

        assertThrows(AssertionError.class, () -> new SortedArrayToBST().postcondition(new int[]{1, 2, 3, 4, 5}, n1));
    }

    @Test
    void testInvariant() {
        TreeNode n1 = new TreeNode(2);
        n1.left = new TreeNode(1);
        n1.right = new TreeNode(3);

        TreeNode n2 = new TreeNode(2);
        n2.left = new TreeNode(3);
        n2.right = new TreeNode(1);

        TreeNode n3 = new TreeNode(3);
        n3.left = new TreeNode(2);
        n3.right = new TreeNode(1);

        assertDoesNotThrow(() -> new SortedArrayToBST().invariant(n1));
        assertThrows(AssertionError.class, () -> new SortedArrayToBST().invariant(n2));
        assertThrows(AssertionError.class, () -> new SortedArrayToBST().invariant(n3));
    }

    @Test
    void testInvariantWithUnbalancedTree() {
        TreeNode node = new TreeNode(2);
        node.left = new TreeNode(1);
        node.right = new TreeNode(3);
        node.right.right = new TreeNode(4);
        node.right.right.right = new TreeNode(5);

        assertThrows(AssertionError.class, () -> new SortedArrayToBST().invariant(node));
    }

    @Property
    void testValid(@ForAll("valid") int[] nums) {
        assertEquals(nums.length, new SortedArrayToBST().levelOrderTraversal(new SortedArrayToBST().sortedArrayToBST(nums)).size());
    }

    @Property(tries = 100)
    void testInvalid(@ForAll("invalid") int[] nums) {
        assertThrows(AssertionError.class, () -> new SortedArrayToBST().sortedArrayToBST(nums));
    }

    @Provide
    Arbitrary<int[]> valid() {
        return Arbitraries.integers().array(int[].class).ofMinSize(0).ofMaxSize(10000).map(integers -> {
            Set<Integer> uniqueSet = new HashSet<>();
            for (int i : integers) uniqueSet.add(i);
            int[] uniqueArray = uniqueSet.stream().mapToInt(Integer::intValue).toArray();
            Arrays.sort(uniqueArray);
            return uniqueArray;
        });
    }

    @Provide
    Arbitrary<int[]> invalid() {
        return Arbitraries.integers().array(int[].class).ofMinSize(10001).map(integers -> integers);
    }
}
