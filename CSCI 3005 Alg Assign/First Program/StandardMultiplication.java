/**

* Name: Kushal Bhattarai
* CWID: 30162615
* Date: February 27, 2025
* Course: CSCI 3005-60360


 * The StandardMultiplication class implements matrix multiplication.
 */
public class StandardMultiplication {
   /**
    * Calculates the number of  multiplications required for matrix multiplication.
    *
    * @param M1 The first matrix.
    * @param M2 The second matrix.
    * @return The number of multiplication required.
    */
   public static int standardCount(int[][] M1, int[][] M2) {
      int row1 = M1.length;
      int col1 = M1[0].length;
      int col2 = M2[0].length;
      return row1 * col1 * col2;
   }
}