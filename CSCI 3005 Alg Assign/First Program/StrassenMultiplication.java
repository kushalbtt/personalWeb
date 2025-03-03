/**
* Name: Kushal Bhattarai
* CWID: 30162615
* Date: February 27, 2025
* Course: CSCI 3005-60360


 * The StrassenMultiplication class implements matrix multiplication using Strassen's algorithm,
 * which reduces the number of multiplications compared to the standard method.
 */
public class StrassenMultiplication {
   private static int stressenCount = 0; 
   
   /**
    * Retrieves the count of multiplications performed in Strassen's algorithm.
    *
    * @return The number of multiplications performed.
    */
   public static int getStressenCount() {
      return stressenCount;
   }

   /**
    * Multiplies two matrices using Strassen's algorithm.
    *
    * @param M1 The first matrix.
    * @param M2 The second matrix.
    * @return The resulting matrix after multiplication.
    */
   public static int[][] multiply(int[][] M1, int[][] M2) {
      stressenCount = 0; 
      return multiplyHelper(M1, M2);
   }

   /**
    * Helper method for performing Strassen's multiplication recursively.
    *
    * @param M1 The first matrix.
    * @param M2 The second matrix.
    * @return The resulting matrix after recursive multiplication.
    */
   private static int[][] multiplyHelper(int[][] M1, int[][] M2) {
      int row1 = M1.length;
   
      // Base case: If matrix is of size 1x1, perform direct multiplication
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
   
      // Extract submatrices
      MatrixOperations.extractSubMatrix(M1, A11, 0, 0);
      MatrixOperations.extractSubMatrix(M1, A12, 0, n);
      MatrixOperations.extractSubMatrix(M1, A21, n, 0);
      MatrixOperations.extractSubMatrix(M1, A22, n, n);
      MatrixOperations.extractSubMatrix(M2, B11, 0, 0);
      MatrixOperations.extractSubMatrix(M2, B12, 0, n);
      MatrixOperations.extractSubMatrix(M2, B21, n, 0);
      MatrixOperations.extractSubMatrix(M2, B22, n, n);
   
      // Compute intermediary matrices using Strassen's formula
      int[][] S1 = MatrixOperations.add(A11, A22);
      int[][] S2 = MatrixOperations.add(B11, B22);
      int[][] H1 = multiplyHelper(S1, S2);  
   
      int[][] S3 = MatrixOperations.add(A21, A22);
      int[][] H2 = multiplyHelper(S3, B11);  
   
      int[][] S4 = MatrixOperations.subtract(B12, B22);
      int[][] H3 = multiplyHelper(A11, S4);  
   
      int[][] S5 = MatrixOperations.subtract(B21, B11);
      int[][] H4 = multiplyHelper(A22, S5);  
   
      int[][] S6 = MatrixOperations.add(A11, A12);
      int[][] H5 = multiplyHelper(S6, B22); 
   
      int[][] S7 = MatrixOperations.subtract(A21, A11);
      int[][] S8 = MatrixOperations.add(B11, B12);
      int[][] H6 = multiplyHelper(S7, S8);  
   
      int[][] S9 = MatrixOperations.subtract(A12, A22);
      int[][] S10 = MatrixOperations.add(B21, B22);
      int[][] H7 = multiplyHelper(S9, S10);  
   
      // Compute result submatrices
      int[][] T1 = MatrixOperations.add(H1, H4);
      int[][] T2 = MatrixOperations.subtract(T1, H5);
      int[][] R11 = MatrixOperations.add(T2, H7);  
   
      int[][] R12 = MatrixOperations.add(H3, H5);  
   
      int[][] R21 = MatrixOperations.add(H2, H4);  
   
      int[][] T3 = MatrixOperations.add(H1, H3);
      int[][] T4 = MatrixOperations.subtract(T3, H2);
      int[][] R22 = MatrixOperations.add(T4, H6);  
   
      // Combine submatrices into the final result matrix
      return MatrixOperations.combine(R11, R12, R21, R22);
   }
}
