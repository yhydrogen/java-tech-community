package likou;

import java.util.HashSet;
import java.util.Set;

public class longConsecutive {
    public static void main(String[] args) {
        int[] nums = {100, 4, 200, 1, 3, 2, 2};
        int result = longestConsecutive(nums);
        System.out.println(result);
    }

    public static int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            set.add(nums[i]);
        }
        int sum = 0;
        for (int num:set){
            if(!set.contains(num-1)){
                int temp = 1;
                int curnum = num;
                while (set.contains(curnum+1)){
                    temp += 1;
                    curnum += 1;
                }
                sum = Math.max(sum, temp);
            }
        }
        return sum;
    }
}
