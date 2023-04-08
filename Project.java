import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.*;
import java.io.*;
import java.lang.Thread;


public class Project
{
  // Dijkstras variables
  private static int[][] _matrix;
  private static int[][] _travelTime;
  private static int _numNodes;
  private static int [] _turnTime;
  
  // Thread variables
  private static ReentrantReadWriteLock _lock = new ReentrantReadWriteLock();
  public static volatile boolean _exit = false;
  private static volatile int _currentTime;
  public static AtomicInteger _counter;

  // Constants
  private static final int INFINITY = (int) 10E8;

  // App settings
  public static final int THREAD_COUNT = 8;
  public static final boolean PRINT = true;

  public static void main(String [] args) throws FileNotFoundException, InterruptedException
  {
    // Change input to be agrs[0]
    readInput("input.txt");
    _counter = new AtomicInteger(0);

    if(PRINT)
    {
      printMatrix();
      printWaitTimes();
    }

    doDijkstras();

    TimeController timeController = new TimeController(_turnTime, _lock, PRINT);
    Thread t1 = new Thread(timeController);
    t1.start();
    
    Thread[] threads = createThreads();
    startThreads(threads);

    joinThreads(threads);
    

  }

  public static Thread[] createThreads() throws InterruptedException
  {

    Thread[] threads = new Thread[THREAD_COUNT-1];

    for (int i = 0; i < (THREAD_COUNT-1); i++)
    {
      threads[i] = new Thread(new multiThread(_matrix, _matrix, _turnTime, _numNodes, _lock, _counter));
    }

    return threads;
  }

  private static void startThreads(Thread[] threads)
  {
    for (int i = 0; i < threads.length; i++)
    {
      threads[i].start();
    }
  }

  private static void joinThreads(Thread[] threads)
  {
    for(int i = 0; i < threads.length; i++)
    {
      try {
        threads[i].join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static int getCurrentTime() { return _currentTime; }

  public static void updateCurrentTime() { Project._currentTime++; }

  // returns 1d int array of length _numNodes
  public static int[] singleSourceDijkstras(int source)
  {
    // Time to get to each node from source
    int [] time = new int[_numNodes];
    
    boolean [] visited = new boolean[_numNodes];

    Arrays.fill(time, INFINITY);
    Arrays.fill(visited, false);

    time[source] = 0; // Source to source -> no wait

    for (int count = 0; count < _numNodes; count++)
    {

      // Finds the next min time path from current node
      int minTime = INFINITY;
      int min = -1; // index of min node

      for (int n = 0; n < _numNodes; n++)
      {
        if (!visited[n] && time[n] <= minTime)
        {
          minTime = time[n];
          min = n;
        }
      }

      // Process that min node
      visited[min] = true;

      // Update values of adjacent nodes of i
      for (int n = 0; n < _numNodes; n++)
      {
        if (!visited[n] // not yet visited
            && _matrix[min][n] != INFINITY // edge from node to node
            && time[min] != INFINITY // evaluate path to be shorter & ↓
            && time[min] + _matrix[min][n] + futureWaitTime(time[min], min) < time[n])
        {
          time[n] = time[min] + _matrix[min][n] + futureWaitTime(time[min], min); 
        }
      }
    }
    
    return time;
  }

  // Uses application wide current time to figure future wait time
  public static int futureWaitTime(int futureTime, int node)
  {
    return (_turnTime[node] - ((_currentTime + futureTime) % _turnTime[node]));
  }
  
  public static void readInput(String filename) throws FileNotFoundException
  {
    Scanner scan = new Scanner(new File(filename));

    _numNodes = scan.nextInt();
    
    _matrix = new int[_numNodes][_numNodes]; // matrix that holds the distance between nodes
    _travelTime = new int[_numNodes][_numNodes];
    _turnTime = new int[_numNodes];
    
    
    for (int i = 0; i < _numNodes; i++)
    {
      for (int j = 0; j < _numNodes; j++)
      {
        _matrix[i][j] = scan.nextInt();
        
        if(_matrix[i][j] == -1)
          _matrix[i][j] = INFINITY;
      }
    }

    for (int i = 0; i < _numNodes; i++)
    {
      _turnTime[i] = scan.nextInt();
    }
  }

  public static void printMatrix()
  {
    System.out.println("Matrix:");
    for (int i = 0; i < _numNodes; i++)
    {
      for (int j = 0; j < _numNodes; j++)
      {
        System.out.printf("%4d", _matrix[i][j]);
      }
      System.out.println();
    }
    System.out.println();
  }

  public static void printTravelTimes()
  {
    System.out.println("Travel times:");
    for (int i = 0; i < _numNodes; i++)
    {
      for (int j = 0; j < _numNodes; j++)
      {
        System.out.printf("%4d", _travelTime[i][j]);
      }
      System.out.println();
    }
    System.out.println();
  }
  
  public static void printWaitTimes()
  {
    System.out.println("Wait times:");
    for(int i = 0; i < _numNodes; i++)
    {
      System.out.printf("%4d",_turnTime[i]);
    }
    System.out.println("\n"); 
  }

  public static void doDijkstras()
  {
    System.out.println("Dijsktras algo for all sources");

    if(PRINT)
    {
      for(int i = 0; i < _numNodes; i++)
      {
        int[] d = singleSourceDijkstras(i);

        for(int j = 0; j < _numNodes; j++)
        {
          System.out.printf("%4d", d[j]);
        }
        System.out.println();
      }
      System.out.println();
    }

    else
    {
      for(int i = 0; i < _numNodes; i++)
      {
        singleSourceDijkstras(i);
      }
    }
  }
}

// Handles the changing wait times inbetween nodes
class TimeController implements Runnable
{
  int[] _turnTimeMaster; // Holds original turn times (shared across all threads)
  int[] _turnTime; // Local copy of original turn times to update as time goes on 
  WriteLock _writeLock;
  
  final boolean PRINT;


  public TimeController(int [] turnTime, ReentrantReadWriteLock lock, boolean print)
  {
    _writeLock = lock.writeLock();
    _turnTimeMaster = turnTime;

    this.PRINT = print;

    // Needs a new copy of turntime to not alter original when printing current wait times
    _turnTime = new int[turnTime.length];
    for (int i = 0; i < turnTime.length; i++)
    {
      _turnTime[i] = turnTime[i];
    }
  }

  @Override
  public void run()   
  {
    // Come up with better names
    if (PRINT)
      print();
    else
      notPrint();
  }

  private void notPrint()
  {
    while(true)
    {
      break;
    }
  }

  private void print()
  {
    for(int i = 0; i < 10; i++)
    {
      System.out.println("Current time: " + Project.getCurrentTime());

      Project.updateCurrentTime();
      updateTimes();

      Project.printTravelTimes();

      printCurrentTimes();

      System.out.println();
    }
  }

  public void updateTimes()
  {
    // Update array
    for (int i = 0; i < _turnTime.length; i++)
    {
      _turnTime[i]--; // adjusts the turn time of every node

      if (_turnTime[i] == -1)
      {
        // resets the timers back to their original wait times
        _turnTime[i] = _turnTimeMaster[i];
      }
    }
  }

  public void printCurrentTimes()
  {
    System.out.println("Current wait times per station:");

    for (int i = 0; i < _turnTime.length; i++)
    {
      // prints the wait times for each node after time has passed
      System.out.printf("%4d", _turnTime[i]);
    }
    System.out.print("\n");
  }
}


class multiThread extends Thread
{
  int[][] _matrix; // Adjancency matrix
  int[][] _times; // 
  int[] _turnTime;
  int _numNodes;

  AtomicInteger _counter;
  ReadLock _lock;
  private final int INFINITY = (int) 10E8;

  public multiThread(int matrix[][], int times[][], int[] turnTime, int numNodes, ReentrantReadWriteLock lock, AtomicInteger counter)
  {
    this._matrix = matrix;
    this._times = times;
    this._turnTime = turnTime;
    this._numNodes = numNodes;
    this._lock = lock.readLock();
    this._counter = counter;
  }


  
  // returns 1d int array of length _numNodes
  public int[] dijkstras(int source)
  {
    // Time to get to each node from source
    int [] time = new int[_numNodes];
    
    boolean [] visited = new boolean[_numNodes];

    Arrays.fill(time, INFINITY);
    Arrays.fill(visited, false);

    time[source] = 0; // Source to source -> no wait

    for (int i = 0; i < _numNodes; i++)
    {

      // Finds the next min time path from current node
      int minTime = INFINITY;
      int min = -1; // index of min node

      for (int n = 0; n < _numNodes; n++)
      {
        if (!visited[n] && time[n] <= minTime)
        {
          minTime = time[n];
          min = n;
        }
      }

      // Process that min node
      visited[min] = true;
      int waitTime;

      // Update values of adjacent nodes of i
      for (int n = 0; n < _numNodes; n++)
      {
        if (!visited[n] // not yet visited
            && _matrix[min][n] != INFINITY // edge from node to node
            && time[min] != INFINITY // evaluate path to be shorter & ↓
            && time[min] + _matrix[min][n] + (waitTime = (_turnTime[min] - ((Project.getCurrentTime() + time[min]) % _turnTime[min]))) < time[n]) // replace 0 with currentTime
        {
          time[n] = time[min] + _matrix[min][n] + waitTime; 
        }
      }
    }
    
    return time;
  }

  public int futureWaitTime(int futureTime, int node)
  {
    return (_turnTime[node] - ((Project.getCurrentTime() + futureTime) % _turnTime[node]));
  }
  


  @Override
  public void run()   // what the thread will do
  {
    int source;
    
    while(!Project._exit)
    {
      // Take source number from pool while its less than the num nodes
      while((source = _counter.getAndIncrement()) < _numNodes)
      {
        // run dijkstras on that source and store it in times
        _times[source] = dijkstras(source);
      }
      
      // wait until the time increments

    break;

    }
    
  }
}
