/**
*
*
* Name: Kushal Bhattarai
* CWID: 30162615
* Date: February 27, 2025
* Course: CSCI 3005-60360
*
*
 * The MatrixOperations class provides utility methods for matrix operations,
 * including extraction of submatrices, addition, subtraction, and combination of matrices.
 */
public class MatrixOperations {
    /**
     * Extracts a submatrix from a given source matrix and stores it in the destination matrix.
     *
     * @param source The original matrix.
     * @param destination The matrix to store the extracted submatrix.
     * @param x The starting row index in the source matrix.
     * @param y The starting column index in the source matrix.
     */
    public static void extractSubMatrix(int[][] source, int[][] destination, int x, int y) {
        int size = destination.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                destination[i][j] = source[x + i][y + j];
            }
        }
    }

    /**
     * Adds two matrices and returns the result.
     *
     * @param A The first matrix.
     * @param B The second matrix.
     * @return The resulting matrix after addition.
     */
    public static int[][] add(int[][] A, int[][] B) {
        int n = A.length;
        int[][] result = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = A[i][j] + B[i][j];
            }
        }
        return result;
    }

    /**
     * Subtracts matrix B from matrix A and returns the result.
     *
     * @param A The first matrix.
     * @param B The second matrix.
     * @return The resulting matrix after subtraction.
     */
    public static int[][] subtract(int[][] A, int[][] B) {
        int n = A.length;
        int[][] result = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = A[i][j] - B[i][j];
            }
        }
        return result;
    }

    /**
     * Combines four smaller matrices into a larger one.
     *
     * @param R11 The top-left submatrix.
     * @param R12 The top-right submatrix.
     * @param R21 The bottom-left submatrix.
     * @param R22 The bottom-right submatrix.
     * @return The combined matrix.
     */
    public static int[][] combine(int[][] R11, int[][] R12, int[][] R21, int[][] R22) {
        int size = R11.length;
        int[][] result = new int[size * 2][size * 2];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = R11[i][j];
                result[i][j + size] = R12[i][j];
                result[i + size][j] = R21[i][j];
                result[i + size][j + size] = R22[i][j];
            }
        }
        return result;
    }
}