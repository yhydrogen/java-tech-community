package likou;

public class PathSumIII {
    public static void main(String[] args) {
        Integer[] values = {10, 5, -3, 3, 2, null, 11, 3, -2, null, 1};
        TreeNode root = TreeNode.fromLevelOrder(values);
        int sum = pathSum(root, 8);
        System.out.println(sum);
    }
    public static int pathSum(TreeNode root, int targetSum) {
        if(root == null)return 0;
        int count = countFrom(root, targetSum);
        int left = pathSum(root.left, targetSum);
        int right = pathSum(root.right, targetSum);
        return count + left + right;
    }

    private static int countFrom(TreeNode node, long remaining){
        if(node == null)return 0;
        long nextRemaining = remaining - node.val;
        int matchesHere = nextRemaining == 0 ? 1 : 0;
        int left = countFrom(node.left, nextRemaining);
        int right = countFrom(node.right, nextRemaining);
        return matchesHere + left + right;
    }
}
