package likou;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildTree {
    public static void main(String[] args) {
        int[] preorder = {3, 9, 20, 15, 7};
        int[] inorder = {9, 3, 15, 20, 7};
        BuildTree buildTree = new BuildTree();
        TreeNode root = buildTree.buildTree(preorder, inorder);

        List<Integer> actualPreorder = new ArrayList<>();
        collectPreorder(root, actualPreorder);
        List<Integer> actualInorder = InorderTraversal.inorderTraversal(root);

        System.out.println("前序预期 " + Arrays.toString(preorder));
        System.out.println("前序实际 " + actualPreorder);
        System.out.println("中序预期 " + Arrays.toString(inorder));
        System.out.println("中序实际 " + actualInorder);

        boolean matches = actualPreorder.equals(Arrays.stream(preorder).boxed().toList())
                && actualInorder.equals(Arrays.stream(inorder).boxed().toList());
        System.out.println(matches ? "检查通过" : "检查失败");
    }

    private static void collectPreorder(TreeNode node, List<Integer> result) {
        if (node == null) {
            return;
        }
        result.add(node.val);
        collectPreorder(node.left, result);
        collectPreorder(node.right, result);
    }

    public TreeNode buildTree(int[] preorder, int[] inorder){
        int n = inorder.length;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            map.put(inorder[i], i);
        }
        TreeNode treeNode = build(preorder, 0, 0, n - 1, map);
        return treeNode;
    }

    private TreeNode build(int[] preorder, int preStart,
                           int inLeft, int inRight,
                           Map<Integer, Integer> inorderIndex){
        if(inLeft > inRight)return null;
        Integer mid = inorderIndex.get(preorder[preStart]);
        TreeNode node = new TreeNode(preorder[preStart]);
        node.left = build(preorder, preStart + 1, inLeft, mid - 1, inorderIndex);
        node.right = build(preorder, preStart + 1 + mid - inLeft, mid + 1, inRight, inorderIndex);
        return node;
    }
}
