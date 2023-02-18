# The Flight Plan
COP4520 group project - The Flight Plan
<h4>Table of contents</h4>
<ul>
  <li><a href="#problem">Problem</a></li>
  <li><a href="#proposal">Project Proposal</a></li>
  <li><a href="#constraints">Constraints</a></li>
  <li><a href="#approach">Summary of Approach</a></li>
</ul>

### Problem

### Proposal
The problem that our project is seeking to address is a method to find the shortest flight plan from one destination to another given that planes can be in one of two different states, running, and loading. While running, a passenger can take a plane to a set destination, however if the plane is loading, the passenger is expected to wait until a certain amount of time has passed. With no self loops and a guaranteed connected path between cities we expect to use multithreading such that we receive the most optimal flight plan to any given destination given any N amount of nodes in the graph and M amount of paths. As with the inclusion of multithreading, we can address the organization of threads' shared memory and resources that they belong to. In addition, our program should be expected to produce results in an optimal runtime through multithreading, considering that there are several threads of activity focused on our problem within the same address space. The use of Multithreading, would allow us to check multiple paths at a time, drastically improving our operations. A possible technique that could be implemented to address this problem is parallelizing Dijkstra’s Algorithm. This would allow fast and efficient traversal through all possible nodes and allow the algorithm to return the fastest possible path that could be taken through all the terminals Along with the time that it took to traverse through the terminals (nodes) and runtime. This method could also be expanded upon given an N sized graph. To further validate the efficiency of our program and its integrated techniques, we will be letting our program handle multiple uniquely made test cases. To not only prove that our program can successfully and efficiently address the problem, but also cover all possible unknown cases.

### Constraints
Constraints list

### Approach
Dijkstra's algorithm (?)
