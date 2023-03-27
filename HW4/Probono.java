import java.util.Scanner;
import java.util.ArrayList;

/**
 * This class takes a series of jobs (startTime, endTime, canBeProbono) from
 * standard input and returns the maximum number of jobs that can be performed
 * without any overlaps with one or more jobs being done pro bono.
 * 
 * @author Dmitry Selin (des3358)
 */
public class Probono {

    /**
     * An ArrayList that contains entries in the following format...
     * 
     * P[job index (starting at 1)] = index of previous next non-overlpping job
     */
    private static ArrayList<Integer> P = new ArrayList<>();

    /**
     * A 2D ArrayList containing entries in the following format...
     * 
     * M[job index (starting at 1)][0] = The largest number of compatible jobs
     *                                   regardless of pro bono status up to
     *                                   the specified job index
     * 
     * M[job index (starting at 1)][1] = The largest number of compatible jobs
     *                                   where at least 1 job is pro bono up to
     *                                   the specified job index
     */
    private static ArrayList<ArrayList<Integer>> M = new ArrayList<>();

    /**
     * This is the main method in the program that sorts the array of jobs by
     * endTime, populates the P array, then populates the M array. Finally, the
     * result (largest number of non-overlapping jobs with at least 1 pro bono
     * job) is printed as it is the last element in the M array.
     * 
     * @param args command line arguments (not applicable)
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Gets the number of jobs from standard input
        int numOfJobs = input.nextInt();

        // Inserts jobs into the jobs ArrayList...
        // jobs[i][0] = startTime, 
        // jobs[i][1] = endTime, 
        // jobs[i][2] = canBeProbono
        ArrayList<ArrayList<Integer>> jobs = new ArrayList<>();
        for (int i = 0; i < numOfJobs; i++) {
            jobs.add(i, new ArrayList<Integer>());
            jobs.get(i).add(0, input.nextInt());
            jobs.get(i).add(1, input.nextInt());
            jobs.get(i).add(2, input.nextInt());
        }

        input.close();

        // Sorts the jobs by their endTime using Merge Sort
        jobs = mergeSort(jobs);

        // Initializes the P ArrayList
        P.add(0, 0);
        P.add(1, 0);

        // Populates the P ArrayList (with Binary Search)
        for (int i = 1; i < numOfJobs; i++) {

            // Binary Search locates the previous next non-overlapping job
            P.add(i+1, binarySearch(jobs, jobs.get(i).get(0), i));
        }

        // Initializes the M ArrayList
        M.add(0, new ArrayList<Integer>());
        M.get(0).add(0, 0);
        M.get(0).add(1, 0);

        // Populates the M ArrayList
        for (int i = 1; i <= numOfJobs; i++) {
            M.add(i, new ArrayList<Integer>());

            // Determines max number of jobs up to i (regardless of pro bono)
            int opt1 = 1 + M.get(P.get(i)).get(0);
            int opt2 = M.get(i-1).get(0);
            M.get(i).add(0, Math.max(opt1, opt2));

            // If a job is pro bono, update the max number of jobs up to i 
            // (including at least 1 pro bono job)
            if (jobs.get(i-1).get(2) == 1) {
                opt2 = M.get(i-1).get(1);
                M.get(i).add(1, Math.max(opt1, opt2));
            }
            // If a pro bono job is already included, update the number of jobs
            // up to i (including at least 1 pro bono job)
            else if (M.get(i-1).get(1) > 0) {
                opt1 = 1 + M.get(P.get(i)).get(1);
                opt2 = M.get(i-1).get(1);
                M.get(i).add(1, Math.max(opt1, opt2));
            }
            // If no other conditions for pro bono are met, it must remain 0
            else {
                M.get(i).add(1, 0);
            }
        }

        // Prints final result (largest number of non-overlapping jobs that
        // includes at least 1 pro bono job)
        System.out.println(M.get(numOfJobs).get(1));
    }

    /**
     * A binary search implementation that searches the jobs array for the
     * desired startTime of a job in jobs. If startTime is not found, the index
     * of the next most compatible job is returned. Additionally, if more than
     * one job has the same startTime, the index of the "right-most" job is
     * returned.
     * 
     * @param jobs an array of jobs sorted by endTime
     * @param startTime the start time to search for in the jobs array
     * @param end the last job index to look at (uses the range [0:end])
     * @return the index of previous next non-overlapping job in jobs 
     *         (new index starting at 1)
     */
    private static int binarySearch(ArrayList<ArrayList<Integer>> jobs, 
        int startTime, int end) {

            // Initializes the starting values
            int L = 0;
            int R = end;
            int m = 0;

            // Standard Binary Search implementation
            while (L <= R) {
                m = (L + R) / 2;

                if (jobs.get(m).get(1) < startTime) {
                    L = m + 1;
                }
                else if (jobs.get(m).get(1) > startTime) {
                    R = m - 1;
                }
                else {
                    break;
                }
            }

            // Moves index if Binary Search returns incorrect value
            while (jobs.get(m).get(1) > startTime && m > 0) {
                m--;

                if (jobs.get(m).get(1) <= startTime) {
                    break;
                }
            }

            // In case of a tie, the "right-most" index is returned
            while (jobs.get(m).get(1) <= startTime && m >= 0) {
                m++;

                if (jobs.get(m).get(1) > startTime) {
                    return m;
                }
            }

            // If no other conditons are met, there must be no compatible jobs
            return 0;
    }

    /**
     * The merge method utilized by Merge Sort that takes two sorted 
     * ArrayLists, merges and sorts them simultaneously.
     * 
     * @param leftList a sorted ArrayList
     * @param rightList a sorted ArrayList
     * @return ArrayList comprising of leftList and rightList (merged & sorted)
     */
    private static ArrayList<ArrayList<Integer>> merge(
        ArrayList<ArrayList<Integer>> leftList,
        ArrayList<ArrayList<Integer>> rightList) {
        
        ArrayList<ArrayList<Integer>> mergedList = new ArrayList<>();

        int leftPointer = 0;
        int rightPointer = 0;

        while (leftList.size() > leftPointer && rightList.size() > rightPointer) {
            if (leftList.get(leftPointer).get(1) > 
                rightList.get(rightPointer).get(1)) {
                
                mergedList.add(rightList.get(rightPointer));
                rightPointer++;
            }
            else {
                mergedList.add(leftList.get(leftPointer));
                leftPointer++;
            }
        }

        while (leftList.size() > leftPointer) {
            mergedList.add(leftList.get(leftPointer));
            leftPointer++;
        }

        while (rightList.size() > rightPointer) {
            mergedList.add(rightList.get(rightPointer));
            rightPointer++;
        }

        return mergedList;
    }

    /**
     * A standard implementation of Merge Sort that sorts an ArrayList of jobs
     * by their endTimes.
     * 
     * @param list an unsorted ArrayList of jobs
     * @return an ArrayList of jobs sorted by endTime
     */
    private static ArrayList<ArrayList<Integer>> mergeSort(
        ArrayList<ArrayList<Integer>> list) {
        
        if (list.size() == 1) {
            return list;
        }

        ArrayList<ArrayList<Integer>> leftList = new ArrayList<>();
        for (int i = 0; i < (list.size()/2); i++) {
            leftList.add(list.get(i));
        }

        ArrayList<ArrayList<Integer>> rightList = new ArrayList<>();
        for (int i = (list.size()/2); i < list.size(); i++) {
            rightList.add(list.get(i));
        }

        leftList = mergeSort(leftList);
        rightList = mergeSort(rightList);

        return merge(leftList, rightList);
    }
}