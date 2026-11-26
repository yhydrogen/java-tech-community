package likou;

import java.util.Arrays;

public class NextPermutation {
    public static void main(String[] args) {
        int[] nums = {1,2,3};
        nextPermutation(nums);
        System.out.println(Arrays.toString(nums));
    }

    public static void nextPermutation(int[] nums) {
        int p1 = -1;
        for (int i = nums.length - 1; i > 0 ; i--) {
            if(nums[i-1] < nums[i]){
                p1 = i-1;
                break;
            }
        }
        if(p1 == -1){
            reverse(nums, 0,nums.length-1);
            return;
        }
        int p2 = nums.length-1;
        for (int i = nums.length - 1; i > p1 ; i--) {
            if(nums[i] > nums[p1]){
                p2 = i;
                break;
            }
        }
        swap(nums, p1, p2);
        reverse(nums, p1 + 1, nums.length - 1);
    }

    public static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    public static void reverse(int[] nums, int left, int right) {
        while (left < right) {
            swap(nums, left, right);
            left++;
            right--;
        }
    }
}
