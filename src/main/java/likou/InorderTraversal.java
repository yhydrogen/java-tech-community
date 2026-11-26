package likou;

import java.util.ArrayList;
import java.util.List;

public class InorderTraversal {
    public static void main(String[] args) {
        Integer[] values = {1, null, 2, 3};
        TreeNode root = TreeNode.fromLevelOrder(values);
        List<Integer> result = inorderTraversal(root);
        System.out.println(result); // 预期输出：[1, 3, 2]
    }

    public static List<Integer> inorderTraversal(TreeNode root){
        List<Integer> ans = new ArrayList<>();
        inorder(root, ans);
        return ans;
    }

    private static void inorder(TreeNode node, List<Integer> result){
        if (node == null) return;
        inorder(node.left, result);
        result.add(node.val);
        inorder(node.right,result);
    }
}
