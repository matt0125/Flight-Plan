# The Flight Plan
COP4520 group project - The Flight Plan
<h4>Table of contents</h4>
<ul>
  <li>
    <a href="#problem"><b>Problem</b></a>
    <ul>
      <li><a href="#input">Input</a></li>
      <li><a href="#output">Output</a></li>
      <li><a href="#constraints">Constraints</a></li>
    </ul>
  </li>
  <li><a href="#proposal">Project Proposal</a></li>
  <li><a href="#approach">Summary of Approach</a></li>
</ul>



## Problem
You are given flights route map of a country consisting of N cities and M undirected flight routes. Each city has an airport and each airport can work as layover. The airport will be in two states, Loading and Running. In loading state, luggage is loaded into the planes. In the running state, planes will leave the airport for the next city. All the airports will switch their states from Loading to Running and vice versa after every T minutes. You can cross a city if its airport state is running. Initially, all the airports are in running state. At an airport, if its state is loading, you have to wait for it to switch its state to running. The time taken to travel through any flight route is C minutes. Find the lexicographically smallest path which will take the minimum amount of time (in minutes) required to move from city X to city Y.

It is guaranteed that the given flight route map will be connected. Graph won't contain multiple edges and self loops. A self loop is an edge that connects a vertex to itself.

### Input
The first line contains 4 space separated integers, N, M, T and C. Next M lines contains two space separated integers each, U and V denoting that there is a bidirectional road between city U and city V. Next line contains two space separated integers, X and Y.

### Output
In the first line print an integer K, denoting the number of city you need to go through to reach city Y from the city X. In next line, print K space separated integers denoting the path which will take the minimum amount of time (in minutes) required by to move from city X to city Y. There can be multiple paths. Print the lexicographically smallest one.

<!-- Probably going to need to up the 10^3 for multi -->
### Constraints
1 ≤ N, C, T ≤ $10^3$ </br>
N - 1 ≤ M ≤ $\frac {N\times(N-1)} {2}$</br>
1 ≤ U, V, X, Y ≤ N



## Proposal
The problem that our project is seeking to address is a method to find the shortest flight plan from one destination to another given that planes can be in one of two different states, running, and loading. While running, a passenger can take a plane to a set destination, however if the plane is loading, the passenger is expected to wait until a certain amount of time has passed. With no self loops and a guaranteed connected path between cities we expect to use multithreading such that we receive the most optimal flight plan to any given destination given any N amount of nodes in the graph and M amount of paths. As with the inclusion of multithreading, we can address the organization of threads' shared memory and resources that they belong to. In addition, our program should be expected to produce results in an optimal runtime through multithreading, considering that there are several threads of activity focused on our problem within the same address space. The use of Multithreading, would allow us to check multiple paths at a time, drastically improving our operations. A possible technique that could be implemented to address this problem is parallelizing Dijkstra’s Algorithm. This would allow fast and efficient traversal through all possible nodes and allow the algorithm to return the fastest possible path that could be taken through all the terminals Along with the time that it took to traverse through the terminals (nodes) and runtime. This method could also be expanded upon given an N sized graph. To further validate the efficiency of our program and its integrated techniques, we will be letting our program handle multiple uniquely made test cases. To not only prove that our program can successfully and efficiently address the problem, but also cover all possible unknown cases.



## Approach
Dijkstra's algorithm (?)
