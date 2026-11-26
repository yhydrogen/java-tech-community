package likou;

import java.util.HashSet;
import java.util.Set;

public class GetIntersectionNode {
    public static void main(String[] args) {
        // 相交链表必须复用同一批节点，不能分别创建两个值相同的尾部。
        ListNode sharedTail = buildList(8, 4, 5);
        ListNode headA = appendTail(buildList(4, 1), sharedTail);
        ListNode headB = appendTail(buildList(5, 6, 1), sharedTail);
        checkCase("长度不同，共享尾部", headA, headB, sharedTail);

        checkCase("值相同但节点不同", buildList(1, 2, 3), buildList(1, 2, 3), null);

        ListNode sameHead = buildList(2, 4);
        checkCase("两个头节点相同", sameHead, sameHead, sameHead);

        ListNode lastNode = buildList(7);
        checkCase("只有最后一个节点相交",
                appendTail(buildList(1, 7), lastNode),
                appendTail(buildList(7, 2), lastNode), lastNode);

        ListNode shortList = buildList(6, 7);
        checkCase("一个头节点位于另一条链表中",
                shortList, appendTail(buildList(5), shortList), shortList);
        checkCase("A 为空", null, buildList(1, 2), null);
        checkCase("B 为空", buildList(1, 2), null, null);
        checkCase("两个链表都为空", null, null, null);
        System.out.println("全部 8 组测试通过");
    }

    // 可变参数：buildList(1, 2, 3) 创建 1 -> 2 -> 3；buildList() 返回 null。
    public static ListNode buildList(int... values) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        for (int value : values) {
            tail.next = new ListNode(value);
            tail = tail.next;
        }
        return dummy.next;
    }

    // 把现有尾部接到 head 末端；head 为空时直接返回 sharedTail。
    // 用于构造无环样例，sharedTail 不能包含 head 链表中已有的节点。
    public static ListNode appendTail(ListNode head, ListNode sharedTail) {
        if (head == null) {
            return sharedTail;
        }
        ListNode tail = head;
        while (tail.next != null) {
            tail = tail.next;
        }
        tail.next = sharedTail;
        return head;
    }

    private static String formatList(ListNode head) {
        StringBuilder result = new StringBuilder();
        for (ListNode node = head; node != null; node = node.next) {
            result.append(node.val).append(" -> ");
        }
        return result.append("null").toString();
    }

    private static void checkCase(String name, ListNode headA,
                                  ListNode headB, ListNode expected) {
        System.out.println("【" + name + "】");
        System.out.println("A: " + formatList(headA));
        System.out.println("B: " + formatList(headB));
        ListNode actual = getIntersectionNode(headA, headB);
        System.out.println("预期交点值: " + (expected == null ? "null" : expected.val)
                + "，实际交点值: " + (actual == null ? "null" : actual.val));
        // 比较节点引用，不能仅比较 val，否则可能把两个不同节点误判为交点。
        if (actual != expected) {
            throw new AssertionError(name + "：返回的不是预期节点对象");
        }
        System.out.println("节点引用校验通过");
        System.out.println();
    }

    public static ListNode getIntersectionNode(
            ListNode headA, ListNode headB){
        Set<ListNode> visited = new HashSet<>();
        ListNode listNode = new ListNode(0);
        listNode.next = headA;
        listNode = listNode.next;
        while(listNode != null){
            visited.add(listNode);
            listNode = listNode.next;
        }
        listNode = headB;
        while (listNode != null){
            if(visited.contains(listNode)) return listNode;
            listNode = listNode.next;
        }
        return null;
    }
}
