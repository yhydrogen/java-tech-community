package likou;

public class MaxDepth {
    public static void main(String[] args) {
        Integer[] values = {1, 2, 5, 3, 4, null, 6};
        TreeNode root = TreeNode.fromLevelOrder(values);
        TreeNode p = root;
        while (p != null) {
            System.out.print(p.val + " ");
            p = p.right;
        }
        int maxed = maxDepth(root);
        System.out.println(maxed);
    }
    public static int maxDepth(TreeNode root){
        if (root == null)return 0;
        int left = maxDepth(root.left);
        int right = maxDepth(root.right);
        return left > right ? left + 1 : right + 1;
    }
}
