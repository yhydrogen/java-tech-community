package likou;

import java.util.HashMap;
import java.util.Map;

public class SubarraySum {
    public static void main(String[] args) {
        int[] nums = {1,2,3};
        System.out.println(subarraySum(nums, 3));
    }
    public static int subarraySum(int[] nums, int k){
        Map<Integer, Integer> count = new HashMap<>();
        count.put(0, 1);
        int sum = 0, ans = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if(count.getOrDefault(sum - k, 0) != 0){
                ans += count.get(sum - k);
            }
            count.put(sum, count.getOrDefault(sum, 0) + 1);
        }
        return ans;
    }
}
