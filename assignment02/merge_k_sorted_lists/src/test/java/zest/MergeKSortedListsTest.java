package zest;

import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MergeKSortedListsTest {
    // Task 1: Code Coverage
    @Test
    void testNull() {
        assertNull(new MergeKSortedLists().mergeKLists(null));
    }

    @Test
    void testEmptyList() {
        assertNull(new MergeKSortedLists().mergeKLists(new ListNode[0]));
    }

    @Test
    void testEmptyNode() {
        ListNode res = new MergeKSortedLists().mergeKLists(new ListNode[]{new ListNode()});

        assertEquals(0, res.val);
        assertNull(res.next);
    }

    @Test
    void testExample() {
        ListNode listNode1 = new ListNode(1);
        ListNode listNode2 = new ListNode(2);
        ListNode listNode3 = new ListNode(3);
        ListNode listNode4 = new ListNode(4);
        ListNode listNode5 = new ListNode(5);

        ListNode[] lists = new ListNode[]{new ListNode(1, new ListNode(4, listNode5)), new ListNode(2, new ListNode(2, listNode2)), new ListNode(3, listNode4), null};

        ListNode result = new MergeKSortedLists().mergeKLists(lists);

        assertEquals(1, result.val);
        assertEquals(2, result.next.val);
        assertEquals(2, result.next.next.val);
        assertEquals(2, result.next.next.next.val);
        assertEquals(3, result.next.next.next.next.val);
        assertEquals(4, result.next.next.next.next.next.val);
        assertEquals(4, result.next.next.next.next.next.next.val);
        assertEquals(5, result.next.next.next.next.next.next.next.val);
        assertNull(result.next.next.next.next.next.next.next.next);
    }


    // Task 3: Testing Contracts
    @Test
    void testPrecondition() {
        ListNode list = new ListNode(0);
        ListNode cur = list;
        for (int i = 0; i < 10001; ++i) {
            cur.next = new ListNode(0);
            cur = cur.next;
        }

        assertThrows(AssertionError.class, () -> new MergeKSortedLists().mergeKLists(new ListNode[]{new ListNode(10001, new ListNode(10002))}));
        assertThrows(AssertionError.class, () -> new MergeKSortedLists().mergeKLists(new ListNode[]{new ListNode(0, new ListNode(10001))}));
        assertThrows(AssertionError.class, () -> new MergeKSortedLists().mergeKLists(new ListNode[]{new ListNode(-10002, new ListNode(-10001))}));
        assertThrows(AssertionError.class, () -> new MergeKSortedLists().mergeKLists(new ListNode[]{list}));
        assertThrows(AssertionError.class, () -> new MergeKSortedLists().mergeKLists(new ListNode[]{new ListNode(0, new ListNode(-10001))}));
    }

    // Task 4: Property-Based Testing
    @Property
    void testValidList(@ForAll("validList") ListNode[] validLists) {
        ListNode result = new MergeKSortedLists().mergeKLists(validLists);

        assertTrue(result.val <= result.next.val);
    }

    @Property
    void testInvalidList(@ForAll("invalidList") ListNode[] invalidLists) {
        assertThrows(AssertionError.class, () -> new MergeKSortedLists().mergeKLists(invalidLists));
    }

    @Property
    void testRandomLists(@ForAll @IntRange(min = 0, max = 10000) int n) {
        ListNode res = new MergeKSortedLists().mergeKLists(Stream.generate(() -> new ListNode(1)).limit(n).toArray(ListNode[]::new));

        int count = 0;
        ListNode cur = res;
        while (cur != null) {
            ++count;
            cur = cur.next;
        }

        assertEquals(n, count);
    }

    @Provide
    Arbitrary<ListNode[]> validList() {
        Arbitrary<Integer> node1 = Arbitraries.integers().between(-10000, 10000);
        Arbitrary<Integer> node2 = Arbitraries.integers().between(-10000, 10000);

        return Combinators.combine(node1, node2).as((n1, n2) -> new ListNode[]{new ListNode(n1), new ListNode(n2)});
    }

    @Provide
    Arbitrary<ListNode[]> invalidList() {
        Arbitrary<Integer> node1 = Arbitraries.integers().between(-10000, 10000);
        Arbitrary<Integer> node2 = Arbitraries.integers().lessOrEqual(-10001);
        Arbitrary<Integer> node3 = Arbitraries.integers().greaterOrEqual(10001);

        return Combinators.combine(node1, Arbitraries.oneOf(node2, node3)).as((n1, n2) -> new ListNode[]{new ListNode(n1, new ListNode(n2))});
    }
}