import java.util.Scanner;

/**
 * StringConvert is a class containing a solution to CSCI-261 Homework 4 
 * Question 4.
 *
 * This program takes in two words and calculates the minimum cost to turn the
 * first word into the second word using:
 * 
 * Cost 3 delete a single character from x
 * Cost 4 insert a single character at any position in x
 * Cost 5 reaplce two consecutive characters in x with some other character
 *
 *  @author Max Burdett(mkb8422)
 */
public class StringConvert {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        String wordX = scan.nextLine();
        String wordY = scan.nextLine();

        scan.close();

        StringConvert stringConvert = new StringConvert();

        int cost = stringConvert.convertCost(wordX, wordY);

        System.out.println(cost);
    }

    /**
     * findSmallestValue is a method to find the smallest of the given integers
     * @param integers the integers to find the smallest of
     * @return the smallest integer of integers
     */
    public int findSmallestValue(int... integers){
        int smallest = Integer.MAX_VALUE;
        for(int cur: integers){
            if(cur < smallest){
                smallest = cur;
            }
        }
        return smallest;
    }

    public void print2dArray(int[][] arr){
        String printme = "";

        for(int i=0; i<arr.length; i++){
            for(int j=0; j<arr[0].length; j++){
                printme += arr[i][j] + " ";
            }
            printme += "\n";
        }

        System.out.println(printme);
    }


    /**
     * convertCost is a method for calculating the minimum cost to convert string x into string y using:
     * Cost 3 delete a single character from x
     * Cost 4 insert a single character at any position in x
     * Cost 5 reaplce two consecutive characters in x with some other character
     *
     *
     * @param x the String to be modified into String y
     * @param y the String that X is being modified to
     */
    public int convertCost(String x, String y){
        //Main idea is to create a dynamic array containing the minimum cost to find the suffix's of the words starting at I and J


        //first instantiate the array with y rows and x columns
        int[][] dynamic = new int[y.length()][x.length()];

        int xLength = x.length() - 1;
        int yLength = y.length() - 1;

        //set last col last row cell
        if(x.charAt(xLength) == y.charAt(yLength)) {
            dynamic[yLength][xLength] = 0;
        } else {
            dynamic[yLength][xLength] = 7; //represents a delete then insert
        }

        //next set the values of the last column and last row

        //set last row starting from second to last column(last col in last row should be either 0 or 7)
        for(int i=xLength-1; i>=0; i--){
            //add 3 each time because for each added character you simply need to remove it which has cost

            //Special case if the newest character in xsubstring is equal to y
            if(x.charAt(i) == y.charAt(yLength)) {
                //3 * number of other characters in ysubstring
                dynamic[yLength][i] = 3 * (xLength - i);
            } else {
                //if bottom right corner is 7 then there is a chance that replacing costs less than just removing

                int costOfRemoving = dynamic[yLength][i+1] + 3;

                //replace first two characters with correct character, remove all remaining characters
                int costOfReplacing = 5 + (3 * ((xLength - 1) - i)) ;

                dynamic[yLength][i] = findSmallestValue(costOfReplacing, costOfRemoving);
            }
        }

        //set last column starting from second to last row(again last col in last row is either 0 or 7)
        for(int i=yLength-1; i>=0; i--){
            //add 4 each time because for each character added you simply need to add one more character to make them match which has cost 4

            //special case if the newest character in ysubstring is equal to x then no need to execute an extra insertion
            if(y.charAt(i) == x.charAt(xLength)){
                dynamic[i][xLength] = 4 * (yLength - i);
            } else {
                dynamic[i][xLength] = dynamic[i+1][xLength] + 4;
            }
        }


        /**
         * now iterate through the remaining cells setting the value to be the max of:
         *         Dynamic[i+1][j] + 4 (value of cell one row below + 4)
         *         Dynamic[i][j-1] + 3 (value of cell one column after + 3)
         *         if size of X suffix substring > 2 then 5 + dynamic[i + 1][j + 2]
         *(swap first two characters and find the cost of converting the remaining x suffix substring and y suffix substring that was previously calculated)
         *         OR if the first character of each substring is the same then grab value from Dynamic[i+1][j+1]
         *(if the first two characters are then same then it has the same cost as removing both those characters, so we use the previously calculated cost)
         *
         */

        int costCreatedFromInserting = Integer.MAX_VALUE;
        int costCreatedFromDeleting = Integer.MAX_VALUE;
        int costCreatedFromReplacing = Integer.MAX_VALUE;
        int costFromDiagonal = Integer.MAX_VALUE;

        //iterate backwards through the columns row by row
        for(int i=yLength-1; i>=0; i--){
            //create the y suffix substring for this row
            String ySuffixSubStr = y.substring(i);

            for(int j=xLength-1; j>=0; j--){
                String xSuffixSubStr = x.substring(j);

                //First

                //otherwise find the max of the 3 other possibilities

                //cost from inserting one more character to the substring one column below
                //EXAMPLE: rd->red could also be achieved by rd->ed and inserting r at cost of 4
                costCreatedFromInserting =  dynamic[i+1][j] + 4;

                //cost from removing one more character from the substring one row to the right
                //EXAMPLE rd->red could also be achieved by subtracting r for cost of 3 and finding d->red
                costCreatedFromDeleting = dynamic[i][j+1] + 3;

                //case if they start with the same character then grab the cost of dynamic[i+1][j+1]
                if(xSuffixSubStr.charAt(0) == ySuffixSubStr.charAt(0)){
                    costFromDiagonal = dynamic[i+1][j+1];
                }

                if(xSuffixSubStr.length() > 2){
                    //cost from replacing first two characters + the cost of the remaining substrings
                    //EXAMPLE: eard->ored, replace ea with o plus the min cost of rd->red
                    costCreatedFromReplacing = 5 + dynamic[i+1][j+2];
                }

                dynamic[i][j] = findSmallestValue(costCreatedFromInserting, costCreatedFromDeleting, costCreatedFromReplacing, costFromDiagonal);

                //reset them all to max integer value each loop to avoid lots of if statements.
                costCreatedFromInserting = Integer.MAX_VALUE;
                costCreatedFromDeleting = Integer.MAX_VALUE;
                costCreatedFromReplacing = Integer.MAX_VALUE;
                costFromDiagonal = Integer.MAX_VALUE;

            }
        }

        return dynamic[0][0];

    }
}