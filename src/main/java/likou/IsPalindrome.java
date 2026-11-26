package likou;

public class IsPalindrome {
    public static void main(String[] args) {
        ListNode listNode = buildList(1, 2, 3, 4, 5);
        boolean palindrome = isPalindrome(listNode);
    }
    public static ListNode buildList(int... values) {
        ListNode dummy = new ListNode(0);
        ListNode temp = dummy;
        for (int value : values){
            temp.next = new ListNode(value);
            temp = temp.next;
        }
        return dummy.next;
    }
    public static boolean isPalindrome(ListNode head) {
        //if(head.next == null || head == null) return true;
        ListNode fast = head, slow = head;
        while (fast.next != null && fast.next.next != null){
            fast = fast.next.next;
            slow = slow.next;
        }
        ListNode half = slow.next;
        ListNode rehalf = reverseList(half);
        while (rehalf != null){
            if (rehalf.val != head.val){
                return false;
            }
            rehalf = rehalf.next;
            head = head.next;
        }
        return true;
    }
    public static ListNode reverseList(ListNode head){
        ListNode cur = head;
        ListNode pre = null;
        while (cur != null){
            ListNode temp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = temp;
        }
        return pre;
    }
}
