package likou;

import java.util.Arrays;

public class RotateImage {
    public static void main(String[] args) {
        int[][] matrix = {{5,1,9,11},{2,4,8,10},{13,3,6,7},{15,14,12,16}};
        rotate(matrix);
        System.out.println(Arrays.deepToString(matrix));
    }

    public static void rotate(int[][] matrix) {
        int m = matrix.length;
        for (int i = 0; i < matrix.length/2; i++) {
            for (int j = 0; j < (matrix.length + 1) / 2; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[m - 1 - j][i];
                matrix[m - 1 - j][i] = matrix[m - 1 - i][m - 1 - j];
                matrix[m - 1 - i][m - 1 - j] = matrix[j][m - 1 - i];
                matrix[j][m - 1 - i] = temp;
            }
        }
    }
}
