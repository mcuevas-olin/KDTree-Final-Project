# kdTree Final Project
For the final project I created a Kotlin implementation of a kdTree class. It uses a k-d tree data structure to provide efficient spatial partitioning and nearest neighbor search capabilities for multidimensional data.

## Functionality
1. Construction
buildkdTree(points: List<Point>, depth: Int): TreeNode<Point>?

When you first create an instance of the class, it requires you to input a multidimenisonal array created from mutable lists filled with integers. Then the initialization calls buildkdTree to construct the kdTree from the given points.
It recursively partitions the points along different dimensions (axes) to create a balanced tree structure by using a modified merge sorting algorithm to sort the points on the given dimension, removes the median point, and recursively call to sort the other points.... add with more clarity and detail how the construction of the kd tree works here..
3. Nearest Neighbor Search
nearestNeighbor(target: Point): TreeNode<Point>?
Finds the nearest neighbor to a given target point of the same dimensionality in the k-d tree.
Write down in more detail about the process and algorithm used
Utilizes a priority queue to efficiently search for the nearest neighbor.
4. k Nearest Neighbors Search
kNearestNeighbors(target: Point, k: Int): List<TreeNode<Point>>
Finds the k nearest neighbors to a given target point in the k-d tree.
write down in more detail about the process and algorithm used
Uses a priority queue to maintain the k nearest neighbors found so far.
5. Points Within Distance Search
pointsWithinDistance(queryPoint: Point, distance: Double): List<Point>
Finds all points within a given distance of a query point in the k-d tree.
write down in more detail about the process and algorithm used
Utilizes a priority queue to efficiently search for points within the specified distance.
6. Sorting
sortBy(points: MutableList<Point>, axis: Int): MutableList<Point>
Implements the Merge Sort algorithm to sort a list of points based on a specific axis.
Used internally for sorting points during k-d tree construction. --- the main reason it is not private is for testing... but this should probably go under construction
7. Tree Visualization
printkdTree()
Prints the structure of the k-d tree, including each node's value and depth.
Utilizes a helper function for recursive traversal of the tree.
Usage
The kdTree class can be used for various spatial data analysis tasks, including nearest neighbor search, clustering, and efficient range queries in multidimensional space. Some examples include for GPS, such as finding all the coffee shops within a given radius if one were to use the Points within Distance Search, or to find the 5 closest coffee shops using the k Nearest Neighbors search, or simply finding the closest coffee shop using the nearest neighbor search. The nature of kd trees means that more computational power is used in the creation of the tree to make future queries faster. This assumes that a greater importance is placed on the efficiency of the future queries over the time it takes to set the trees up. To preserve the balanced stucture, and no nodes may be added or removed. Instead, one has to create a new tree. It is possible to create an algorithm to add, remove, or change the value of the nodes while keeping the tree balanced. However, it is much simpler on the programming end to simply create a new tree. 

