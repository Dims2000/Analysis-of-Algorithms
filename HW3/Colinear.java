import java.util.Scanner;
import java.util.ArrayList;

/**
 * A program that takes 2D points as input and outputs a simple "YES" or "NO"
 * depending on if there exists 3 points that are co-linear and evenly spaced
 * 
 * @author Dmitry Selin (des3358)
 */
public class Colinear {

    /**
     * The main method of the program that takes the data points from standard
     * input and calls a method to determine if there exists 3 points that are
     * co-linear and evently spaced.
     * 
     * @param args command line arguments (not applicable)
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int numOfPoints = input.nextInt();
        int[][] points = new int[numOfPoints][2];

        // The 2D coordinates are stored in a 2D array (x, y)
        for (int i = 0; i < numOfPoints; i++) {
            points[i][0] = input.nextInt();
            points[i][1] = input.nextInt();
        }

        input.close();
        colinear(points);
    }

    /**
     * A method that run in O(n^2log(n)) time that determines if 2 points
     * in the points array are co-linear and evenly spaced.
     * 
     * @param points the array of 2D coordinates (x, y)
     */
    private static void colinear(int[][] points) {

        // Iterates through each point in the array
        for (int i = 0; i < points.length; i++) {

            // The distances array is also a 2D array (indexOfPoint, distance)
            ArrayList<ArrayList<Double>> distances = new ArrayList<>();

            // The slopes array is a simple 1D array of slopes
            ArrayList<Double> slopes = new ArrayList<>();

            // Iterates through every point in points again (comparing to ith
            // point)
            for (int j = 0; j < points.length; j++) {

                // Calculates distance between points i and j
                double dist = Math.sqrt(
                    Math.pow((points[j][1] - points[i][1]), 2) + 
                    Math.pow((points[j][0] - points[i][0]), 2)
                );

                // Calculates slope between i and j (if it can be defined)
                double slope = Double.NaN;
                if (points[j][0] - points[i][0] != 0) {
                    slope = (float)(points[j][1] - points[i][1])/
                    (float)(points[j][0] - points[i][0]);
                }

                ArrayList<Double> temp1 = new ArrayList<>();
                temp1.add(Double.parseDouble(j + ""));
                temp1.add(Double.parseDouble(dist + ""));
                distances.add(temp1);

                slopes.add(slope);
            }

            // Distances are sorted by shortest to longest <-- O(nlog(n))
            distances = mergeSort(distances);

            // Then, all duplicates in the sorted distances is found <--- O(n)
            ArrayList<ArrayList<Integer>> duplicates = findDuplicates(distances);

            // If there exists 2 points that have are the same distance apart
            // from point i (and are on the same line), then return YES
            for (int k = 0; k < duplicates.size(); k++) {
                double slope1 = slopes.get(duplicates.get(k).get(0));
                double slope2 = slopes.get(duplicates.get(k).get(1));

                if (slope1 == slope2) {
                    System.out.println("YES");
                    return;
                }
            }
        }

        System.out.println("NO");
    }

    /**
     * A method that searches through an (already storted) ArrayList and find
     * pairs of points that have the same distance.
     * 
     * @param points a 2D ArrayList of distances (index, distance)
     * @return an ArrayList of indexes that have the same distance
     */
    private static ArrayList<ArrayList<Integer>> findDuplicates(
        ArrayList<ArrayList<Double>> points) {

        ArrayList<ArrayList<Integer>> duplicates = new ArrayList<>();

        // Goes through each point in points linearly
        for (int i = 0; i < points.size() - 1; i++) {
            if (points.get(i).get(1).equals(points.get(i + 1).get(1))) {
                ArrayList<Integer> temp = new ArrayList<>();
                temp.add(points.get(i).get(0).intValue());
                temp.add(points.get(i + 1).get(0).intValue());
                duplicates.add(temp);
            }
        }

        return duplicates;
    }

    /**
     * The merge method for Merge Sort that sorts and conjoins leftList
     * and rightList.
     * 
     * @param leftList a sorted ArrayList
     * @param rightList a sorted ArrayList
     * @return a single coinjoined sorted ArrayList
     */
    private static ArrayList<ArrayList<Double>> merge(
        ArrayList<ArrayList<Double>> leftList,
        ArrayList<ArrayList<Double>> rightList) {
        
        ArrayList<ArrayList<Double>> mergedList = new ArrayList<>();

        int leftPointer = 0;
        int rightPointer = 0;

        while (leftList.size() > 0 && rightList.size() > 0) {
            if (leftList.get(leftPointer).get(1) > 
                rightList.get(rightPointer).get(1)) {
                
                mergedList.add(rightList.get(rightPointer));
                rightList.remove(rightPointer);
            }
            else {
                mergedList.add(leftList.get(leftPointer));
                leftList.remove(leftPointer);
            }
        }

        while (leftList.size() > 0) {
            mergedList.add(leftList.get(leftPointer));
            leftList.remove(leftPointer);
        }

        while (rightList.size() > 0) {
            mergedList.add(rightList.get(rightPointer));
            rightList.remove(rightPointer);
            }

        return mergedList;
    }

    /**
     * The Merge Sort algorithm - runs in O(nlogn) time.
     * 
     * @param list an unsorted 2D ArrayList of distances (index, distance)
     * @return a sorted ArrayList based on distance
     */
    private static ArrayList<ArrayList<Double>> mergeSort(
        ArrayList<ArrayList<Double>> list) {
        
        if (list.size() == 1) {
            return list;
        }

        ArrayList<ArrayList<Double>> leftList = new ArrayList<>();
        for (int i = 0; i < (list.size()/2); i++) {
            leftList.add(list.get(i));
        }

        ArrayList<ArrayList<Double>> rightList = new ArrayList<>();
        for (int i = (list.size()/2); i < list.size(); i++) {
            rightList.add(list.get(i));
        }

        leftList = mergeSort(leftList);
        rightList = mergeSort(rightList);

        return merge(leftList, rightList);
    }
}