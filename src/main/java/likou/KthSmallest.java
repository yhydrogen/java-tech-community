package likou;

import java.util.Stack;

public class KthSmallest {
    public static void main(String[] args) {
        Integer[] values = {5,3,6,2,4,null,null,1};
        TreeNode root = TreeNode.fromLevelOrder(values);
        int kthed = kthSmallest(root, 3);
        System.out.println(kthed);
    }
    public static int kthSmallest(TreeNode root, int k){
        Stack<TreeNode> stack = new Stack<>();
        while (root != null || !stack.empty()){
            while (root != null){
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            --k;
            if(k == 0){
                break;
            }
            root = root.right;
        }
        return root.val;
    }
}
