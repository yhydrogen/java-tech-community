package likou;

public class JumpGameII {
    public static void main(String[] args) {
        int[] nums = {3,2,1,0,4};
        int jump = jump(nums);
        System.out.println(jump);
    }
    public static int jump(int[] nums){
        if(nums.length == 1) return 0;
        int steps = 0, end = 0, fasted = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            fasted = Math.max(fasted, i + nums[i]);
            if(end == i){
                end = fasted;
                steps++;
            }
        }
        return steps;
    }
}
