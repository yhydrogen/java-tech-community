package likou;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Merge {
    public static void main(String[] args) {
        int[][] intervals = {{1,3},{2,6},{8,10},{15,18}};
        int[][] ans = merge(intervals);
        System.out.println(Arrays.deepToString(ans));
    }
    public static int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        List<int[]> result = new ArrayList<>();
        for (int i = 0; i < intervals.length; i++) {
            int l = intervals[i][0], r = intervals[i][1];
            if(result.isEmpty() || result.get(result.size() - 1)[1] < l){
                result.add(new int[]{l , r});
            }
            else {
                // get 取得列表中最后一个区间数组，修改它也会更新列表中的内容。
                int[] last = result.get(result.size() - 1);
                // 被包含的区间不能让原来的右端点缩小。
                last[1] = Math.max(last[1], r);
            }
        }
        return result.toArray(new int[0][]);
    }
}
