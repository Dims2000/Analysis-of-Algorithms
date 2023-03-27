import java.util.ArrayList;
import java.util.Random;

/**
 * The SortingTest class tests the time complexity of Merge Sort, Insertion
 * Sort, and BucketSort.
 * 
 * @author Dmitry Selin (des3358)
 */
public class SortingTest {

    /**
     * The main methods of the class that creates an Arraylist of random
     * values and runs Merge Sort, Insertion Sort, and Bucket Sort (while
     * recording the run time of each algorithm)
     * 
     * @param args command line arguments (not applicable)
     */
    public static void main(String args[]) {
        int n = 100000;     // the number of values in an array

        // initializes an array of random floating point values
        ArrayList<Float> list = new ArrayList<Float>();
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            list.add(i, (float)(rand.nextGaussian() * (1/10000) + 0.5));
        }

        // runs Insertion Sort and Bucket Sort
        insertionSort(list, true);
        bucketSort(list);

        // runs Merge Sort
        long start = System.nanoTime();
        list = mergeSort(list);
        System.out.println("Merge Sort (nanoseconds): " + 
        (System.nanoTime() - start));
    }

    /**
     * Sorts list by Insertion Sort.
     * 
     * @param list an ArrayList of floating point values
     * @param doesPrintTime if TRUE, print out the run time of the sort
     * @return the sorted list
     */
    private static ArrayList<Float> insertionSort(ArrayList<Float> list, 
    boolean doesPrintTime) {
        long start = System.nanoTime();

        for (int i = 1; i < list.size(); i++) {
            int j = i;

            while (j > 0 && (list.get(j - 1) > list.get(j))) {
                float temp = list.get(j);
                list.set(j, list.get(j - 1));
                list.set(j - 1, temp);
                j--;
            }
        }

        if (doesPrintTime) {
            System.out.println("Insertion Sort (nanoseconds): " + 
            (System.nanoTime() - start));
        }
            
        return list;
    }

    /**
     * Sorts list by Bucket Sort
     * 
     * @param list an unsorted ArrayList of floating point values
     * @return
     */
    private static ArrayList<Float> bucketSort(ArrayList<Float> list) {
        long start = System.nanoTime();
        int size = list.size();

        // creates buckets
        ArrayList<ArrayList<Float>> buckets = 
        new ArrayList<ArrayList<Float>>();
        for (int i = 0; i < size; i++) {
            buckets.add(new ArrayList<Float>());
        }

        // puts list values into buckets
        for (int i = 0; i < size; i++) {
            float value = list.get(i);
            buckets.get((int)(value * size)).add(value);
        }

        // sorts buckets by Insertion Sorts
        for (int i = 0; i < size; i++) {
            buckets.set(i, insertionSort(buckets.get(i), false));
        }

        // conjoins all buckets
        int pointer = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < buckets.get(i).size(); j++) {
                list.set(pointer, buckets.get(i).get(j));
                pointer++;
            }
        }

        System.out.println("Bucket Sort (nanoseconds): " + 
        (System.nanoTime() - start));
        return list;
    }

    private static ArrayList<Float> mergeSort(ArrayList<Float> list) {
        if (list.size() == 1) {
            return list;
        }

        ArrayList<Float> leftList = new ArrayList<Float>();
        for (int i = 0; i < (list.size()/2); i++) {
            leftList.add(list.get(i));
        }

        ArrayList<Float> rightList = new ArrayList<Float>();
        for (int i = (list.size()/2); i < list.size(); i++) {
            rightList.add(list.get(i));
        }

        leftList = mergeSort(leftList);
        rightList = mergeSort(rightList);

        return merge(leftList, rightList);
    }

    /**
     * The merge method for Merge Sort that sorts and conjoins leftList
     * and rightList.
     * 
     * @param leftList a sorted ArrayList
     * @param rightList a sorted ArrayList
     * @return a single coinjoined sorted ArrayList
     */
    private static ArrayList<Float> merge(ArrayList<Float> leftList,
    ArrayList<Float> rightList) {
        ArrayList<Float> mergedList = new ArrayList<Float>();

        int leftPointer = 0;
        int rightPointer = 0;

        while (leftList.size() > 0 && rightList.size() > 0) {
            if (leftList.get(leftPointer) > rightList.get(rightPointer)) {
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
}
