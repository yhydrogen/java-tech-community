package likou;

public class MaxProduct {
    public static void main(String[] args) {
        int[] nums = {-2,3,-4};
        int ans = maxProduct(nums);
        System.out.println(ans);
    }
    public static int maxProduct(int[] nums) {
        int maxn = nums[0], minn = nums[0], ans = nums[0];
        for (int i = 1; i < nums.length; i++) {
            int temp = maxn;
            maxn = Math.max(Math.max(maxn * nums[i], nums[i]), minn * nums[i]);
            minn = Math.min(Math.min(minn * nums[i], nums[i]), temp * nums[i]);
            ans = Math.max(Math.max(maxn, minn), ans);
        }
        return ans;
    }
}
