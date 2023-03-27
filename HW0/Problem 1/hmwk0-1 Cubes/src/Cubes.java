/**
 * The Cubes class takes a non-negative integer less than 1,000,000 using
 * standard input and prints every perfect cube from 0 to less than or equal
 * to the enered ineger using standard output.
 *  
 * @author Dmitry Selin (des3358)
 */

import java.util.Scanner;

public class Cubes {

    /**
     * The main method that takes a single integer from standard input (using
     * Scanner), calls another method to print all perfect cubes, and closes 
     * the Scanner.
     * 
     * @param args command line arguments (not applicable)
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        printAllCubes(input.nextInt());
        input.close();
    }

    /**
     * Prints all perfect cubes from 0 to limit to standard output
     * 
     * @param limit the upper limit for printing out cubes
     */
    public static void printAllCubes(int limit) {

        //An infitite for loop - terminates at break
        for(int i = 0; i >= 0; i++) {
            double num = Math.pow(i, 3);

            //If the num is over the limit, break, otherwise keep iterating
            if (num > limit)
                break;
            else
                System.out.println((int)num);
        }
    }
}


