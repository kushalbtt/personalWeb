/**
Date:   12/02/2024
Course: CSCI2073-44355
 
Programming Description:  The ProductControlSystem class is a software for a simple production control system designed 
for a speciality store.This system will manage the acquisition and sale of various items of different types.
I used Map to make this software. I used two methods as a public methods named as ProductControlSystem() and String 
processTransactions(String dataFile). Where ProductControlSystem() method intiialize the map that is used in the software 
and the String processTransactions(String dataFile) methods is used to read and proess data about various transactions from a file.
The software will stop when "*" is encountered.


if the transaction format is given in the formats of "new item-id item-cost item-selling-price" then that item is added into 
the inventory if an only the item isnot in the inventory before. And the messege is printed in well formated form as:
If the item already exists: ERROR: <Item ID> already in inventory
If successfully added: <Item ID> added to inventory


if the transaction format is given in the formats of "delete item-id" then the item is deleted from the inventory if it is 
available in the inventory. All the remaining product of that item is consider a loss. And the message is printed in well formated form as:
If the item doesn’t exist: ERROR: <Item ID> not in inventory
If successfully removed: <Item ID> removed from inventory for a total loss of $<Amount>

if the transaction format is given in the formats of "sell item-id quantity" then the specific number of that item will 
be sold if the item is there in the inventory. And the message is printed in well formated form as:
If the item doesn’t exist: ERROR: <Item ID> not in inventory
If the quantity exceeds available stock: ERROR: <Number> exceeds units of <Item ID> in inventory
If successfully sold: <Number> units of <Item ID> sold at a total price of $<Amount> for a profit of $<Amount>


if the transaction format is given in the formats of "buy item-id quantity" then the specific number of item is added 
in the inventory. And the message is printed in the well format form as:
If the item doesn’t exist: ERROR: <Item ID> not in inventory
If successfully added: <Quantity> units of <Item ID> added to inventory at a total cost of $<Amount>

if the transcation format is given in the formats of "report" then the message containing the summary report of the total 
cost of items in inventory, total profit is printed as:
Total cost: $<Amount>, Total profit: $<Amount>

  


 
On my honor, I have neither given nor received unauthorized help while
completing this assignment.
 
Kushal Bhattarai/ 30162615
*/
 


import java.io.*;
import java.util.*;


/**
 * This class represents a Product Control System for managing inventory operations.
 * It allows adding, deleting, buying, selling products, and generating a financial report.
 * @inventory  inventory map to store the items with production name as key and items details as values
 * @param totalInvest total cost invested in the inventory
 * @param TotalLoss total loss faced
 * @param TotalProfit total profit made
 */
 public class ProductControlSystem {
   private Map<String, details> inventory;      
   private double totalInvest = 0;           
   private double TotalLoss=0;              
   private double TotalProfit=0;           



    /**
    * Constructor to initialize the ProductControlSystem with an empty inventory.
    */
      ProductControlSystem() {
      inventory = new HashMap<>();
   
   }


  /**
    * Processes transactions from a given file containing transaction details.
    * 
    * @param dataFile the path to the file containing transaction data.
    * @return a string containing the results of all processed transactions.
  */
   public String processTransactions(String dataFile)  {
   
      StringBuilder result = new StringBuilder();   // Stores output messages 
   
      try {
      // Reading the file from transaction line
         File fl = new File(dataFile);
         Scanner sc = new Scanner(fl);
      
         while (sc.hasNextLine()) {
            String line = sc.nextLine();
            
            // Split line into action and arguments
            String[] split = line.split(" ");
         
           // Extract the action from the transaction line
            String action = split[0].toLowerCase();
           
         
         // Add new item to the inventory
            if (action.equals("new")) {
               double cost = Double.parseDouble(split[2]);
               double sell = Double.parseDouble(split[3]);
               int num = 0;
            
            // Check if the item already exists in the store
               if (inventory.containsKey(split[1])){
                  result.append("ERROR: "+ split[1]+" already in inventory\n");
               }
               
               else{
               // Add the item in the inventory
                  inventory.put(split[1], new details(cost, sell, num));
                  result.append(split[1]+" added to inventory\n");
               }
            
            }
            
            
         // Handle "delete" action to remove a product from the inventory
            else if (action.equals("delete")) {
            
            // Check if the item exist in the inventory
               if(inventory.containsKey(split[1])){
                 details goods = inventory.get(split[1]);
                  int itemLeft = goods.getNumber();
                  double deletLoss = itemLeft * goods.getCost();
                  TotalLoss += deletLoss;
                  inventory.remove(split[1]);              
                  totalInvest -= TotalLoss;
                  TotalProfit -= deletLoss;
                  result.append(split[1]).append(" removed from inventory for a total loss of $").append(String.format("%.2f",TotalLoss)).append("\n");
               }
               else{
               // If the item isnot in inventory 
                  result.append("ERROR: ").append(split[1]).append(" not in inventory\n");
               }
            
            } 
            
            // Handle "buy" action to add units of an existing product to the inventory
              else if (action.equals("buy")) {             
               String name = split[1];
               int count = Integer.parseInt(split[2]);
            
               if(inventory.containsKey(name)){
                  details goods = inventory.get(name);
                  goods.setNumber(goods.getNumber()+count);
                  totalInvest += goods.getCost()*count;
                  double productCost = goods.getCost()*count;
                  result.append(count).append(" units of ").append(name).append(" added to inventory at a total cost of $").append(String.format("%.2f\n",productCost));
               }
               else{
                  result.append("ERROR: ").append(name).append(" not in inventory\n");
               }
            
               
          // Handle "sell" action to sell units of a product from the inventory  
            } else if (action.equals("sell")) {
             
               String name= split[1];
               int count = Integer.parseInt(split[2]);
            
               if(inventory.containsKey(name)){
                  details goods = inventory.get(name);
                  if(count > goods.getNumber()){
                     result.append("ERROR: ").append(count).append(" exceeds units of ").append(name).append(" in inventory\n");
                  }
                  else{
                  
                     goods.setNumber(goods.getNumber()-count);
                     double sellAmt = count * goods.getSell();
                     double TransProfit = (goods.getSell()- goods.getCost())*count;
                  
                     TotalProfit += TransProfit;
                     totalInvest -= goods.getCost()*count;
                  
                     result.append(count).append(" units of ").append(name).append(" sold at a total price of $").append(String.format("%.2f", sellAmt)).append(" for a profit of $").append(String.format("%.2f\n", TransProfit));
                  }
               }
               else{
                  result.append("ERROR: ").append(name).append(" not in inventory\n");
               }
            
               
             // Handle "report" action to generate a financial report
            } else if (action.equals("report")) {
            
               result.append("Total cost: $").append(String.format("%.2f,",totalInvest)).append(" Total profit: $").append(String.format("%.2f\n",TotalProfit));
            
              
            }
         }
      
      } catch (FileNotFoundException  e) {
         result.append("ERROR: file not found.");
      }
   
   
      return result.toString();
   }

}

/**
 *  details class represents an item in the inventory.
 * It stores details such as cost price, selling price, and number of units.
 */
class details{
   private double cost ;
   private double sell;
   private int number;


 /**
    * Constructor to initialize an details with cost, sell, and number of units.
    * 
    * @param c the cost price of the item.
    * @param s the selling price of the item.
    * @param n the number of units of the item.
 */
   details(double c, double s, int n){
      cost = c;
      sell = s;
      number = n;
   }
   
   /**
    * Gets the cost price of the item.
    * 
    * @return the cost price.
    */
   public double getCost(){
      return cost;
   }
   
   /**
    * Gets the selling price of the item.
    * 
    * @return the selling price.
    */
   public double getSell(){
      return sell;
   }
   
    /**
    * Sets the number of units of the item.
    * 
    * @return the number of items.
    */
   public int getNumber(){
      return number;
   }
   
    /**
    * Sets the number of units of the item.
    * 
    * @param n the number of units to set.
    */
   public void setNumber(int n){
      number =n;
   }

}