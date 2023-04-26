import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * This program takes in edges and a list of unreliable spies from standard input and calculates the minimum cost needed
 * to connect the entire spy network without passing information through an unreliable spy
 *
 * @author Max Burdett(mkb8422)
 */
public class Spies {

    public static void main(String[] args) {
        /**
         * Input specification: the first line contains n and m (the number of vertices and edges of the graph,
         * i.e. the number of spies and connections), separated by a space. The vertices are labeled with numbers
         * 0, 1, 2, ..., n-1. The next line contains a single number, k, indicating the number of unreliable spies.
         * The next line contains k numbers, separated by spaces, indicating the vertices (spies) that are unreliable.
         * Then m lines follow. The i-th of these lines contains three numbers representing the first vertex of the i-th edge,
         * the second vertex of the i-th edge, and the cost of the i-th edge, separated by spaces. The edges are undirected.
         * For the purposes of this implementation (but not for your complexity argument), you may assume that n is at
         * most 1000 and that m is at most 10000. You may also assume that all edge costs are positive and fit in int,
         * and that the total cost of the network will fit in int as well. You may also assume that k < n. That is,
         * there is always at least one reliable spy.
         */

        Scanner scan = new Scanner(System.in);

        Spies spy = new Spies();

        //first line contains n and m the number of vertices and edges of the graph
        int[] nums = Arrays.stream(scan.nextLine().split(" ")).mapToInt(Integer::valueOf).toArray();
        int numVertices = nums[0];
        int numEdges = nums[1];

        Edge[] edges = new Edge[numEdges];

        //second line contains k, the number of unreliable spies
        int numBadSpies = Integer.parseInt(scan.nextLine());

        //third line contains k numbers, seperated by spaces indicating the vertices that are unreliable spies
        int[] unreliableSpies = Arrays.stream(scan.nextLine().split(" ")).mapToInt(Integer::valueOf).toArray();
        Set<Integer> unreliableSpySet = new HashSet<>();

        //turn into a set to make easier to work with
        for (int curSpy : unreliableSpies) {
            unreliableSpySet.add(curSpy);
        }

        //Next M lines follow with each line representing an edge and containing the first vertex, second vertex, and cost of the edge
        for (int i = 0; i < numEdges; i++) {
            int[] curEdge = Arrays.stream(scan.nextLine().split(" ")).mapToInt(Integer::valueOf).toArray();

            edges[i] = new Edge(curEdge[0], curEdge[1], curEdge[2]);
        }

//        System.out.println("numEdges = " + numEdges);
//        System.out.println("edges.length = " + edges.length);
//        System.out.println("edges[numEdges] = " + edges[numEdges]);

        int num = spy.createAdjacencyMatrix(edges, numVertices, unreliableSpySet);

        if (num == -1) {
            System.out.println("NONE");
        } else {
            System.out.println(num);
        }
    }

    public record Edge(int firstVertex, int secondVertex, int cost) {
    }


    /**
     * CreateAdjacencyMatrix creates an adjacency matrix representation of a graph from an array of {@link Edge} objects
     * and while doing so finds the total cost to go from the primary path to each spy edge if possible. This method
     * also calls on findShortestConnectingCost to find the minimum cost to connect the network with the created
     * adjacency matrix
     *
     * @param edges    the array of edge objects containing the information of each edge in the graph
     * @param numSpies is the number of vertices(spies) in the graph
     * @return the integer representing the minimum cost to connect the spy network
     */
    public int createAdjacencyMatrix(Edge[] edges, int numSpies, Set<Integer> unreliableSpies) {
        int[][] adjacencyMatrix = new int[numSpies][numSpies];

        int[] minCostUnreliableSpies = new int[numSpies];

        Arrays.fill(minCostUnreliableSpies, Integer.MAX_VALUE);


        for (int i = 0; i < edges.length; i++) {
            Edge curEdge = edges[i];

            //only add to adjacency matrix if both first and second vertices(spies) are reliable

            //  We split this into two parts of logic
            //      (1) we add the reliable spy to reliable spy edges to the adjacency matrix
            //      (2) we limit available edges to just the edges that go between 1 spy and 1 non-spy to find the
            //          minumum cost for each edge


            boolean firstSpyReliable = !unreliableSpies.contains(curEdge.firstVertex);
            boolean secondSpyReliable = !unreliableSpies.contains(curEdge.secondVertex);


            //(1) Create Adjacency Matrix
            if (firstSpyReliable && secondSpyReliable) {
                adjacencyMatrix[curEdge.firstVertex][curEdge.secondVertex] = curEdge.cost;
                adjacencyMatrix[curEdge.secondVertex][curEdge.firstVertex] = curEdge.cost;
            }


            //(2) first vertex reliable second vertex not reliable
            if (firstSpyReliable && !secondSpyReliable) {
                //only update the cost of the non-reliable spy
                if (curEdge.cost < minCostUnreliableSpies[curEdge.secondVertex]) {
                    minCostUnreliableSpies[curEdge.secondVertex] = curEdge.cost;
                }
            }

            //(2) first vertex unreliable second vertex reliable
            if (!firstSpyReliable && secondSpyReliable) {

                //only update the cost of the non-reliable spy
                if (curEdge.cost < minCostUnreliableSpies[curEdge.firstVertex]) {
                    minCostUnreliableSpies[curEdge.firstVertex] = curEdge.cost;
                }
            }
        }


        int spiesPathTotal = Arrays.stream(minCostUnreliableSpies).filter(i -> i != Integer.MAX_VALUE).sum();

        //Now find the shortest cost to connect the reliable spies
        int lowestCost = findShortestConnectingCost(adjacencyMatrix, numSpies, numSpies - unreliableSpies.size(), spiesPathTotal);


        return lowestCost;

    }

    /**
     * findShortestConnectingCost takes in an adjacency matrix and uses prim's algorithm to calculate the minimum cost
     * to connect the entire network of spies
     *
     * @param adjacencyMatrix  the adjacency representation of the graph only including the reliable spies
     * @param numSpies         the number of vertices in the graph
     * @param numReliableSpies the number of reliable vertices in the graph
     * @param costToReachSpies the cost to reach the unreliable spies from reliable spies
     * @return the minimum cost to connect the entire spy network
     */
    public int findShortestConnectingCost(int[][] adjacencyMatrix, int numSpies, int numReliableSpies, int costToReachSpies) {

//        print2dArray(adjacencyMatrix);

        //we should be able to run djikstras once, keeping track of the costs to visit each edge and the visited edges
        //if we didn't visit every reliable spy then there is no path
        //if we did visit every reliable spy then the sum of minCost should be the lowest cost to connect the reliable spies


        //pick a random starting point -> for this implementation i'm just picking the first reliable spy
        // in this case this is the first spy with any connections in the adjacency matrix
        int startingVertex = -1;
        loop:
        for (int i = 0; i < numSpies; i++) {
            for (int j = 0; j < numSpies; j++) {
                if (adjacencyMatrix[i][j] != 0) {
                    startingVertex = i;
                    break loop;
                }
            }
        }
        //shouldn't be possible unless there are no connections in the graph between reliable spies
        if (startingVertex == -1) {
            return -1;
        }


        //min cost keeps track of the minimum cost to get to each given vertex from any reliable adjacent vertex
        int[] minCost = new int[numSpies];
        Arrays.fill(minCost, Integer.MAX_VALUE);

        //path cost keeps track of the cost of the path to get to a given vertex from startingVertex
        int[] pathCost = new int[numSpies];
        Arrays.fill(pathCost, Integer.MAX_VALUE);

        //possible need for a set implementation so that we can check that all reliable spies have been visited (in faster time complexity)
        boolean[] visitedSpies = new boolean[numSpies];
        Arrays.fill(visitedSpies, false);

        //no cost to reach the starting vertex
        minCost[startingVertex] = 0;

        //loop through each vertex to find the shortest path
        for (int curVertex = 0; curVertex < numReliableSpies; curVertex++) {

            //find the lowest distance vertex that has not been visited yet (startingVertex = 0) so will always go first
            int minVertex = minOption(visitedSpies, minCost, numSpies);

            //assure that minVertex is not -1 AKA skip if there isnt an adjacent vertex available
            if (minVertex != -1) {

                //now minVertex is the lowest cost vertex that hasnt been visited yet

                //mark that we have visited this selected vertex
                visitedSpies[minVertex] = true;


                for (int adjacentVertex = 0; adjacentVertex < numSpies; adjacentVertex++) {

                    //update cost only if:
                    //not visited
                    //&& there is an edge from curVertex to adjacentVertex
                    //&& total cost of this path from the startingVertex to minVertex is smaller than the current
                    //pathCost[adjacentVertex]
                    //&& the pathCost is not the default
                    if (!visitedSpies[adjacentVertex]
                            && minCost[minVertex] != Integer.MAX_VALUE
                            && adjacencyMatrix[minVertex][adjacentVertex] != 0
                            && adjacencyMatrix[minVertex][adjacentVertex] < minCost[adjacentVertex]
                    ) {
                        //if this is the most efficient path to get to this adjacent vertex, then we know this is the
                        // lowest cost to get from any adjacent reliable spy to adjacentVertex

                        minCost[adjacentVertex] = adjacencyMatrix[minVertex][adjacentVertex];
                    }
                }

            }
        }

        int sum = 0;

        for (int i = 0; i < minCost.length; i++) {
            if (minCost[i] != Integer.MAX_VALUE) {
                sum += minCost[i];
            }
        }


        int pathSum = 0;
        for (int i = 0; i < pathCost.length; i++) {
            if (pathCost[i] != Integer.MAX_VALUE) {
                pathSum += pathCost[i];
            }
        }

        int visitedSum = 0;
        for (int i = 0; i < visitedSpies.length; i++) {
            if (visitedSpies[i]) {
                visitedSum++;
            }
        }

        int costSum = 0;
        for (int i = 0; i < visitedSpies.length; i++) {
            if (visitedSpies[i]) {
                costSum += minCost[i];
            }
        }


//        System.out.println("this shoudld hopefully be correct: " + (sum + costToReachSpies));

        //if we were unable to make it to every reliable spy in the network return -1 for failure
        if (visitedSum != numReliableSpies) {
            return -1;
        } else {
            return (sum + costToReachSpies);
        }

    }

    public int minOption(boolean[] visitedSpies, int[] minCost, int numSpies) {
        int lowestCost = Integer.MAX_VALUE;
        int minVertex = -1;
        for (int i = 0; i < numSpies; i++) {
            if (!visitedSpies[i] && minCost[i] < lowestCost) { //if not yet visited and cost is less than cur lowest cost
                lowestCost = minCost[i];
                minVertex = i;
            }
        }
        return minVertex;
    }


    /**
     * print2dArray is a helper method for printing a formatted 2d integer array
     *
     * @param arr the array to print
     */
    public void print2dArray(int[][] arr) {
        String outStr = "";

        for (int i = 0; i < arr.length; i++) {
            outStr += "{";
            for (int j = 0; j < arr[0].length; j++) {
                outStr += " " + arr[i][j];
                if (j != arr[0].length - 1) {
                    outStr += ",";
                }
            }
            outStr += "}, \n";
        }

        System.out.printf(outStr);
    }


    /**
     * Idea is to pick a random starting node, then go through one by one finding the lowest cost node to add exploring
     * layer by layer. we keep a set of visited nodes and an array containing the minimum cost to get to that node.
     *      if current path to node is lower cost than minCost[node] then set = newCost
     *      if current path to node is higher cost than minCost[node] then pass
     *
     * at the end we have the lowest cost to get to each spy in the network from another spy in the network, the sum of
     * these costs is the lowest possible cost to connect every spy.
     */


}