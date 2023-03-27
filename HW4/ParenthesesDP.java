import java.util.Scanner;

public class ParenthesesDP {

    private static int[][] dpArray;
    private static String[] exp;
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        exp = input.nextLine().split(" ");
        int numOfNums =(exp.length + 1)/2;

        input.close();
        
        dpArray = new int[numOfNums][numOfNums];

        int j = 0;
        for (int i = 1; i < numOfNums; i++) {
            int num1 = getNum(j);
            int num2 = getNum(i);
            String symbol = getSymbol(j, i);

            dpArray[j][i] = performOperation(num1, num2, symbol);
            j++;
        }

        print2DArray(dpArray);
    }

    private static int performOperation(int num1, int num2, String symbol) {
        if (symbol.equals("+")) {
            return num1 + num2;
        }

        return num1 - num2;
    }

    private static int getNum(int order) {
        return Integer.parseInt(exp[order * 2]);
    }

    private static String getSymbol(int num1, int num2) {
        return exp[num1 + num2];
    }

    private static void print2DArray(int[][] array) {
        for (int[] row : array) {
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }
}
