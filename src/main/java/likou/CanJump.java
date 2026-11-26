package likou;

public class CanJump {
    public static void main(String[] args) {
        int[] nums = {3,2,1,0,4};
        boolean b = canJump(nums);
        System.out.println(b);
    }
    public static boolean canJump(int[] nums) {
        if(nums.length == 1) return true;
        int end = 0;
        for (int i = 0; i < nums.length; i++) {
            end = Math.max(end, nums[i]+i);
            if(end >= nums.length-1)return true;
            if (i >= end){
                return false;
            }
        }
        return true;
    }
}
