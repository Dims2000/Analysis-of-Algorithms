import java.util.Scanner;

/**
 * This program takes a binary string from standard input and outputs
 * the number of possible decodings from a code that was given to us.
 * 
 * @author Dmitry Selin (des3358)
 */
public class Decodings {

    /**
     * The main method of the program that simply take a binary string
     * from standard input and outputs the number of decodings.
     * 
     * @param args command line arguments (not applicable)
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String code = input.nextLine();
        input.close();

        System.out.println(getDecodings(code));
    }

    /**
     * This method keeps track of all possible decodings using a state-machine
     * that shifts different states around based on the binary digits that are
     * entered. This state machine is based on the code provided.
     * 
     * @param code a binary string
     * @return the number of decodings as an integer
     */
    private static int getDecodings(String code) {

        // The initial state of the machine (completeState is most important)
        int completeState = 0;
        int cState = 0;
        int dState = 0;
        int eState = 0;
        int fState = 0;

        // Increments through each binary digit once
        for (int i = 0; i < code.length(); i++) {
            int temp = 0;

            // If the machine has yielded any decodings, restart it
            if (completeState == 0)
                completeState++;

            // Shifts states based on an input of 0
            if (code.charAt(i) == '0') {

                // These states get discarded (lead to invalid results)
                fState = 0;
                eState = 0;
                dState = 0;

                dState = completeState;
                completeState += cState;
                cState = 0;
            }
            // Shifts the states based on an input of 1
            else {
                temp = cState;
                cState = completeState;

                completeState += eState;
                completeState += dState;
                completeState += fState;

                eState = temp;
                fState = dState;
                dState = 0;
            }
        }

        return completeState;
    }
}
