
public class bhattaraiku {
   public static void main(String[] args) {
      int[] testValues = {0, 1,2,3,4,5,6,7, 10, 100, 1000,10000, 100000, 1000000}; 
      int cwid = 30162617;  
     
     
      for (int n : testValues) {
         long operations = Analyzer.test(n, cwid);
         System.out.println("n = " + n + ", operations = " + operations);
      }
   
   }   
          
}
