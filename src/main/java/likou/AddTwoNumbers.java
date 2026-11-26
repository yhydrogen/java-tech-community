package likou;

public class AddTwoNumbers {
    public static void main(String[] args) {
        ListNode listNode1 = buildList(1, 2, 3, 4, 5);
        ListNode listNode2 = buildList(1, 2, 3, 4, 5);
        ListNode listNode = addTwoNumbers(listNode1, listNode2);
        ListNode temp = listNode;
        while (temp != null){
            System.out.println(temp.val);
            temp = temp.next;
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
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode temp =dummy;
        int cur = 0, carry = 0;
        while (l1 != null && l2 != null){
            cur = (l1.val + l2.val +carry) % 10;
            carry = (l1.val + l2.val +carry) / 10;
            temp.next = new ListNode(cur);
            temp = temp.next;
            l1 = l1.next;
            l2 = l2.next;
        }
        while (l1 != null){
            cur = (l1.val +carry) % 10;
            carry = (l1.val +carry) / 10;
            temp.next = new ListNode(cur);
            temp = temp.next;
            l1 = l1.next;
        }
        while (l2 != null){
            cur = (l2.val +carry) % 10;
            carry = (l2.val +carry) / 10;
            temp.next = new ListNode(cur);
            temp = temp.next;
            l2 = l2.next;
        }
        if (carry != 0){
            temp.next = new ListNode(carry);
        }
        return dummy.next;
    }
}
