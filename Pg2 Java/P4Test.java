import java.util.*;
import java.io.*;

/**
	This class is provided by the instructor to test some of the
	basic functionality of the ProductControlSystem class.
   
   Expected output:
      Shade01 added to inventory
      BluBlock added to inventory
      100 units of BluBlock added to inventory at a total cost of $100.00 
      2 units of BluBlock sold at a total price of $9.96 for a profit of $7.96
      10 units of Shade01 added to inventory at a total cost of $5.00 
      ERROR: Towel01 not in inventory
      Shade01 removed from inventory for a total loss of $5.00
      Shade03 added to inventory
      200 units of Shade03 added to inventory at a total cost of $102.00 
      Total cost: $200.00, Total profit: $2.96
*/
public class P4Test
{
   public static void main(String[] args)
   {
		ProductControlSystem myStore = new ProductControlSystem();
      String output = myStore.processTransactions("trans0.txt");
      System.out.println(output);
   }
}