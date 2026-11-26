package likou;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class threeSum {
    public static void main(String[] args) {
        int[] nums = {-1,0,1,2,-1,-4};
        threeSum(nums);

    }
    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> ans = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if(i>0 && nums[i]==nums[i-1])continue;
            int k = nums.length-1;
            int target = -nums[i];
            for (int j = i+1; j < nums.length; j++) {
                if(j>i+1 && nums[j]==nums[j-1])continue;
                while (j<k && nums[j]+nums[k]>target)k--;
                if(j == k)break;
                if (nums[j]+nums[k]==target){
                    List<Integer> list = new ArrayList<>();
                    list.add(nums[i]);
                    list.add(nums[j]);
                    list.add(nums[k]);
                    ans.add(list);
                }
            }
        }
        return ans;
    }
}
