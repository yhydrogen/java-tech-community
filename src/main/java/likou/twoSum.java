package likou;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class twoSum {
    public static int[] twoSum(int[] nums, int target){
        //int[] nums = {2, 7, 11, 15};
        //int target = 9;
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if(map.containsKey(target - nums[i]))
                return new int[]{map.get(target-nums[i]), i};
            map.put(nums[i], i);
        }

        return new int[0];
    }

    public static void main(String[] args) {
        int[] nums = {2, 7, 11, 15};
        int target = 9;

        int[] result = twoSum(nums, target);

        System.out.println(Arrays.toString(result));
    }
}
