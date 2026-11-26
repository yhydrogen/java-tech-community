package likou;

public class SymmetricTree {
    public static void main(String[] args) {
        Integer[] values = {1,2,2,3,4,4,3};
        TreeNode root = TreeNode.fromLevelOrder(values);
        boolean symmetric = isSymmetric(root);
        System.out.println(symmetric);
    }
    public static boolean isSymmetric(TreeNode root) {
        return isMirror(root.left, root.right);
    }
    private static boolean isMirror(TreeNode a, TreeNode b){
        if(a == null && b == null)return true;
        if (a == null || b == null) return false;
        return a.val == b.val && isMirror(a.left, b.right) && isMirror(a.right, b.left);
    }
}
