package likou;

public class FlattenBinaryTree {
    public static void main(String[] args) {
        Integer[] values = {1, 2, 5, 3, 4, null, 6};
        TreeNode root = TreeNode.fromLevelOrder(values);
        FlattenBinaryTree flattenBinaryTree = new FlattenBinaryTree();
        flattenBinaryTree.flatten(root);
        TreeNode p = root;
        while (p != null) {
            System.out.print(p.val + " ");
            p = p.right;
        }
    }
    public void flatten(TreeNode root){
        flattenAndGetTail(root);
    }
    private TreeNode flattenAndGetTail(TreeNode node){
        if (node == null) return null;
        TreeNode left = flattenAndGetTail(node.left);
        TreeNode right = flattenAndGetTail(node.right);
        if(left != null){
            left.right = node.right;
            node.right = node.left;
            node.left = null;
        }
        if(right != null){
            return right;
        }else if(left != null){
            return left;
        }
        else {
            return node;
        }
    }
}
