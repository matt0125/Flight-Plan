import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.*;


// Input: Weighted undirected graph
//  N
//  NxN matrix
//  N length array of wait times for each station


public class Project
{
  private static int[][] _matrix;
  private static int _numNodes;
  private static int _turnTime;
  private static final int INFINITY = (int) 10E8;

  public static void main(String [] args) throws FileNotFoundException
  {
    // Fix later
    readMatrix("input.txt");
    //multiThread[] thr = new multiThread[];
    printMatrix();

    int [][] j = new int [7][7];
    System.out.println(j[0][0]);

  }

  public static void test(int [][] g)
  {
    g[0][0] = 1;
  }

  // returns 1d int array of length _numNodes
  public static int[] dijkstras(int source)
  {
    int [] time = new int[_numNodes];
    boolean [] visited = new boolean[_numNodes];

    for (int i = 0; i < _numNodes; i++)
    {
      time[i] = INFINITY;
      visited[i] = false;
    }

    time[source] = 0; // Source to source -> no travel

    for (int i = 0; i < _numNodes; i++)
    {
      int min = INFINITY;
      int nextNode = -1;

      for (int j = 0; j < _numNodes; j++)
      { 
        if (visited[j] == false && time[j] <= min)
        {
          min = time[j];
          nextNode = j;
        }
      }

      visited[nextNode] = true;

      for (int j = 0; j < _numNodes; j++)
      {
        if (!visited[j] && _matrix[i][j] != 0 && time[i] != INFINITY
            && time[i] + _matrix[i][j] < time[j])
            time[j] = time[i] + _matrix[i][j];
      }
    }

    return time;
  }
  
  public static void readMatrix(String filename) throws FileNotFoundException
  {
    Scanner scan = new Scanner(new File(filename));

    _numNodes = scan.nextInt();

    _turnTime = scan.nextInt(); // time taken to swap between loading and running

    _matrix = new int[_numNodes][_numNodes]; // matrix that holds the distance between nodes
    
    
    for (int i = 0; i < _numNodes; i++)
    {
      for (int j = 0; j < _numNodes; j++)
      {
        _matrix[i][j] = scan.nextInt();
        
        if(_matrix[i][j] == -1)
          _matrix[i][j] = INFINITY;
      }
    }
  }

  public static void printMatrix()
  {
    for (int i = 0; i < _numNodes; i++)
    {
      for (int j = 0; j < _numNodes; j++)
      {
        System.out.print(_matrix[i][j] + " ");
      }
      System.out.println();
    }
  }
}




class multiThread extends Thread
{
  int source; // the source vertex
  int result[];
  int[][] _matrix;
  int _numNodes;
  AtomicInteger _counter;
  private final int INFINITY = (int) 10E8;

  public multiThread(int matrix[][], int numNodes, AtomicInteger counter)
  {
    this.source = counter.get();
    this._matrix = matrix;
    this._numNodes = numNodes;
    _counter = counter;
  }


  // returns 1d int array of length _numNodes
  public int[] dijkstras(int source)
  {
    int [] time = new int[_numNodes];
    boolean [] visited = new boolean[_numNodes];

    for (int i = 0; i < _numNodes; i++)
    {
      time[i] = INFINITY;
      visited[i] = false;
    }

    time[source] = 0; // Source to source -> no travel

    for (int i = 0; i < _numNodes; i++)
    {
      int min = INFINITY;
      int nextNode = -1;

      for (int j = 0; j < _numNodes; j++)
      { 
        if (visited[j] == false && time[j] <= min)
        {
          min = time[j];
          nextNode = j;
        }
      }

      visited[nextNode] = true;

      for (int j = 0; j < _numNodes; j++)
      {
        if (!visited[j] && _matrix[i][j] != 0 && time[i] != INFINITY
            && time[i] + _matrix[i][j] < time[j])
            time[j] = time[i] + _matrix[i][j];
      }
    }
    return time;
  }


  @Override
  public void run()   // what the thread will do
  {
    // will run dij algorithm and find the shortest path for every vertex from a given source

    // we need to call dij from this class so we need to move the method from project to here

    // perhaps we could make a 2d matrix in project1 to store result. This way, threads can move on w/o having to wait for another thread
    while()
      result = dijkstras(source);
  }
}