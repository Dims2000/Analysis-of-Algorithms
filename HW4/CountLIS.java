import java.util.Arrays;
import java.util.Scanner;

public class CountLIS {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        int n = Integer.parseInt(scan.nextLine());
        int[] nums = Arrays.stream(scan.nextLine().split(" ")).mapToInt(Integer::valueOf).toArray();

        scan.close();

        CountLIS countLIS = new CountLIS();

        int numSequences = countLIS.solve(nums, n);
        System.out.println(numSequences);
    }


    public int solve(int[] nums, int n){

        int longestIncreasingSubsequenceCount = 0;

        //maxLISLength holds the highest length of LIS
        int maxLISLength = 0;

        //dynamic holds the longest subsequence up to nums[i]
        int[] dynamic = new int[n];

        //count holds the number of lonbgest subsequences at nums[i]
        int[] count = new int[n];


        for(int i=0; i<n; i++){

            //initialize dynamic array and count
            dynamic[i] = 1;
            count[i] = 1;

            for(int j=i-1; j>=0; j--){
                if(nums[i]>nums[j]){
                    //if current element can be added to array.
                    if(dynamic[i] < dynamic[j]+1){
                        dynamic[i] = dynamic[j]+1;
                        //count is copied as it is creating new sequence by adding jth element
                        count[i] = count[j];
                    } else if(dynamic[i] == dynamic[j]+1){
                        //it means same length is found again
                        //add count of jth element to existing count
                        count[i] += count[j];
                    }
                }
                //check maxLISLength each iteration to keep it updated
                maxLISLength=Math.max(maxLISLength, dynamic[i]);
            }
        }

        //now go through and count the number of subsequences that have a length equal to the maxLISLength to find our answer

        for(int i=0; i<n; i++){
            if(dynamic[i] == maxLISLength){
                longestIncreasingSubsequenceCount += count[i];
            }
        }

        return longestIncreasingSubsequenceCount;

    }
}
