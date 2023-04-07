import java.util.Scanner;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * This program takes in a maze from standard input that has open spaces (0),
 * walls (1), a start point (2), and an end point (3). The program returns the
 * minimum number of steps needed to get through the maze while using dynamite
 * on, at most, one wall.
 * 
 * @author Dmitry Selin (des3358)
 */
public class Dynamite {

    /** A 2D array that contains the entire maze given by the user */
    private static int[][] maze;

    /**
     * A Queue used by the Breadth-First Search algorithm to keep track of
     * which parts of the maze need to be traversed to. The Queue stores
     * Strings that contain 4 pieces of information delimited by commas...
     * 
     * EXAMPLE: "x, y, usedDynamite, numOfSteps"
     * 
     * x -> the x coordinate of the current position in the maze
     * y -> the y coordinate of the current position in the maze
     * usedDynamite -> '1' if a wall HAS been broken down, '0' otherwise
     * numOfSteps -> the number of steps taken so far
     */
    private static Queue<String> visitNext = new ArrayDeque<>();

    /**
     * The main method of the program that takes in the maze from standard
     * input and runs a modified Breadth-First Search algorithm on the maze.
     * 
     * @param args command line arguments (not applicable)
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int numOfRows = input.nextInt();
        int numOfCols = input.nextInt();

        // Gets the maze from standard input
        maze = new int[numOfRows][numOfCols];
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfCols; j++) {
                maze[i][j] = input.nextInt();
            }
        }

        input.close();

        // Prints out the result of Breadth-First Search
        System.out.println(bfs());
    }

    /**
     * A modified Breadth-First Search algorithm that incorperates step 
     * tracking and another tracker for if the dynamite was used. The search
     * ends when the end of the maze is reached.
     * 
     * @return the minimum number of steps needed to get from the start of the
     *         maze to the end (while using dynamite on, up to, one wall)
     */
    private static int bfs() {
        int solvedSteps = Integer.MAX_VALUE;

        // The start of the maze is always at the top left corner
        visitNext.add("0,0,0,0");

        // The main loop for Breadth-First Search
        do {
            // The top entry in the Queue is removed
            String[] values = visitNext.poll().split(",");

            // All values are derived from the Queue String
            int x = Integer.parseInt(values[0]);
            int y = Integer.parseInt(values[1]);
            boolean usedDynamite = values[2].equals("1");
            int numOfSteps = Integer.parseInt(values[3]);

            // If the end has been reached and the path is shorter, skip the
            // rest of the steps
            if (maze[x][y] == 3 && solvedSteps > numOfSteps) {
                solvedSteps = numOfSteps;
                continue;
            }
            
            // If this space has been traversed from a path that has not yet
            // used dynamite, replace the maze location with '8', otherwise,
            // replace it with '9' (NOTE: walls are never replaced)
            if (maze[x][y] != 1) {
                maze[x][y] = (usedDynamite ? 8 : 9);
            }

            // Add all 4 neighbors of the current position to the Queue 
            // (if they are valid)
            addToQueue(x+1, y, usedDynamite, numOfSteps);
            addToQueue(x-1, y, usedDynamite, numOfSteps);
            addToQueue(x, y+1, usedDynamite, numOfSteps);
            addToQueue(x, y-1, usedDynamite, numOfSteps);
        
        // Only end Breadth-First Search when the Queue is empty
        } while (!visitNext.isEmpty());

        return solvedSteps;
    }

    /**
     * The method that checks if a maze position is valid to be put into the
     * vistNext Queue, then inserts the correct String into the Queue.
     * 
     * @param x the x coordinate of the current position in the maze
     * @param y the y coordinate of the current position in the maze
     * @param usedDynamite if dynamite has been used so far
     * @param numOfSteps the number of steps taken to get to this point
     */
    private static void addToQueue(int x, int y, 
        boolean usedDynamite, int numOfSteps) {

        // If the (x,y) location is out of bounds, nothing happens
        if (y >= 0 && y < maze[0].length && x >= 0 && x < maze.length) {
            String queueString = null;
            int mazeValue = maze[x][y];

            // If the current position is an open space, the end, or (an open
            // space that has previously been traversed by a path that has
            // used dynamite AND this path has not used dynamite), add a String
            // to the Queue 
            if (mazeValue == 0 || 
                mazeValue == 3 || 
                (mazeValue == 8 && !usedDynamite)
            ) {
                queueString = x + "," + y + "," + (usedDynamite ? "1" : "0") +
                              "," + (numOfSteps+1);
            }
            // If the current position is a wall and dyanmite has not been used
            // so far, break the wall and add a Strng to Queue
            else if (mazeValue == 1 && !usedDynamite) {
                queueString = x + "," + y + "," + "1" + "," + (numOfSteps+1);
            }

            if (queueString != null) {
                visitNext.add(queueString);
            }
        }
    }
}