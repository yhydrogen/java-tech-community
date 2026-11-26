package likou;

public class SortColor {
    public static void main(String[] args) {
        int[] nums = {2,0,2,1,1,0};
        sortColors(nums);
        for (int i = 0; i < nums.length; i++) {
            System.out.println(nums[i]);
        }
    }
    public static void sortColors(int[] nums) {
        int left = 0,right = nums.length-1;
        for (int i = 0; i <= right;) {
            if(nums[i]==0){
                int temp = nums[left];
                nums[left] = nums[i];
                nums[i] = temp;
                left++;
                i++;
            }
            else if(nums[i]==2){
                int temp = nums[right];
                nums[right] = nums[i];
                nums[i] = temp;
                right--;
            }
            else {
                ++i;
            }
        }
    }
}
