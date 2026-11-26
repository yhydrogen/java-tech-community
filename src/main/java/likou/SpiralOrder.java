package likou;

import java.util.ArrayList;
import java.util.List;

public class SpiralOrder {
    public static void main(String[] args) {
        int[][] matrix = {{1,2,3},{4,5,6},{7,8,9}};
        List<Integer> list = spiralOrder(matrix);
        System.out.println(list);
    }
    public static List<Integer> spiralOrder(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        List<Integer> list = new ArrayList<>();
        int step = (Math.min(m, n) + 1) / 2;
        for (int i = 0; i < step; i++) {
            if(i == m - 1 - i){
                for(int j = i; j <= n - 1 - i; ++j){
                    list.add(matrix[i][j]);
                }
                return list;
            }
            else if(i == n - 1 - i){
                for(int j = i; j <= m - 1 - i; ++j){
                    list.add(matrix[j][i]);
                }
                return list;
            }
            for (int j = i; j < n - i - 1; j++) {
                list.add(matrix[i][j]);
            }
            for (int j = i; j < m - 1 - i; j++) {
                list.add(matrix[j][n - 1 - i]);
            }
            for (int j = n - 1 - i; j > i; j--) {
                list.add(matrix[m - 1 - i][j]);
            }
            for (int j = m - 1 - i; j > i ; j--) {
                list.add(matrix[j][i]);
            }
        }
        return list;
    }
}
