package likou;

public class LowestCommonAncestor {
    public static void main(String[] args) {
        Integer[] values = {3,5,1,6,2,0,8,null,null,7,4};
        TreeNode root = TreeNode.fromLevelOrder(values);
        TreeNode treeNode = lowestCommonAncestor(root, root.left.right.left, root.left.right.right);
        System.out.println(treeNode.val);
    }
    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q){
        if(root == null)return null;
        if(root == p || root == q) return root;
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p ,q);
        if(left == null && right != null)return right;
        if(left != null && right == null) return left;
        if(left == null && right == null) return null;
        return root;
    }
}
