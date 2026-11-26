package likou;

import java.util.*;

public class groupAnagrams_49 {
    public static List<List<String>> groupAnagrams(String[] strs) {
        //String[] strs = {"eat", "tea", "tan", "ate", "nat", "bat"};

        Map<String, List<String>> map = new HashMap<>();
        for (String word : strs) {
            char[] chars = word.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            if(!map.containsKey(key))
                map.put(key, new ArrayList<>());
            map.get(key).add(word);
            //System.out.println(word + " -> " + key);
        }
        return new ArrayList<>(map.values());
    }
    public static void main(String[] args) {
        String[] strs = {"eat", "tea", "tan", "ate", "nat", "bat"};

        List<List<String>> result = groupAnagrams(strs);
        System.out.println(result);
    }
}
