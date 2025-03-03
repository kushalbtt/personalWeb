import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

public class matrix {
    private static int stressenCount = 0;
    public static void main(String[] args) {

      
        int standardMatrix = 0;

        try (Scanner scan = new Scanner(new File("input.txt"));
                PrintWriter writer = new PrintWriter(new FileWriter("output.txt"))) {
            int data = scan.nextInt();
            for (int i = 0; i < data; i++) {
                int row1 = scan.nextInt();
                int col1 = scan.nextInt();
                int row2 = scan.nextInt();
                int col2 = scan.nextInt();

                if (col1 != row1) {
                    System.out.println("Matrix not compactable.");
                }

                int[][] matrix1 = firstMatrix(scan, row1, col1);
                int[][] matrix2 = firstMatrix(scan, row2, col2);

                standardMatrix = standardMatrix(matrix1, matrix2);

                // int[][] result = new int[row1][col2];
                stressenCount = 0;
                int[][] result = stressenMatrix(matrix1, matrix2);

                writer.println("Data Set " + data + ":");
                writer.println("Size of Result Matrix: " + row1 + " " + col2);
                writer.println("Result Matrix:");
                printMatrix(result, writer);
                writer.println("Standard Multiplications: " + standardMatrix);
                writer.println("Strassen Multiplications: " + stressenCount);
                writer.println("Difference: " + (standardMatrix - stressenCount));
                writer.println();
               

            }
        } catch (IOException e) {
            System.out.println("Error file not found.");
        }

    }

    public static int[][] firstMatrix(Scanner scan, int x, int y) {
        int[][] matrix = new int[x][y];
        for (int i = 1; i <= x; i++) {
            for (int j = 1; j <= y; j++) {
                matrix[i][j] = scan.nextInt();
            }
        }
        return matrix;
    }

    public static int standardMatrix(int[][] M1, int[][] M2) {
        int count = 0;
        int row1 = M1.length;
        int col1 = M1[0].length;
        int col2 = M2[0].length;
        int[][] result = new int[row1][col2];

        for (int i = 1; i <= row1; i++) {
            for (int j = 1; j <= col2; j++) {
                for (int k = 1; k <= col1; k++) {
                    result[i][j] += M1[i][k] * M2[k][j];
                    count++;
                }
            }
        }
        return count;
    }

    public static int[][] stressenMatrix(int[][] M1, int[][] M2) {
        int stressenCount = 0;
        int row1 = M1.length;
        int col1 = M1[0].length;
        int col2 = M2[0].length;
        int[][] result = new int[row1][col2];

        if (row1 == 1) {
            stressenCount++;
            return new int[][]{{M1[0][0] * M2[0][0]}};
        }

        int n = row1 / 2;

        int[][] A11 = new int[n][n];
        int[][] A12 = new int[n][n];
        int[][] A21 = new int[n][n];
        int[][] A22 = new int[n][n];

        int[][] B11 = new int[n][n];
        int[][] B12 = new int[n][n];
        int[][] B21 = new int[n][n];
        int[][] B22 = new int[n][n];

        minMatrix(M1, A11, 0, 0);
        minMatrix(M1, A12, 0, n);
        minMatrix(M1, A21, n, 0);
        minMatrix(M1, A22, n, n);

        minMatrix(M2, B11, 0, 0);
        minMatrix(M2, B12, 0, n);
        minMatrix(M2, B21, n, 0);
        minMatrix(M2, B22, n, n);

        int[][] H1 = stressenMatrix(addMatrix(A11, A22), addMatrix(B11, B22));
        int[][] H2 = stressenMatrix(addMatrix(A21, A22), B11);
        int[][] H3 = stressenMatrix(A11, subMatrix(B12, B22));
        int[][] H4 = stressenMatrix(A22, subMatrix(B21, B11));
        int[][] H5 = stressenMatrix(addMatrix(A11, A12), B22);
        int[][] H6 = stressenMatrix(subMatrix(A21, A11), addMatrix(B11, B12));
        int[][] H7 = stressenMatrix(subMatrix(A12, A22), addMatrix(B21, B22));

        int[][] R11 = addMatrix(subMatrix(addMatrix(H1, H4), H5), H7);
        int[][] R12 = addMatrix(H3, H5);
        int[][] R21 = addMatrix(H2, H4);
        int[][] R22 = addMatrix(subMatrix(addMatrix(H1, H3), H2), H6);

        result = joinMatrix(R11, R12, R21, R22);

        return result;
    }

    public static int[][] minMatrix(int[][] A, int[][] ns, int x, int y) {
        int size = ns.length;

        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                ns[i][j] = A[x + i][y + j];
            }
        }
        return ns;
    }

    public static int[][] addMatrix(int[][] A, int[][] B) {
        int n = A.length;

        int[][] ns = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ns[i][j] = A[i][j] + B[i][j];
            }
        }
        return ns;
    }

    public static int[][] subMatrix(int[][] A, int[][] B) {
        int n = A.length;

        int[][] ns = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ns[i][j] = A[i][j] - B[i][j];
            }
        }
        return ns;
    }

    public static int[][] joinMatrix(int[][] R11, int[][] R12, int[][] R21, int[][] R22) {
        int size = R11.length * 2;
        int[][] ns = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ns[i][j] = R11[i][j];
                ns[i][j + size] = R12[i][j];
                ns[i + size][j] = R21[i][j];
                ns[i + size][j + size] = R22[i][j];
            }
        }

        return ns;
    }
    public static void printMatrix(int[][] result, PrintWriter print){
        for(int[] rus : result){
            for(int value: rus){
                print.print(value+ " ");
            }
        }
        
    }

}
