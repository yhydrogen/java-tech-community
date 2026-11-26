package likou;

public class HasCycle {
    public static void main(String[] args) {
        ListNode head = buildList(1, 2, 3, 4, 5);
        ListNode tail = head.next.next.next;
        tail.next = head.next;
        System.out.println(hasCycle(head));
    }
    public static ListNode buildList(int... values){
        ListNode dummy = new ListNode(0);
        ListNode temp = dummy;
        for (int value : values){
            temp.next = new ListNode(value);
            temp = temp.next;
        }
        return dummy.next;
    }
    public static boolean hasCycle(ListNode head){
        if(head == null || head.next == null) return false;
        ListNode fast = head, slow = head;
        while (fast.next != null && fast.next.next != null){
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) return true;
        }
        return false;
    }
}
