package zest;

import java.util.PriorityQueue;

class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}

public class MergeKSortedLists {
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        precondition(lists);

        PriorityQueue<ListNode> queue = new PriorityQueue<>(lists.length, (a, b) -> a.val - b.val);

        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        invariant(tail);

        for (ListNode node : lists) {
            if (node != null) {
                queue.add(node);
            }
        }

        while (!queue.isEmpty()) {
            tail.next = queue.poll();
            tail = tail.next;

            if (tail.next != null) {
                queue.add(tail.next);
            }
            invariant(dummy.next);
        }

        invariant(tail);
        postcondition(dummy.next);
        return dummy.next;
    }


    private void precondition(ListNode[] lists) {
        // Check 0 <= size <= 10^4
        int count = 0;
        for (ListNode node : lists) {
            ListNode cur = node;
            while (cur != null) {
                ++count;
                cur = cur.next;
            }
        }
        // assert count >= 0: "Number of nodes less than 0.";  // Already checked in the first line of the method
        assert count <= 10000: "Number of nodes greater than 10^4.";

        // Check if linked-lists are sorted
        for (ListNode node : lists) {
            ListNode cur = node;
            if (cur == null) continue;
            while (cur.next != null) {
                assert cur.val <= cur.next.val : "Linked list is not sorted.";
                assert cur.val <= 10000: "Value greater than 10^4.";
                assert cur.val >= -10000: "Value less than -(10^4).";
                cur = cur.next;
            }
            assert cur.val <= 10000 : "Value greater than 10^4.";
            // assert cur.val >= -10000 : "Value less than -(10^4)."; --> Not possible because of code logic in while loop
        }
    }

    private void postcondition(ListNode head) {
        invariant(head);
    }

    private void invariant(ListNode head) {
        ListNode cur = head;
        while (cur.next != null) {
            assert cur.val <= cur.next.val : "Linked list is not sorted.";
            cur = cur.next;
        }
    }
}
