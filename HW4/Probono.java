import java.util.Scanner;
import java.util.ArrayList;

public class Probono {

    private static ArrayList<Integer> P = new ArrayList<>();
    private static ArrayList<ArrayList<Integer>> M = new ArrayList<>();
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int numOfJobs = input.nextInt();
        ArrayList<ArrayList<Integer>> jobs = new ArrayList<>();

        for (int i = 0; i < numOfJobs; i++) {
            jobs.add(i, new ArrayList<Integer>());
            jobs.get(i).add(0, input.nextInt());
            jobs.get(i).add(1, input.nextInt());
            jobs.get(i).add(2, input.nextInt());
        }

        input.close();

        jobs = mergeSort(jobs);

        System.out.println("SORTED LIST...");
        for (int i = 0; i < numOfJobs; i++) {
            System.out.println("[" + (i+1) + "] " + jobs.get(i).get(0) + " " + jobs.get(i).get(1) + 
            " " + jobs.get(i).get(2));
        }

        P.add(0, 0);
        P.add(1, 0);

        System.out.println("P-VALUES...\nP[1] = 0");
        for (int i = 1; i < numOfJobs; i++) {
            P.add(i+1, binarySearch(jobs, jobs.get(i).get(0), i));
            System.out.println("P[" + (i+1) + "] = " + P.get(i+1));
        }

        M.add(0, new ArrayList<Integer>());
        M.get(0).add(0, 0);
        M.get(0).add(1, 0);

        int largestSet = 0;

        System.out.println("M-VALUES...");
        int endTime = 0;
        for (int i = 0; i < numOfJobs; i++) {
            M.add(i, new ArrayList<Integer>());
        }

        System.out.println("LARGEST SET...");
        System.out.println(largestSet);
    }

    private static int binarySearch(ArrayList<ArrayList<Integer>> jobs, 
        int startTime, int end) {
            int L = 0;
            int R = end;
            int m = 0;

            while (L <= R) {
                m = (L + R) / 2;

                if (jobs.get(m).get(1) < startTime) {
                    L = m + 1;
                }
                else if (jobs.get(m).get(1) > startTime) {
                    R = m - 1;
                }
                else {
                    return m + 1;
                }
            }

            while (jobs.get(m).get(1) > startTime && m > 0) {
                m--;

                if (jobs.get(m).get(1) <= startTime) {
                    break;
                }
            }

            while (jobs.get(m).get(1) <= startTime && m >= 0) {
                m++;

                if (jobs.get(m).get(1) > startTime) {
                    return m;
                }
            }

            return 0;
    }

    private static ArrayList<ArrayList<Integer>> merge(
        ArrayList<ArrayList<Integer>> leftList,
        ArrayList<ArrayList<Integer>> rightList) {
        
        ArrayList<ArrayList<Integer>> mergedList = new ArrayList<>();

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