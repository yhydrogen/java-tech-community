package likou;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FindAnagrams {
    public static void main(String[] args) {
        String s = "cbaebabacd", p = "abc";
        List<Integer> anagrams = findAnagrams(s, p);
        for (int i:anagrams) System.out.println(i);
    }
    public static List<Integer> findAnagrams(String s, String p){
        if(s.length() < p.length()){
            return new LinkedList<>();
        }
        int[] need = new int[26];    // p 中各字母的次数
        int[] window = new int[26];  // 当前窗口中各字母的次数
        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < p.length(); i++) {
            char pc = p.charAt(i), sc = s.charAt(i);
            need[pc - 'a']++;
            window[sc - 'a']++;
        }
        if(Arrays.equals(need, window)){
            ans.add(0);
        }
        for(int i = p.length(); i < s.length(); i++){
            char c = s.charAt(i), pre = s.charAt(i-p.length());
            window[c - 'a']++;
            window[pre - 'a']--;
            if(Arrays.equals(window, need)){
                ans.add(i - p.length()+1);
            }
        }
        return  ans;
    }
}
