import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Random;

public class InputGenerator 
{
    public static int NUM_NODES = 100;
    public static int AVG_NEG = 1;
    public static Random rand = new Random();

    public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException
    {
        int [][] nums = getnerateNumbers();
        
        PrintWriter writer = new PrintWriter("input.txt", "UTF-8");

        writer.println(NUM_NODES + "\n");
        

        for(int i  = 0; i < NUM_NODES; i++)
        {
            for(int j  = 0; j < NUM_NODES; j++)
            {
                writer.print(nums[i][j] + " ");
            }
            writer.println();
        }
        
        writer.println();

        for(int i = 0; i < NUM_NODES; i++)
        {
            writer.print(rand.nextInt(100) + 1 + " ");
        }

        writer.close();
    }

    private static int[][] getnerateNumbers()
    {
        int nums[][] = new int[NUM_NODES][NUM_NODES];
        boolean[] rows = new boolean[NUM_NODES];
        boolean[] cols = new boolean[NUM_NODES];

        Arrays.fill(rows, false);
        Arrays.fill(cols, false);

        for(int i  = 0; i < NUM_NODES; i++)
        {
            for(int j  = 0; j < NUM_NODES; j++)
            {
                if(i == j)
                    nums[i][j] = 0;
                else
                {
                    if(rand.nextInt(AVG_NEG) == 0)
                        nums[i][j] = -1;
                    else
                    {
                        nums[i][j] = (rand.nextInt(100) + 1);
                        rows[i] = true;
                        cols[j] = true;
                    }
                }
            }
        }

        for(int i = 0; i < NUM_NODES; i++)
        {
            if(rows[i] == false)
            {
                if(i > 0)
                    nums[i][0] = (rand.nextInt(100) + 1);
                else
                    nums[i][1] = (rand.nextInt(100) + 1);
            }
            if(cols[i] == false)
            {
                if(i > 0)
                    nums[0][i] = (rand.nextInt(100) + 1);
                else
                    nums[1][i] = (rand.nextInt(100) + 1);
            }
        }
        
        return nums;
    }
}