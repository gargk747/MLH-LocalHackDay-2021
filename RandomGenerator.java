// Generate Random Numbers Using Middle 
// Square Method in Java 
import java.util.Random; 
  
public class Main { 
    static int rangeArray[] 
        = { 1,      10,      100,      1000,     10000, 
            100000, 1000000, 10000000, 100000000 }; 
    // function for generating a random number 
    static long middleSquareNumber(long num, int digit) 
    { 
        long sqn = num * num, nextNum = 0; 
        int trim = (digit / 2); 
        sqn = sqn / rangeArray[trim]; 
        for (int i = 0; i < digit; i++) { 
            nextNum += (sqn % (rangeArray[trim])) 
                       * (rangeArray[i]); 
            sqn = sqn / 10; 
        } 
        return nextNum; 
    } 
}
    public static void main(String args[]) 
    { 
        int numberOfDigit = 3; 
        int start = rangeArray[numberOfDigit - 1], 
            end = rangeArray[numberOfDigit]; 
        // create rand object 
        Random rand = new Random(); 
        long nextNumber = rand.nextInt(end - start) + start; 
        System.out.print( 
            "The random numbers for the Geeks are:\n"
            + nextNumber + ", "); 
        // Generating 10 random numbers 
        for (int i = 0; i < 9; i++) { 
            nextNumber = middleSquareNumber(nextNumber, 
                                            numberOfDigit); 
            System.out.print(nextNumber + ", "); 
        } 
    } 
}