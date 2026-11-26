package likou;

import java.util.ArrayList;
import java.util.List;

public class partitionLabels {
    public static void main(String[] args) {
        String s = "ababcbacadefegdehijhklij";
        List<Integer> integers = partitionLabels(s);
        for (int i : integers) {
            System.out.println(i);
        }
    }
    public static List<Integer> partitionLabels(String s) {
        int[] last = new int[26];
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            last[c-'a'] = i;
        }
        List<Integer> result = new ArrayList<>();
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            end = Math.max(end,last[s.charAt(i) - 'a']);
            if(end == i){
                result.add(end - start + 1);
                start = end + 1;
            }
        }
        return result;
    }
}
