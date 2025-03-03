import java.io.*;
import java.util.*;

public class MatrixMultiplicationComparison {

   // Counter for Strassen multiplications
   private static int strassenMultiplications = 0;

   public static void main(String[] args) {
      
      
      try (Scanner scan = new Scanner(new File("input.txt"));
             PrintWriter writer = new PrintWriter(new FileWriter("output.txt"))) {
      
      
         int numDataSets = scan.nextInt();
      
         for (int set = 0; set < numDataSets; set++) {
            int rows1 = scan.nextInt();
            int cols1 = scan.nextInt();
            int rows2 = scan.nextInt();
            int cols2 = scan.nextInt();
         
            if (cols1 != rows2) {
               throw new IllegalArgumentException("Matrix dimensions are not compatible for multiplication.");
            }
         
            int[][] matrix1 = readMatrix(scan, rows1, cols1);
            int[][] matrix2 = readMatrix(scan, rows2, cols2);
         
            // Standard multiplication
            int[][] standardResult = new int[rows1][cols2];
            int standardMultiplications = standardMultiply(matrix1, matrix2, standardResult);
         
            // Strassen multiplication
            strassenMultiplications = 0; // Reset counter
            int[][] strassenResult = strassen(matrix1, matrix2);
            int strassenCount = strassenMultiplications;
         
            // Write results to output file
            writer.println("Data Set " + (set + 1) + ":");
            writer.println("Size of Result Matrix: " + rows1 + "x" + cols2);
            writer.println("Result Matrix:");
            printMatrix(strassenResult, writer);
            writer.println("Standard Multiplications: " + standardMultiplications);
            writer.println("Strassen Multiplications: " + strassenCount);
            writer.println("Difference: " + (standardMultiplications - strassenCount));
            writer.println();
         }
      
      } catch (IOException e) {
         System.err.println("Error reading or writing file: " + e.getMessage());
      }
   }

   // Standard matrix multiplication
   private static int standardMultiply(int[][] A, int[][] B, int[][] C) {
      int rows1 = A.length;
      int cols1 = A[0].length;
      int cols2 = B[0].length;
      int multiplications = 0;
   
      for (int i = 0; i < rows1; i++) {
         for (int j = 0; j < cols2; j++) {
            C[i][j] = 0;
            for (int k = 0; k < cols1; k++) {
               C[i][j] += A[i][k] * B[k][j];
               multiplications++;
            }
         }
      }
   
      return multiplications;
   }

   // Strassen matrix multiplication
   private static int[][] strassen(int[][] A, int[][] B) {
      int n = A.length;
   
      if (n == 1) {
         strassenMultiplications++; // Count multiplication
         return new int[][]{{A[0][0] * B[0][0]}};
      }
   
      int newSize = n / 2;
   
      // Partition matrices into submatrices
      int[][] A11 = partition(A, 0, 0, newSize);
      int[][] A12 = partition(A, 0, newSize, newSize);
      int[][] A21 = partition(A, newSize, 0, newSize);
      int[][] A22 = partition(A, newSize, newSize, newSize);
      int[][] B11 = partition(B, 0, 0, newSize);
      int[][] B12 = partition(B, 0, newSize, newSize);
      int[][] B21 = partition(B, newSize, 0, newSize);
      int[][] B22 = partition(B, newSize, newSize, newSize);
   
      // Recursive steps
      int[][] M1 = strassen(add(A11, A22), add(B11, B22));
      int[][] M2 = strassen(add(A21, A22), B11);
      int[][] M3 = strassen(A11, subtract(B12, B22));
      int[][] M4 = strassen(A22, subtract(B21, B11));
      int[][] M5 = strassen(add(A11, A12), B22);
      int[][] M6 = strassen(subtract(A21, A11), add(B11, B12));
      int[][] M7 = strassen(subtract(A12, A22), add(B21, B22));
   
      // Combine results
      int[][] C11 = add(subtract(add(M1, M4), M5), M7);
      int[][] C12 = add(M3, M5);
      int[][] C21 = add(M2, M4);
      int[][] C22 = add(subtract(add(M1, M3), M2), M6);
   
      // Join submatrices into result matrix
      return join(C11, C12, C21, C22);
   }

   // Helper methods for matrix operations
   private static int[][] add(int[][] A, int[][] B) {
      int n = A.length;
      int[][] result = new int[n][n];
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            result[i][j] = A[i][j] + B[i][j];
         }
      }
      return result;
   }

   private static int[][] subtract(int[][] A, int[][] B) {
      int n = A.length;
      int[][] result = new int[n][n];
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            result[i][j] = A[i][j] - B[i][j];
         }
      }
      return result;
   }

   private static int[][] partition(int[][] matrix, int row, int col, int size) {
      int[][] sub = new int[size][size];
      for (int i = 0; i < size; i++) {
         for (int j = 0; j < size; j++) {
            sub[i][j] = matrix[row + i][col + j];
         }
      }
      return sub;
   }

   private static int[][] join(int[][] C11, int[][] C12, int[][] C21, int[][] C22) {
      int size = C11.length * 2;
      int[][] result = new int[size][size];
      int newSize = C11.length;
   
      for (int i = 0; i < newSize; i++) {
         for (int j = 0; j < newSize; j++) {
            result[i][j] = C11[i][j];
            result[i][j + newSize] = C12[i][j];
            result[i + newSize][j] = C21[i][j];
            result[i + newSize][j + newSize] = C22[i][j];
         }
      }
   
      return result;
   }

   // Read matrix from input
   private static int[][] readMatrix(Scanner scanner, int rows, int cols) {
      int[][] matrix = new int[rows][cols];
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < cols; j++) {
            matrix[i][j] = scanner.nextInt();
         }
      }
      return matrix;
   }

   // Print matrix to output file
   private static void printMatrix(int[][] matrix, PrintWriter writer) {
      for (int[] row : matrix) {
         for (int value : row) {
            writer.print(value + " ");
         }
         writer.println();
      }
   }
}