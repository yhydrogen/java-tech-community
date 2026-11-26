package likou;

public class MergeTwoLists {
    public static void main(String[] args) {
        ListNode head1 = buildList(1, 2, 3, 4, 5);
        ListNode head2 = buildList(1, 3, 5, 6, 7);
        ListNode node = mergeTwoLists(head1, head2);
        for (ListNode i = node; i!= null; i = i.next){
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
}
