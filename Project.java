import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.*;
import java.io.*;
import java.lang.Thread;


public class Project
{
  private static int[][] _matrix;
  private static int _numNodes;
  private static int [] _turnTimeMaster; // holds the original wait time values so that wait time can reset once it reaches 0
  private static int [] _turnTime; // the decremented times in other words the current time 
  private static ReentrantReadWriteLock _lock = new ReentrantReadWriteLock();

  private static final int INFINITY = (int) 10E8;

  public static void main(String [] args) throws FileNotFoundException, InterruptedException
  {
    AtomicInteger counter = new AtomicInteger(0);

    // Change input to be agrs[0]
    readMatrix("input.txt");
    printMatrix();

    printTimes();

    doDijkstras();

    // TimeController timeController = new TimeController(_turnTime, _turnTimeMaster, _lock);
    // Thread t1 = new Thread(timeController);
    // t1.start();
    
    // createThreads(counter);
  }

  public static void createThreads(AtomicInteger counter) throws InterruptedException
  {
    Thread[] threads = new Thread[7];

    for (int i = 0; i < 7; i++)
    {
      threads[i] = new Thread(new multiThread(_matrix, _matrix, _numNodes, _lock, counter));
      threads[i].start();
    }

    for (int i = 0; i < 7; i++)
    {
      threads[i].join();
    }
  }

  // returns 1d int array of length _numNodes
  public static int[] dijkstras(int source)
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
      int min = INFINITY;
      int min_index = -1; // index of min node

      for (int v = 0; v < _numNodes; v++)
      {
        if (!visited[v] && time[v] <= min)
        {
          min = time[v];
          min_index = v;
        }
      }

      // Process that min node
      visited[min_index] = true;

      // Update values of adjacent nodes of i
      for (int v = 0; v < _numNodes; v++)
      {
        if (!visited[v] // not yet visited
            && _matrix[min_index][v] != INFINITY // edge from node to node
            && time[min_index] != INFINITY // evaluate path to be shorter & ↓
            && time[min_index] + _matrix[min_index][v] + futureWaitTime(time[min_index], min_index) < time[v])
        {
          time[v] = time[min_index] + _matrix[min_index][v] + futureWaitTime(time[min_index], min_index); 
        }
      }
    }
    
    return time;
  }

  // Returns current nodes wait time [futureTime] in the future
  public static int futureWaitTime(int futureTime, int node)
  {
    int presentWaitTime = _turnTime[node];
    
    // just wait the x amount of minutes then return the rest of the remaining time
    if(futureTime < presentWaitTime)
      return (presentWaitTime - futureTime);
    
    // wait the x amount of minutes then reference the time schedule
    return ((futureTime - presentWaitTime) % _turnTimeMaster[node]);
  }
  
  public static void readMatrix(String filename) throws FileNotFoundException
  {
    Scanner scan = new Scanner(new File(filename));

    _numNodes = scan.nextInt();
    
    _matrix = new int[_numNodes][_numNodes]; // matrix that holds the distance between nodes
    _turnTime = new int[_numNodes];
    _turnTimeMaster = new int[_numNodes];
    
    
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
      _turnTimeMaster[i] = scan.nextInt();
      _turnTime[i] = _turnTimeMaster[i];
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
  
  private static void doDijkstras()
  {
    System.out.println("Dijsktras algo for all sources");

    for(int i = 0; i < _numNodes; i++)
    {
      int[] d = dijkstras(i);

      for(int j = 0; j < _numNodes; j++)
      {
        System.out.printf("%4d", d[j]);
      }
      System.out.println();
    }
    System.out.println();
  }

  private static void printTimes()
  {
    System.out.println("Master wait times:");
    for(int i = 0; i < _numNodes; i++)
    {
      System.out.printf("%4d",_turnTimeMaster[i]);
    }
    System.out.println("\n");
  }

  public static int[] getTime(ReadLock lock)
  {
    try
    {
      // Wait for unlock
      lock.lock();

      return _turnTime;
    }
    finally{
      // Unlock
      lock.unlock();
    }

    // _lockTime = TimeController.getTime()
  }
}

// Handles the changing wait times inbetween nodes
class TimeController implements Runnable
{
  int[] _turnTime;
  int[] _turnTimeMaster;
  WriteLock _writeLock;
  AtomicInteger _currentTime;


  public TimeController(int [] turnTime, int [] turnTimeMaster, ReentrantReadWriteLock lock, AtomicInteger currentTime)
  {
    _turnTime = turnTime;
    _turnTimeMaster = turnTimeMaster;
    _writeLock = lock.writeLock();
    _currentTime = currentTime;
  }

  @Override
  public void run()   
  {
    while(true)
    {
      try
      {
        // Lock
        _writeLock.lock();
        
        // Update array
        for (int i = 0; i < _turnTime.length; i++)
        {
          _turnTime[i]--; // adjusts the turn time of every node
          _currentTime.incrementAndGet(); // increments current time
          if (_turnTime[i] == -1)
          {
            _turnTime[i] = _turnTimeMaster[i]; // resets the timers back to their original wait times
          }
          System.out.print("Node "+(i+1)+": "+_turnTime[i]+"s  "); // prints the wait times for each node after time has passed
        }
        System.out.print("\n");
      }
      finally
      {
        // Unlock array
        _writeLock.unlock();
      }
       // Wait
       synchronized(this)
       {
          try 
          {
            this.wait(1000);
          } catch (InterruptedException e) 
          {
            e.printStackTrace();
          }
       }
    }
  }
}


class multiThread extends Thread
{
  int source; // the source vertex
  int result[];
  int[][] _matrix;
  int[][] _times;
  int _numNodes;
  AtomicInteger _counter;
  ReadLock _lock;
  private final int INFINITY = (int) 10E8;

  public multiThread(int matrix[][], int times[][], int numNodes, ReentrantReadWriteLock lock, AtomicInteger counter)
  {
    this.source = counter.get(); // source to work on is whatever the counter was
    counter.getAndIncrement();  // counter increases so threads can work on other sources
    this._matrix = matrix;
    this._times = times;
    this._numNodes = numNodes;
    _lock = lock.readLock();
    _counter = counter;
  }


  
  // returns 1d int array of length _numNodes
  public int[] dijkstras(int source)
  {

    int [] turnTime = new int[_numNodes];

    // Time to get to each node from source
    int [] time = new int[_numNodes];
    
    boolean [] visited = new boolean[_numNodes];

    for (int i = 0; i < _numNodes; i++)
    {
      time[i] = INFINITY;
      visited[i] = false;
    }

    time[source] = turnTime[source]; // Source to source -> just wait time

    for (int i = 0; i < _numNodes; i++)
    {
      int min = INFINITY;
      int nextNode = -1;

      for (int j = 0; j < _numNodes; j++)
      { 
        if (visited[j] == false && (time[j] + turnTime[j]) <= min)
        {
          min = (time[j] + turnTime[j]);
          nextNode = j;
        }
      }

      visited[nextNode] = true;

      for (int j = 0; j < _numNodes; j++)
      {
        if (!visited[j] && _matrix[i][j] != 0 && time[i] != INFINITY
            && (time[i] + turnTime[i] + _matrix[i][j]) < time[j])
            time[j] = (time[i] + turnTime[i] + _matrix[i][j]);
      }
    }

    return time;
  }
  


  @Override
  public void run()   // what the thread will do
  {
    // will run dij algorithm and produce a map of the shortest distances 
    result = dijkstras(source); // still need to store result
    // need to tell thread to get next atomic int to work on
  }
}
