package HW2;
import java.util.Scanner;
import java.util.PriorityQueue;
/**
 * This program takes in a list of food as a series of tuples from standard
 * input and prints wheather a chef would be able to use all the food without
 * wasting anything. This program utulizes a Heap data structure in the form of
 * PriorityQueue.
 * 
 * @author Dmitry Selin (des3358)
 */
public class Food {

    /**
     * The min Heap data structure that stores all food that has arrived.
     * The root of this Heap is always the food that expires the soonest.
     */
    private static PriorityQueue<Integer> currentFood = new PriorityQueue<>();

    /**
     * The main method that takes a tuples of food (arrival_date, shelf_life),
     * adjusts the expiration date, and runs a simulation to determine if the
     * food can be used without any of it expiring.
     * 
     * @param args command line arguments (not applicable)
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int numOfFood = input.nextInt();

        // foodList[arrival_date][expiration_date]
        int[][] foodList = new int[numOfFood][2];

        for (int i = 0; i < numOfFood; i++) {
            foodList[i][0] = input.nextInt();

            // shelf_life + arrival_date = expiration_date
            foodList[i][1] = input.nextInt() + foodList[i][0];
        }

        input.close();

        
        // Keeps track of where in foodList the simulation is currently at
        int arrayPivot = 0;

        boolean noWaste = true;
        int currentDay = 0;
        while (currentDay < numOfFood) {

            // If any food arrive on currentDay, it does into the Heap
            for (int i = arrayPivot; i < numOfFood; i++) {
                if(foodList[i][0] == currentDay) {
                    currentFood.add(foodList[i][1]);
                }
                else
                    break;
                arrayPivot++;
            }

            // Each day, a food is used
            if (currentFood.size() > 0) {
                int consumedFood = currentFood.poll();

                // If the food is used after or on the expiration_date, return
                // FALSE - food is therefore wasted
                if (consumedFood <= currentDay) {
                    noWaste = false;
                    break;
                }
            }

            currentDay++;
        }

        // Prints output
        if (noWaste)
            System.out.println("YES");
        else
            System.out.println("NO");
    }
}
