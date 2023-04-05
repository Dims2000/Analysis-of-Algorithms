import java.util.Scanner;

public class Dynamite {

    private static int[][] maze;
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int numOfRows = input.nextInt();
        int numOfCols = input.nextInt();

        maze = new int[numOfRows][numOfCols];
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfCols; j++) {
                maze[i][j] = input.nextInt();
            }
        }

        input.close();
    }
}