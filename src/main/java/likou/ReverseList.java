package likou;

public class ReverseList {
    public static void main(String[] args) {
        ListNode listNode = buildList(1, 2, 3, 4, 5);
        ListNode reverseList = reverseList(listNode);
        for (ListNode listNode1 = reverseList; listNode1 != null; listNode1 = listNode1.next){
            System.out.println(listNode1.val + " ");
        }
    }
    public static ListNode buildList(int... values) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        for (int value : values) {
            tail.next = new ListNode(value);
            tail = tail.next;
        }
        return dummy.next;
    }
    public static ListNode reverseList(ListNode head) {
        ListNode pre = null;
        ListNode cur = head;
        while (cur != null) {
            ListNode temp = cur.next; // 先保存下一个
            cur.next = pre;           // 当前节点指向前一个
            pre = cur;
            cur = temp;
        }
        return pre;
    }
}
