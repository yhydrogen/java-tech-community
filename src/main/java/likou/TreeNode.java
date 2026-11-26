package likou;

import java.util.ArrayDeque;
import java.util.Queue;

public class TreeNode {
    int val;         // 当前节点的值
    TreeNode left;   // 左孩子
    TreeNode right;  // 右孩子
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    /** 按层序数组建树。null 表示缺少对应节点，末尾的 null 可以省略。 */
    public static TreeNode fromLevelOrder(Integer[] values) {
        if (values == null || values.length == 0 || values[0] == null) {
            return null;
        }

        TreeNode root = new TreeNode(values[0]);
        Queue<TreeNode> parents = new ArrayDeque<>();
        parents.add(root);

        int index = 1;
        while (index < values.length && !parents.isEmpty()) {
            TreeNode parent = parents.remove();

            Integer leftValue = values[index++];
            if (leftValue != null) {
                parent.left = new TreeNode(leftValue);
                parents.add(parent.left);
            }

            if (index < values.length) {
                Integer rightValue = values[index++];
                if (rightValue != null) {
                    parent.right = new TreeNode(rightValue);
                    parents.add(parent.right);
                }
            }
        }
        return root;
    }
}
