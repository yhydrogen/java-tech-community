package likou;

public class CopyRandomList {
    public static void main(String[] args) {
        Node head = buildRandomList(
                new int[]{7, 13, 11, 10, 1},
                new int[]{-1, 0, 4, 2, 0});
        Node copiedHead = new CopyRandomList().copyRandomList(head);

        for (Node node = copiedHead; node != null; node = node.next) {
            System.out.println("val=" + node.val
                    + ", random=" + (node.random == null ? "null" : node.random.val));
        }
    }

    // values[i] 是第 i 个节点的值；randomIndices[i] 是其 random 目标下标，-1 表示 null。
    public static Node buildRandomList(int[] values, int[] randomIndices) {
        if (values == null || randomIndices == null || values.length != randomIndices.length) {
            throw new IllegalArgumentException("节点值数组与 random 下标数组必须非 null 且长度相同");
        }
        if (values.length == 0) {
            return null;
        }

        Node[] nodes = new Node[values.length];
        for (int i = 0; i < values.length; i++) {
            if (randomIndices[i] < -1 || randomIndices[i] >= values.length) {
                throw new IllegalArgumentException("random 下标越界，位置：" + i);
            }
            nodes[i] = new Node(values[i]);
        }
        for (int i = 0; i < nodes.length; i++) {
            nodes[i].next = i + 1 < nodes.length ? nodes[i + 1] : null;
            nodes[i].random = randomIndices[i] == -1 ? null : nodes[randomIndices[i]];
        }
        return nodes[0];
    }

    public Node copyRandomList(Node head) {
        if (head == null) {
            return null;
        }
        for (Node node = head; node != null; node = node.next.next){
            Node newNode = new Node(node.val);
            newNode.next = node.next;
            node.next = newNode;
        }
        for (Node node = head; node != null; node = node.next.next){
            Node newNode = node.next;
            newNode.random = (node.random != null) ? node.random.next : null;
        }
        Node newhead = head.next;
        for (Node node = head; node != null; node = node.next){
            Node newNode = node.next;
            node.next = newNode.next;
            newNode.next = (newNode.next != null) ? newNode.next.next : null;
        }
        return newhead;
    }
}
