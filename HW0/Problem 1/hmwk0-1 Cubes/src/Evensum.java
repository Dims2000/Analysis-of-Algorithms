import java.util.Scanner;

/**
 * The Evensum class takes an initial integer (numOfInts) and then takes a
 * numOfInt number of integers. Following this, the program returns the sum of
 * all the even integers that were entered (not including numOfInts).
 * 
 * @author Dmitry Selin (des3358)
 */
public class Evensum {

    /**
     * The main method of the program. Takes an integer from standard input
     * (numOfInts), then reads numOfInts number of integers from standard
     * input. If an entered integer is even, it is added to evenSum. When
     * all the integers have been entered, evenSum is printed.
     * 
     * @param args command line arguments (not applicable)
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int numOfInts = input.nextInt();
        int evenSum = 0;

        //Repeats numOfInt times
        for(int i = 0; i < numOfInts; i++) {
            int num = input.nextInt();

            //Checks if an entered integer is even
            if (num%2 == 0)
                evenSum += num;
        }

        //Outputs the sum of all entered even integers (and closes Scanner)
        System.out.println(evenSum);
        input.close();
    }
}
