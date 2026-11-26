package likou;

public class maxWater {
    public static void main(String[] args) {
        int[] nums = {1,8,6,2,5,4,8,3,7};
        int result = maxArea(nums);
        System.out.println(result);
    }
    public static int maxArea(int[] height) {
        int ans = 0, l = 0, r = height.length-1;
        while(l<=r)
        {
            int temp = (r-l)*Math.min(height[l],height[r]);
            ans = Math.max(ans, temp);
            if(height[l]>height[r])r--;
            else l++;
        }
        return ans;
    }
}
