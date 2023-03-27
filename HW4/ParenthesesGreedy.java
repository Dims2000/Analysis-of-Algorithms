import java.util.Scanner;
import java.util.ArrayList;

public class ParenthesesGreedy {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        String[] exp = input.nextLine().split(" ");

        input.close();

        ArrayList<String> addedExp = new ArrayList<>();
        for (int i = 0; i < exp.length; i++) {

            if (exp[i].equals("+")) {
                int num1 = Integer.parseInt(addedExp.get(addedExp.size() - 1));
                int num2 = Integer.parseInt(exp[i+1]);
                int sum = num1 + num2;
                addedExp.set(addedExp.size() - 1, "" + sum);
                i++;
            } else if (exp[i].equals("*")) {
                addedExp.add("*");
            }
            else {
                addedExp.add(exp[i]);
            }
        }

        int finalAnswer = Integer.parseInt(addedExp.get(0));
        for (int i = 1; i < addedExp.size(); i++) {
            finalAnswer = finalAnswer * Integer.parseInt(addedExp.get(i+1));
            i++;
        }

        System.out.println(finalAnswer);
    }
}
