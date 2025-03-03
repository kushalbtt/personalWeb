/**
* Name: Kushal Bhattarai
* CWID: 30162615
* Date: February 27, 2025
* Course: CSCI 3005-60360

* Programming Description: This programming assignment compairs the number of multiplications in the standard
*         and strassen matric multliplication. This will take a file as a input which contains the numbers of the set
*        of matrices, the size of the matrices and the values contained in the matrices. The program will calculate the 
*        number of multiplications needed for standard multiplication using the formula (rows 1 * columns 1 * columns 2).
*         This program will run only if the number of columns in the first matrix must match the number of rows in the second 
*        matrix. Whereas the number of multiplications used in the Strassen Method by count the number of multiplications 
*        performed as you go through the process of multiplying the input matrices. Once the program has multiplied the matrix 
*        you will generate an output file (out.txt) that contain the number of sets the size of each result matrix, the result matrix
*        the number of multiplications for both standard and Strassen method and the difference between the two techniques. 
*
*
*
*      On my honor, I have neither given nor received unauthorized help while
*               completing this assignment.
*/


import java.io.*;
import java.util.*;

/**
 * The Home class reads matrix data from an input file, performs matrix multiplication using both 
 * standard and Strassen's algorithm, and writes the results to an output file.
 */
public class Home {
   private static int stressenCount = 0;

   /**
    * The main method reads input data, performs matrix multiplications, and writes the results to a file.
    *
    * @param args Command-line arguments (not used in this program).
    */
   public static void main(String[] args) {
      int standardMatrix = 0;
   
      try (Scanner scan = new Scanner(new File("input.txt"));
             PrintWriter writer = new PrintWriter(new FileWriter("output.txt"))) {
         
         int data = scan.nextInt();
         writer.println(data);
         
         for (int i = 0; i < data; i++) {
            int row1 = scan.nextInt();
            int col1 = scan.nextInt();
            int row2 = scan.nextInt();
            int col2 = scan.nextInt();
         
            // Check if matrices are compatible for multiplication
            if (col1 != row2) {
               System.out.println("Matrix not compatible.");
               continue;
            }
         
            // Read matrices from input
            int[][] matrix1 = MatrixFinal.readMatrix(scan, row1, col1);
            int[][] matrix2 = MatrixFinal.readMatrix(scan, row2, col2);
         
            // Perform standard matrix multiplication
            standardMatrix = StandardMultiplication.standardCount(matrix1, matrix2);
            
            // Perform Strassen's matrix multiplication
            int[][] result = StrassenMultiplication.multiply(matrix1, matrix2);
         
            // Write results to output file
            writer.println(row1 + " " + col2);
            MatrixFinal.printMatrix(result, writer);
            writer.println("Standard Multiplications: " + standardMatrix);
            writer.println("Strassen Multiplications: " + StrassenMultiplication.getStressenCount());
            writer.println("Difference: " + (standardMatrix - StrassenMultiplication.getStressenCount()));
         }
      } 
      // Printing the error if the file isnot found
      catch (IOException e) {
         System.out.println("Error: file not found.");
      }
   }
}
