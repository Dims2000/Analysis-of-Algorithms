package HW2;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * The Bully class takes an array of integers (representing an order of
 * students and the value of their lunches) and returns the number of
 * incidents of bullying that would occur with the given order and values.
 * 
 * @author Dmitry Selin (des3358)
 */
public class Bully {

    /**
     * The array of students (order of the array = age, 
     * elements = values of lunches)
     */
    private static ArrayList<Integer> students = new ArrayList<>();

    /**
     * The main method of the program that takes an arry of students from
     * standard input, calls the function that counts the number of
     * incidents of bullying and outputs the returned value to standard output
     * 
     * @param args command line arguments (not applicable)
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Populates the students array
        int numOfStudents = input.nextInt();
        for (int i = 0; i < numOfStudents; i++) {
            students.add(i, input.nextInt());
        }

        input.close();

        int pivot = 0;
        int numOfIncidents = 0;

        // Seperates students into sections by where elements of -1 are
        for (int i = 0; i < numOfStudents; i++) {
            if (students.get(i) == -1) {
                numOfIncidents += countIncidents(pivot, i - 1);
                pivot = i + 1;
            }
        }
        numOfIncidents += countIncidents(pivot, numOfStudents - 1);

        // Prints the number of incidents of bullying
        System.out.println(numOfIncidents);
    }

    /**
     * A recursive function that counts the number of incidents of bullying
     * by utilizing the divide and conquer algorithm style.
     * 
     * @param left the leftmost index of the students array
     * @param right the rightmost index of the students array
     * @return the number of incidents of bullying
     */
    private static int countIncidents(int left, int right){
        // Base Case = if the range is for 2 or less elements
        if (right - left <= 1) {

            // If neither element is -1 and if the right element is bigger then
            // swap the elements (older child gets better lunch)
            if (students.get(left) < students.get(right)) {
                swap(left, right);
                return 1;
            }

            return 0;
        }

        int middleIndex = (left + right) / 2;

        // Splits the array indicies and recirsively counts incidents
        int leftIncidents = countIncidents(left, middleIndex);
        int rightIncidents = countIncidents(middleIndex + 1, right);

        int totalIncidents = leftIncidents + rightIncidents;

        // Creates the sub-arrays for left and right
        ArrayList<Integer> leftArray = 
            new ArrayList<>(students.subList(left, middleIndex + 1));
        ArrayList<Integer> rightArray = 
            new ArrayList<>(students.subList(middleIndex + 1, right + 1));

        int positionPivot = left;
        int leftPivot = 0;
        int rightPivot = 0;

        // Iterates through each element in leftArray and rightArray once
        while (positionPivot <= right) {

            // Catches any OutOfBounds Exceptions
            if (leftPivot >= leftArray.size()) {
                students.set(positionPivot, rightArray.get(rightPivot));
                rightPivot++;
            }
            else if (rightPivot >= rightArray.size()) {
                students.set(positionPivot, leftArray.get(leftPivot));
                leftPivot++;
            }
            else {
                if (leftArray.get(leftPivot) >= rightArray.get(rightPivot)) {
                    students.set(positionPivot, leftArray.get(leftPivot));
                    leftPivot++;
                }
                // Counts inversions - adds to totalIncidents
                else {
                    totalIncidents += leftArray.size() - leftPivot;
                    students.set(positionPivot, rightArray.get(rightPivot));
                    rightPivot++;
                }
            }

            positionPivot++;
        }

        return totalIncidents;
    }

    /**
     * Swaps two elements in the students array.
     * 
     * @param posA one position in the students array to be swapped with posB
     * @param posB position in students array to be swapped with posA
     */
    private static void swap(int posA, int posB) {
        int temp = students.get(posA);
        students.set(posA, students.get(posB));
        students.set(posB, temp);
    }
}