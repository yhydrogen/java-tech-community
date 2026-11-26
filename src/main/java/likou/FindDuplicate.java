package likou;

public class FindDuplicate {
    public static void main(String[] args) {
        int[] nums = {1,3,4,2,2};
        int duplicate = findDuplicate(nums);
        System.out.println(duplicate);
    }
    public static int findDuplicate(int[] nums) {
        int fast = 0,slow = 0;
        do {
            fast = nums[nums[fast]];
            slow = nums[slow];
        }while (fast != slow);
        int ans = 0;
        while (ans != slow){
            ans = nums[ans];
            slow = nums[slow];
        }
        return ans;
    }
}
