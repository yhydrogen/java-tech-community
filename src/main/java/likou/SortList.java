package likou;

public class SortList {
    public static void main(String[] args) {
        ListNode listNode = buildList(1, 4, 7, 2, 4, 6, 8, 10, 3);
        ListNode sorted = sortList(listNode);
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
    public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(0);
        ListNode head = dummy;
        while (list1 != null && list2 != null){
            if (list1.val >= list2.val){
                head.next = list2;
                list2 = list2.next;
            }
            else {
                head.next = list1;
                list1 = list1.next;
            }
            head = head.next;
        }
        head.next = list1 != null ? list1 : list2;
        return dummy.next;
    }
    public static ListNode sortList(ListNode head){
        return sort(head, null);
    }
    public static ListNode sort(ListNode head, ListNode tail) {
        if (head == tail) return null;
        if (head.next == tail){
            head.next = null;
            return head;
        }
        ListNode fast = head, slow = head;
        while (fast != tail && fast.next != tail){
            fast = fast.next.next;
            slow = slow.next;
        }
        ListNode left = sort(head, slow), right = sort(slow, tail);
        ListNode listNode = mergeTwoLists(left, right);
        return listNode;
    }
}
