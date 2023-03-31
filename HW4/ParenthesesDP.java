import java.util.Scanner;

/**
 * This program takes a mathematical expression (with only addition and 
 * subtraction) from standard input and outputs the largest possible value
 * that can be formed when parenthesizing the given expression.
 * 
 * @author Dmitry Selin (des3358)
 */
public class ParenthesesDP {

    /**
     * The dynamic 2D array that stores largest values for different
     * expression permutations in the following format...
     * 
     * dpArray[startNum][endNum] = largest value that can be formed from the
     *                             interval startNum -> endNum
     */
    private static int[][] dpArray;

    /**
     * The user-given math expression in an array format (delimited by spaces)
     */
    private static String[] exp;

    /**
     * The main method of the program that parses the given expression by
     * spaces, populates the dynamic array, and returns the largest value
     * (the last element of the first row of the dynamic array).
     * 
     * @param args command line arguments (not applicable)
     */
    public static void main(String[] args) {

        // Gets the expression from standard input and parses it by spaces
        Scanner input = new Scanner(System.in);
        exp = input.nextLine().split(" ");

        input.close();
        
        // Generates the dynamic array
        int numOfNums =(exp.length + 1)/2;
        dpArray = new int[numOfNums][numOfNums];

        // Populates the dynamic array by iterating diagonally
        for (int y = 1; y < numOfNums; y++) {
            int x = 0;
        
            for (int yi = y; yi < numOfNums; yi++) {
                dpArray[x][yi] = calculateValue(x, yi);
                x++;
            }
        }

        // Prints out the largest value
        System.out.println(dpArray[0][numOfNums-1]);
    }

    /**
     * Iterates through every possible 2-part split of the interval l -> r and
     * returns the largest value that can be formed from said interval.
     * 
     * @param l the index of the leftmost number in exp
     * @param r the index of the rightmost number in exp
     * @return the largest value that can be formed from the interval l -> r
     */
    private static int calculateValue(int l, int r) {

        // Initializes starting values
        Integer largestValue = null;
        boolean maxNeg = false;

        // If the sign in front of l is negative, minimize largestValue instead
        if (l > 0 && !getOperator(l-1, l).equals("+")) {
              maxNeg = true;
        }

        // Iterates through every possible 2-part split of l -> r
        for (int i = 0; i < (r - l); i++) {

            // Gets the two numbers (from dpArray or exp)
            int num1 = getNum(l, l+i);
            int num2 = getNum(l+i+1, r);

            // Performs an operation on the two numbers
            String operator = getOperator(l+i, l+i+1);
            int value = performOperation(num1, num2, operator);

            // If maxNeg, the value must be minimized, otherwise, maximize it
            if (
                (largestValue == null) ||
                (maxNeg && value < largestValue) || 
                (!maxNeg && value > largestValue)
            ) {
                largestValue = value;
            }
        }

        return largestValue;
    }

    /**
     * Simply performs an operation (addition or subtraction) on num1 and num2
     * based on the operator that is passed.
     * 
     * @param num1 a number
     * @param num2 a number
     * @param operator '+' or '-', descides if num1 and num2 should be added or
     *                 subtracted
     * @return the result of the performed operation
     */
    private static int performOperation(int num1, int num2, String operator) {
        return (operator.equals("+") ? (num1 + num2) : (num1 - num2));
    }

    /**
     * Returns the operator that is between num1 and num2 in exp. Both numbers
     * must be adjacent to each other in order for this method to function.
     * 
     * @param num1 the index of a number in exp (must be adjacent to num2)
     * @param num2 the index of a number in exp (must be adjacent to num1)
     * @return the operator between num1 and num2
     */
    private static String getOperator(int num1, int num2) {
        return exp[num1 + num2];
    }

    /**
     * Returns a number in dpArray based on the x/y coordinates passed in.
     * However, if x and y are the same, return the number it corresponds
     * in exp.
     * 
     * @param x the x coordinate for dpArray
     * @param y the y coordinate for dpArray
     * @return the value in dpArray[x][y] if x and y are different, otherwise,
     *         return the corresponding number in exp
     */
    private static int getNum(int x, int y) {
        return (x == y ? Integer.parseInt(exp[x * 2]) : dpArray[x][y]);
    }
}
