# The Flight Plan
COP4520 group project - The Flight Plan
<h4>Table of contents</h4>
<ul>
  <li>
    <a href="#problem"><b>Problem</b></a>
    <ul>
      <li><a href="#solution">Compilation</a></li>
      <li><a href="#input">Input</a></li>
      <li><a href="#output">Output</a></li>
    </ul>
  </li>
  <li><a href="#proposal">Project Proposal</a></li>
  
  <li><a href="./report.pdf">Project Report</a></li>
</ul>

## Summary
To sum up this project in a few words, we parallelized Dijkstra's Algorithm against a large input in Java. We accomplished this by simply running the algorithm over the dataset across multiple threads, each getting assigned its own array. This is done across all rows every tick or "minute" in the problem's case and the travel times to get from one airport to any other are displayed in rapid succession along with the resulting average execution time in milliseconds per tick.



## Problem
You are given flights route map of a country consisting of N cities and M undirected flight routes. Each city has an airport and each airport can work as layover. The airport will be in two states, Loading and Running. In loading state, luggage is loaded into the planes. In the running state, planes will leave the airport for the next city. All the airports will switch their states from Loading to Running and vice versa after every T minutes. You can cross a city if its airport state is running. Initially, all the airports are in running state. At an airport, if its state is loading, you have to wait for it to switch its state to running. The time taken to travel through any flight route is C minutes. Find the lexicographically smallest path which will take the minimum amount of time (in minutes) required to move from city X to city Y.

It is guaranteed that the given flight route map will be connected. Graph won't contain multiple edges and self loops. A self loop is an edge that connects a vertex to itself.

### Solution

To compile and run:
  ```sh
  javac Project.java
  ```
To run:
  ```sh
  java Project
  ```

### Input
**input.txt** - The first line contains one integer, n. The next n lines contain the weighted directional matrix of all of the possible paths in the graph. If a path has a weight of ”-1” then there is no path from the first node to the second. Finally, the last line contains n space-separated integers corresponding to the respective wait times of each airport.

### Output
**Console** - A map of the total time to get from each node to every other node, updated in real-time.


## Proposal
The problem that our project is seeking to address is a method to find the shortest flight plan from one destination to another given that planes can be in one of two different states, running, and loading. While running, a passenger can take a plane to a set destination, however if the plane is loading, the passenger is expected to wait until a certain amount of time has passed. With no self loops and a guaranteed connected path between cities we expect to use multithreading such that we receive the most optimal flight plan to any given destination given any N amount of nodes in the graph and M amount of paths. As with the inclusion of multithreading, we can address the organization of threads' shared memory and resources that they belong to. In addition, our program should be expected to produce results in an optimal runtime through multithreading, considering that there are several threads of activity focused on our problem within the same address space. The use of Multithreading, would allow us to check multiple paths at a time, drastically improving our operations. A possible technique that could be implemented to address this problem is parallelizing Dijkstra’s Algorithm. This would allow fast and efficient traversal through all possible nodes and allow the algorithm to return the fastest possible path that could be taken through all the terminals Along with the time that it took to traverse through the terminals (nodes) and runtime. This method could also be expanded upon given an N sized graph. To further validate the efficiency of our program and its integrated techniques, we will be letting our program handle multiple uniquely made test cases. To not only prove that our program can successfully and efficiently address the problem, but also cover all possible unknown cases.
