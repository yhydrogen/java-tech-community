package likou;

public class MajorityElement {
    public static void main(String[] args) {
        int[] nums = {2,2,1,1,1,2,2};
        int i = majorityElement(nums);
        System.out.println(i);
    }
    public static int majorityElement(int[] nums) {
        int candidate = nums[0], votes = 0;
        for (int i = 0; i < nums.length; i++) {
            if(votes == 0){
                candidate = nums[i];
            }
            if(nums[i] == candidate){
                votes++;
            }
            else {
                votes--;
            }
        }
        return candidate;
    }
}
