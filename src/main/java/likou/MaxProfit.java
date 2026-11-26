package likou;

public class MaxProfit {
    public static void main(String[] args) {
        int[] nums = {7,1,5,3,6,4};
        int maxed = maxProfit(nums);
        System.out.println(maxed);
    }
    public static int maxProfit(int[] prices) {
        int pre = prices[0];
        int ans = 0;
        for (int i = 1; i < prices.length; i++) {
            ans = Math.max(ans, prices[i] - pre);
            pre = Math.min(pre, prices[i]);
        }
        return ans;
    }
}
