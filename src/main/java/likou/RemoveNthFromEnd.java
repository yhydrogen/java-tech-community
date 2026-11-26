package likou;

public class RemoveNthFromEnd {
    public static void main(String[] args) {
        ListNode node = builder(1, 2, 3, 4, 5);
        node = removeNthFromEnd(node, 2);
        for (ListNode listNode = node; listNode != null; listNode = listNode.next)
            System.out.println(listNode.val);
    }
    public static ListNode builder(int... vals){
        ListNode dummy = new ListNode(0);
        ListNode temp = dummy;
        for (int val : vals){
            ListNode node = new ListNode(val);
            temp.next = node;
            temp = temp.next;
        }
        return dummy.next;
    }
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode fast = dummy, slow = dummy;
        for (int i = 0; i < n; i++) {
            fast = fast.next;
        }
        while (fast.next != null){
            fast = fast.next;
            slow = slow.next;
        }
        slow.next = slow.next.next;
        return dummy.next;
    }
}
