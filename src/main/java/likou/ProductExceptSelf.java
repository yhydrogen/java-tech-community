package likou;

import java.util.Arrays;

public class ProductExceptSelf {
    public static void main(String[] args) {
        int[] nums = {1,2,3,4};
        nums = productExceptSelf(nums);
        System.out.println(Arrays.toString(nums));
    }
    public static int[] productExceptSelf(int[] nums) {
        int[] ans = new int[nums.length];
        int left = 1, right = 1;
        for(int i = 0;i < nums.length; ++i){
            ans[i] = left;
            left *= nums[i];
        }
        for (int i = nums.length-1; i >= 0; --i){
            ans[i] *= right;
            right *= nums[i];
        }
        return ans;
    }
}
