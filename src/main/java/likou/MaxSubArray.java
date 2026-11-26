package likou;

public class MaxSubArray {
    public static void main(String[] args) {

        int[] nums = {-2,1,-3,4,-1,2,1,-5,4};
        System.out.println(maxSubArray(nums));
        System.out.println(maxSubArray2(nums));
    }
    // 方法一：动态规划，pre 表示以当前元素结尾的最大子数组和。
    public static int maxSubArray(int[] nums){
        int pre = 0, maxAns = nums[0];
        for (int x : nums) {
            pre = Math.max(pre + x, x);
            maxAns = Math.max(maxAns, pre);
        }
        return maxAns;
    }

    // 方法二：前缀和。与方法一一样，按题意要求 nums 非空。
    public static int maxSubArray2(int[] nums) {
        int sum = 0;
        int minPrefix = 0; // 一个元素都没取时，前缀和为 0。
        int maxAns = nums[0];
        for (int x : nums) {
            sum += x;
            // 先减去此前最小的前缀和，得到以当前元素结尾的最大和。
            maxAns = Math.max(maxAns, sum - minPrefix);
            // 再登记当前前缀，避免把当前前缀减去自身而选中空子数组。
            minPrefix = Math.min(minPrefix, sum);
        }
        return maxAns;
    }
}
