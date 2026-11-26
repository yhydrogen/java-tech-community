package likou;

public class SwapPairs {
    public static void main(String[] args) {
        ListNode listNode = buildList(1, 4, 7, 2, 4, 6, 8, 10, 3);
        ListNode sorted = swapPairs(listNode);
        for (ListNode i = sorted; i!= null; i = i.next){
            System.out.println(i.val);
        }
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
    public static ListNode swapPairs(ListNode head) {
        if(head == null || head.next == null) return head;
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode pre = dummy;
        while (pre.next != null && pre.next.next != null){
            ListNode a = pre.next, b = pre.next.next;
            ListNode next = b.next;
            b.next = a;
            a.next = next;
            pre.next = b;
            pre = pre.next.next;
        }
        return dummy.next;
    }
}
