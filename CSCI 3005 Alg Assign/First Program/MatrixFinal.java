/**
*
* Name: Kushal Bhattarai
* CWID: 30162615
* Date: February 27, 2025
* Course: CSCI 3005-60360
*
* The MatrixFinal class provides utility methods for reading and printing matrices.
 */
import java.io.*;
import java.util.*;

public class MatrixFinal {
    /**
     * Reads a matrix from the given Scanner input.
     *
     * @param scan The Scanner object to read input from.
     * @param rows The number of rows in the matrix.
     * @param cols The number of columns in the matrix.
     * @return A 2D array representing the matrix.
     */
    public static int[][] readMatrix(Scanner scan, int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = scan.nextInt();
            }
        }
        return matrix;
    }

    /**
     * Prints a matrix to the specified PrintWriter output.
     *
     * @param matrix The matrix to print.
     * @param writer The PrintWriter object to write output to.
     */
    public static void printMatrix(int[][] matrix, PrintWriter writer) {
        for (int[] row : matrix) {
            for (int value : row) {
                writer.print(value + " ");
            }
            writer.println();
        }
    }
}