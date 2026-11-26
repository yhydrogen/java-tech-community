package likou;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class RightSideView {
    public static void main(String[] args) {
        Integer[] values = {1, 2, 5, 3, 4, null, 6};
        TreeNode root = TreeNode.fromLevelOrder(values);
    }
    public static List<Integer> rightSideView(TreeNode root){
        Queue<TreeNode> queue = new ArrayDeque<>();
        List<Integer> result = new ArrayList<>();
        if(root == null) return result;
        queue.offer(root);
        while (!queue.isEmpty()){
            int length = queue.size();
            while(length > 1){
                TreeNode poll = queue.poll();
                if(poll.left != null)queue.offer(poll.left);
                if(poll.right != null)queue.offer(poll.right);
                length --;
            }
            TreeNode poll = queue.poll();
            result.add(poll.val);
            if(poll.left != null)queue.offer(poll.left);
            if(poll.right != null)queue.offer(poll.right);
        }
        return result;
    }
}
