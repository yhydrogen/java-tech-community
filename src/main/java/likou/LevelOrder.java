package likou;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class LevelOrder {
    public static void main(String[] args) {
        Integer[] values = {1,2,2,3,4,4,3};
        TreeNode root = TreeNode.fromLevelOrder(values);
    }
    public static List<List<Integer>> levelOrder(TreeNode root){
        Queue<TreeNode> queue = new ArrayDeque<>();
        List<List<Integer>> result = new ArrayList<>();
        if(root == null) return result;
        queue.offer(root);
        while (!queue.isEmpty()){
            int size = queue.size();
            List<Integer> list = new ArrayList<>();
            while (size != 0){
                --size;
                TreeNode node = queue.poll();
                list.add(node.val);
                if(node.left != null) queue.offer(node.left);
                if(node.right != null) queue.offer(node.right);
            }
            result.add(list);
        }
        return result;
    }
}
