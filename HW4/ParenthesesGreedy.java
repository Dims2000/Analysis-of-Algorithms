import java.util.Scanner;
import java.util.ArrayList;

/**
 * This class takes a mathematical expression made of summations and
 * multiplications and outputs the largest possible value that can be
 * made with that expression by parenthesizing it.
 * 
 * @author Dmitry Selin (des3358)
 */
public class ParenthesesGreedy {

    /**
     * This is the main method of the program that iterates through all the
     * values in the expression twice. The first iteration, all the addition
     * operations are performed. In the second iteration, all the
     * multiplications are performed. This yeilds the largest possible result.
     * 
     * @param args command line arguments (not applicable)
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // The expression is recieved from standard input and split by spaces
        String[] exp = input.nextLine().split(" ");

        input.close();

        // Iterates through the values in the expression, performing addition
        ArrayList<String> addedExp = new ArrayList<>();
        for (int i = 0; i < exp.length; i++) {

            // If a summation is found, perform it and insert its result into
            // the addedExp ArrayList
            if (exp[i].equals("+")) {
                int num1 = Integer.parseInt(addedExp.get(addedExp.size() - 1));
                int num2 = Integer.parseInt(exp[i+1]);
                int sum = num1 + num2;
                addedExp.set(addedExp.size() - 1, "" + sum);
                i++;
            
            // If the next value is not addition, move on
            } else if (!exp[i].equals("*")) {
                addedExp.add(exp[i]);
            }
        }

        // Second iteration, multiply every value in addedExp
        int finalAnswer = Integer.parseInt(addedExp.get(0));
        for (int i = 1; i < addedExp.size(); i++) {
            finalAnswer = finalAnswer * Integer.parseInt(addedExp.get(i));
        }

        // Print result
        System.out.println(finalAnswer);
    }
}
