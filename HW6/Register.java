import java.util.Scanner;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * This program takes a list of students and courses (from standard input) and
 * returns the total maximum number of student-course pairings possible with
 * the given lists.
 * 
 * @author Dmitry Selin (des3358)
 */
public class Register {

    /**
     * The graph of students and courses that follows this design...
     * 
     * graph[v1][v2] -> the edge capacity from vertex v1 to vertex v2
     * 
     * The graph contains 2 more vertecies than the numbers given by standard
     * input, those vertecies being...
     * 
     * graph[0][0] -> the source (has an edge to every student)
     * graph[length-1][length-1] -> the sink (has an edge to every course)
     */
    private static int[][] graph;

    /**
     * The main method for the program that takes in students and courses from
     * standard input, puts them into a 2D array graph straucture, and calls
     * the Ford Fulkerson Algorithm that runs from the source to the sink,
     * which prints the maximum number of student-course pairings.
     * 
     * @param args command line arguments (not applicable)
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int numOfStudents = input.nextInt();
        int numOfCourses = input.nextInt();
        int sizeOfGraph = numOfCourses + numOfStudents + 2;
        graph = new int[sizeOfGraph][sizeOfGraph];

        // Skips the rest of the line and moves on to the next
        input.nextLine();

        // Iterates through every student and adds them to graph
        for (int i = 1; i <= numOfStudents; i++) {
            String[] student = input.nextLine().split(" ");

            // All edges from source to student have a capacity of 3 (since a
            // student can take no more than 3 courses)
            graph[0][i] = 3;

            // Connects student to their desired courses (edge cap = 1)
            for (int k = 0; k < student.length; k++) {
                int courseNum = Integer.parseInt(student[k]);
                graph[i][numOfStudents + courseNum] = 1;
            }
        } 

        // Connects every course to the sink (edge cap = course capacity)
        for (int i = numOfStudents + 1; i <= (sizeOfGraph - 2); i++) {
            graph[i][graph.length - 1] = input.nextInt();
        }

        input.close();

        // Returns max number of student-course pairings
        System.out.println(getPairings(0, sizeOfGraph - 1));
    }

    /**
     * This is an implementation of the Ford Fulkerson Algorithm that runs
     * from the source vertex to the sink vertex, modifying the graph in the
     * process (residual capacity).
     * 
     * @param source the start vertex
     * @param sink the end vertex
     * @return the maximum number of student-course pairings
     */
    public static int getPairings(int source, int sink) {
        int totalFlow = 0;

        // A random path from source to sink is found
        int[] path = findPath(source, sink);

        // As long as a path exists in graph, the algorithm continues to run
        while (path != null) {

            // Initilaizes starting values
            int x;
            int y = sink;
            int flow = -1;

            // Traverses the path and determines the max flow
            while (y != source) {
                x = path[y];

                if (flow < 0 || flow > graph[x][y]) {
                    flow = graph[x][y];
                }

                y = path[y];
            }

            // Updates the totalFlow to include the value of flow
            totalFlow += flow;
            y = sink;

            // Traverses the path again and updates the residual capacities
            while (y != source) {
                x = path[y];

                // Creates an an edge back to y from x
                graph[y][x] += flow;
                graph[x][y] -= flow;

                y = path[y];
            }

            path = findPath(source, sink);
        }

        return totalFlow;
    }

    /**
     * An implementation of Breadth-First Search that traverses graph from
     * start to end and returns a valid path if it exists.
     * 
     * @param start the start vertex
     * @param end the end vertex
     * @return a valid path from the start vertex to the end vertex if it 
     *         exists, otherwise returns null. Path is in the format of...
     * 
     *         path[x] -> the index of which vertex has an edge to vertex x
     */
    public static int[] findPath(int start, int end) {

        // The start vertex has a value of -1 (since it does not have a vertex
        // that points to it)
        int[] path = new int[graph[0].length];
        path[start] = -1;

        // Initilaizes the visited array (visited[x] -> visited vertex x)
        boolean[] visited = new boolean[graph[0].length];
        visited[start] = true;

        // Keeps track of vertexes that need to be visisted with a Queue
        Queue<Integer> visitNext = new ArrayDeque<>();
        visitNext.add(start);

        // As long as there are vertexes to be visited, the algorithm continues
        while (!visitNext.isEmpty()) {

            // Remove the next vertex in the queue
            int startVertex = visitNext.poll();

            // Iterates through all the edges of startVertex and checks if they
            // have been visted or exist
            for (int endVertex = 0; endVertex < graph[0].length; endVertex++) {
                if (!visited[endVertex] && graph[startVertex][endVertex] > 0) {
                    path[endVertex] = startVertex;

                    // If the end has been found, return the path
                    if (endVertex == end) {
                        return path;
                    }

                    visited[endVertex] = true;
                    visitNext.add(endVertex);
                }
            }
        }

        return null;
    }
}
