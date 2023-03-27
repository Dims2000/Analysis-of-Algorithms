import java.util.Scanner;

/**
 * The IndexSearch method takes a sorted array of distinct integers from
 * standard input and determines if it contains a value that is equal to
 * that value's index (i.e A[i]=i).
 * 
 * @author Dmitry Selin (des3358)
 */
public class IndexSearch {

    /**
     * The main method that gets a sorted array of distinct integers (from 
     * standard input),calls on a method (searchArray) to determine if a value
     * in the array is equal to its index, and prints TRUE or FALSE depending
     * on the outcome of searchArray.
     * 
     * @param args command line arguments (not applicable)
     */
    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        int arrayLength = input.nextInt();
        
        //Initializes the array from standard input
        int[] array = new int[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            array[i]=input.nextInt();
        }

        input.close();

        if (searchArray(array))
            System.out.println("TRUE");
        else
            System.out.println("FALSE");
    }

    /**
     * Searches through array to find if an index of a value is equal to the
     * index. If this condition exists, the method terminates (returns TRUE).
     * Additionally, if a value that is larger than its index is found, the 
     * method also terminates (returns FALSE).
     * 
     * @param array a sorted array of distinct integers
     * @return true if array has a value that is equal to that value's index,
     *         false otherwise
     */
    private static boolean searchArray(int[] array) {

        //Iterate through the length of the array - index starting at 1
        for (int i = 1; i <= array.length; i++) {
 
            if (array[i-1] == i)      //Check if the index is equal to the
                return true;          //value.
            else if (array[i-1] > i)  //If a value is bigger than the index,
                break;                //terminate the loop.
        }

        return false;
    }
}