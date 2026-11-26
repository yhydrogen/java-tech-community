package likou;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Rotate {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4, 5, 6, 7};
        rotate(nums, 3);
        System.out.println(Arrays.toString(nums));
    }
    public static void rotate(int[] nums, int k){
        k = k % nums.length;
        reverse(nums, 0,nums.length-1);
        reverse(nums, 0, k-1);
        reverse(nums, k, nums.length-1);
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
