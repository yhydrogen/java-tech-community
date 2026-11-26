package likou;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class InvertTree {
    public static void main(String[] args) {
        Integer[] values = {10, 5, -3, 3, 2, null, 11, 3, -2, null, 1};
        TreeNode root = TreeNode.fromLevelOrder(values);
        TreeNode flipped = invertTree(root);

        // LinkedList 可以保存 null，用它记录缺少左孩子或右孩子的位置。
        Queue<TreeNode> queue = new LinkedList<>();
        List<Integer> levelOrder = new ArrayList<>();
        queue.offer(flipped);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node == null) {
                levelOrder.add(null);
                continue;
            }
            levelOrder.add(node.val);
            queue.offer(node.left);
            queue.offer(node.right);
        }
        while (!levelOrder.isEmpty() && levelOrder.get(levelOrder.size() - 1) == null) {
            levelOrder.remove(levelOrder.size() - 1);
        }
        System.out.println(levelOrder);
    }
    public static TreeNode invertTree(TreeNode root){
        if (root == null) return null;
        TreeNode temp = invertTree(root.left);
        root.left = invertTree(root.right);
        root.right = temp;
        return root;
    }
}
