class fibIteratives{

   private int count =0;
   private int count2 =0;
   
   
   public long rec(int n){
      if(n<= 1){
         return 1;
      }
   
   } 

   
   public int getCount2(){
      return count2;
   }
   
   
   
   public long fib(int n){
      long i = 0;
      long s= 1;
      long total=0;
        
      for (int k=2; k<=n; k++){
         total = i + s;
         i = s;
         s = total;
         count ++;
      }
      return s;   
   }
    
   public int getCount(){
      return count;
   }
}
public class Java
{
   public static void main(String[] args) 
   {
      test(10);
      test(20);
      test(100);
   }
	
   public static void test(int num)
   {
      fibIteratives f = new fibIteratives();
   	
      System.out.println("Using fib: " + f.fib(num) + " in " + f.getCount() + " calculations");
   	//System.out.println("Using fib2: " + f.fib2(num) + " in " + f.getCount2() + " calculations");
   }
}