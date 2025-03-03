/**
Date:   10/30/2024
Course: CSCI2073-44355

Programming Description: The RobotWorld class is a class that simulates a robotic environment in which a robot performs tasks with two piles of two character strings. 
  This program process a limited set of commands for manipulating blocks that lies on the surface. The Blocks comes in three colors (Pink, Yellow, and white) where 
  they are leveled with lowercase characters and are arranged in two different piles. 
  This class has two public methods alongs with necessay privates methods:
  
  a) RobotWorld(String world): It is a constructor that initializes the robot's world. The string argument contains characters representing the blocks in each of the piles,
  seperated by a comma. Each block is a lowerCase character followed by either Y,W, and P to represent the color. For each pile, the block at the bottom of the pile is listed
  first.
  
  b) String commands(String filename): Its a method to read and process commands for the robot to perform the valid commands for the robot arms. They are:
      * move x: where x is lowercase letter representing a block label. The robot puts block x onto the top of another pile. Any blocks originally stacked
                on the top of x should remain in their original pile in original order. 
      * discard x: The robot removes the block x from its pile and restore the block in their original pile in original order.
      * grab-Color X: where X is the color (Y,W, or P). The robot finds and removes the topmost blockof color C from each pile,
                      if it exists. Any blocks originally stacked on the top of the selected blocks should be restored in the original pile in original order.  
                      
  c) String printPile(StackInt<String> pile1, StackInt<String> pile2): It is the method to print the conatents of two piles without commas between the blocks. It returns a 
  string representation of two piles, showing their contents without altering their order.                     


On my honor, I have neither given nor received unauthorized help while
completing this assignment.
 
Kushal Bhattarai/ 30162615
*/





import java.io.*;
import java.util.*;

/**
 * The RobotWorld class simulates a robotic environment where a robot manipulates blocks
 * in two piles, with blocks represented as two-character strings (label and color).
 * The robot can execute a limited set of commands to move, discard, or grab blocks.
*/
 
public class RobotWorld {
   StackInt<String> pile1 = new LinkedStack<>();
   StackInt<String> pile2 = new LinkedStack<>();

/**
    * Constructs a RobotWorld with two piles initialized from the provided string.
    *
    * @param blocks A string containing two comma-separated sequences of blocks,
    *               where each block is represented by a lowercase character followed
    *               by a color character (Y, W, P).
*/
    
   RobotWorld(String blocks) {
      String[] sep = blocks.split(",");
      String word1 = sep[0];
      String word2 = sep[1];
   
      for (int i = 0; i < word1.length(); i += 2) {
         pile1.push(word1.substring(i, i + 2));
      }
   
      for (int i = 0; i < word2.length(); i += 2) {
         pile2.push(word2.substring(i, i + 2));
      }
   
   }
/** 
   * Reads and processes commands from the given file.
   *
   * @param fileName The name of the file containing commands for the robot.
   * @return A string representing the states of piles after executing the commands.
*/
   public String commands(String fileName) {
      StringBuilder result = new StringBuilder();
      try {
         File fs = new File(fileName);
         Scanner sc = new Scanner(fs);
      
         while (sc.hasNextLine()) {
            String command = sc.nextLine();
         
            String[] sep = command.split(" ");
            String command1 = sep[0];
            char argument = sep[1].charAt(0);
         
            if (command1.equals("move")) {
               move(argument);
            
               result.append(printPile(pile1, pile2)).append("\n");
            }
            else if(command1.equals("discard")) {
               discard(argument);
               
               result.append(printPile(pile1, pile2)).append("\n");
            }
            else if (command1.equals("grab-color")) {
               grabColor(argument);
               
               result.append(printPile(pile1, pile2)).append("\n");
            }
         }
              
      }
      catch (FileNotFoundException e) {
         System.out.println("The given file is missing.");
      }
      return result.toString();
   }         
      
/** 
   * Moves a block from one pile to another pile.
   *
   * @param x The level of block to be moved.
 
*/   
     
   
   private void move(char x) {
      Stack<String> temp = new Stack<>();
      boolean found = false;
   
    // Step 1: Look for `x` in pile1
      while (!pile1.empty()) {
         String tp = pile1.pop();
      
         if (!found && tp.charAt(0) == x) {
            pile2.push(tp);  // Move `x` to pile2 if found
            found = true;
         } else {
            temp.push(tp);   // Push non-target elements to temp
         }
      }
   
    // Restore pile1 in the correct order
      while (!temp.empty()) {
         pile1.push(temp.pop());
      }
   
    // Step 2: If `x` was not found in pile1, look in pile2
      if (!found) {
         while (!pile2.empty()) {
            String tp = pile2.pop();
         
            if (!found && tp.charAt(0) == x) {
               pile1.push(tp);  // Move `x` to pile1 if found
               found = true;
            } else {
               temp.push(tp);   // Push non-target elements to temp
            }
         }
      
        // Restore pile2 in the correct order
         while (!temp.empty()) {
            pile2.push(temp.pop());
         }
      }
   }
  


   /**
    * Discards a block from both piles.
    *
    * @param y The label of the block to be discarded.
    */ 

   private void discard(char y){
      Stack<String> temp = new Stack<>();
        //Stack<String> temp2 = new Stack<>();
   
      while(!pile1.empty()){
         String tp= pile1.pop();
         if((tp.charAt(0) != y )){
            temp.push(tp);
         }
      }
      while(!temp.empty()){
         pile1.push(temp.pop());
      }
   
      while(!pile2.empty()){
         String tp= pile2.pop();
         if(!(tp.charAt(0)==(y))){
            temp.push(tp);
         }
      }
      while(!temp.empty()){
         pile2.push(temp.pop());
      }
   
   }
   
/**
    * Grabs the topmost block of a specified color from both piles.
    *
    * @param z The color of the block to be grabbed (Y, W, or P).
*/

   private void grabColor(char z){
      Stack<String> temp = new Stack<>();
      boolean found= false;
        
      while(!pile1.empty()){
         String fn = pile1.pop();
           
         if(!found && fn.charAt(1) == z) {
         
            found = true;
              
         
         }else{
            
            temp.push(fn);}
            
      }
      while(!temp.empty()){
         pile1.push(temp.pop());
      }
        
      found = false;
   
      while(!pile2.empty()){
            
         String fn = pile2.pop();
         if(!found && fn.charAt(1) == z) {
            found = true;    
         }
         else{
            temp.push(fn);
         }
            
      }
      while(!temp.empty()){
         pile2.push(temp.pop());
      }
   
   }
   
/**
    * Prints the contents of both piles without altering their order.
    *
    * @param pile1 The first pile to print.
    * @param pile2 The second pile to print.
    * @return A string representation of the contents of both piles.

*/
   public String printPile(StackInt<String> pile1, StackInt<String> pile2) {
      StackInt<String> temp1 = new LinkedStack<>();
      StackInt<String> temp2 = new LinkedStack<>();
      
      StringBuilder result = new StringBuilder();
   
      boolean firstBlock = true;
   
      result.append("PILE 1: [");
   
      while (!pile1.empty()) {
         String x = pile1.pop();
      
         if(!firstBlock){
            result.append(" ");
         }
         
         
         result.append(x);
         temp1.push(x);   
         firstBlock = false;
      }
      result.append("] ");
    
      while (!temp1.empty()) {
         pile1.push(temp1.pop());
      }
      
      firstBlock = true;
   
      result.append("PILE 2: [");
      while (!pile2.empty()) {
         String y = pile2.pop();
         
         if(!firstBlock){
            result.append(" ");
         }
      
         result.append(y);
         temp2.push(y);    
         firstBlock = false;
      }
      result.append("]");
    
    
      while (!temp2.empty()) {
         pile2.push(temp2.pop());
      }
      return result.toString();
   }
}
