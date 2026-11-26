package likou;

public class DetectCycle {
    public static void main(String[] args) {
        ListNode head = buildList(1, 2, 3, 4);
        ListNode tail = head.next.next.next;
        tail.next = head.next;
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
    public ListNode detectCycle(ListNode head) {
        if(head == null || head.next == null) return null;
        ListNode fast = head, slow = head;
        while (fast.next != null && fast.next.next != null){
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                ListNode ans = head;
                while (ans != slow){
                    ans = ans.next;
                    slow = slow.next;
                }
                return ans;
            }
        }
        return null;
    }
}
