package likou;

import java.util.HashSet;
import java.util.Set;

public class lengthSubstring {
    public static void main(String[] args) {
        String s = "abcabcbb";
        int ans = lengthOfLongestSubstring(s);
        System.out.println(ans);
    }
    public static int lengthOfLongestSubstring(String s) {
        int ans = 0, left = 0;
        Set<Character> set = new HashSet<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            while (set.contains(c)){
                set.remove(s.charAt(left));
                ++left;
            }
            set.add(s.charAt(i));
            ans = Math.max(ans, i - left + 1);
        }
        return ans;
    }
}
