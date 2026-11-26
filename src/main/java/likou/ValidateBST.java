package likou;

public class ValidateBST {
    public static void main(String[] args) {
        Integer[] values = {3,5,1,6,2,0,8,null,null,7,4};
        TreeNode root = TreeNode.fromLevelOrder(values);
    }
    public static boolean isValidBST(TreeNode root){
        return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }
    private static boolean validate(TreeNode node, long lower, long upper){
        if(node == null)return true;
        if (node.val > lower && node.val <upper){
            boolean left = validate(node.left, lower, node.val);
            boolean right = validate(node.right, node.val, upper);
            if(left && right)return true;
            return false;
        }
        return false;
    }
}
