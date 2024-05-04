# kdTree Final Project

For the final project, I developed a Kotlin implementation of a kdTree class, leveraging the k-d tree data structure to facilitate efficient spatial partitioning and nearest neighbor search operations for multidimensional data.

## Functionality

### 1. Construction

**buildkdTree(points: List<Point>, depth: Int): TreeNode<Point>?**

Upon instantiation, the kdTree class requires input of a multidimensional array created from mutable lists filled with integers. The initialization invokes buildkdTree to construct the kdTree from the provided points. This process recursively partitions the points along different dimensions (axes) to achieve a balanced tree structure. 

#### Algorithm Steps:
1. **Base Case**: If the list of points is empty, return null.
2. **Determine Dimension**: Calculate the dimension (axis) to split based on the current depth.
3. **Sort Points**: Sort the points based on the current axis.
4. **Select Pivot**: Find the median point as the pivot element.
5. **Create Node**: Create a node for the k-d tree with the pivot point.
6. **Recursively Construct Subtrees**: Construct the left and right subtrees with points before and after the median, respectively.
7. **Return Root**: Return the root of the k-d tree.

### 2. Nearest Neighbor Search

**nearestNeighbor(target: Point): TreeNode<Point>?**

This function finds the nearest neighbor to a given target point of the same dimensionality within the k-d tree. 

#### Algorithm Steps:
1. **Priority Queue Initialization**: Initialize a priority queue to store nodes based on their distance to the target point.
2. **Offer Root Node**: Offer the root node to the priority queue.
3. **Search Iteration**: While the priority queue is not empty, poll a node from the queue.
4. **Update Nearest Node**: Update the nearest node and distance if the current node is closer to the target point.
5. **Determine Child Nodes**: Determine which child node to search first based on the target point's position relative to the current node.
6. **Recursively Search Child Nodes**: Recursively search the nearer child node.
7. **Offer Further Child Node**: If necessary, offer the further child node to the priority queue.

### 3. k Nearest Neighbors Search

**kNearestNeighbors(target: Point, k: Int): List<TreeNode<Point>>**

For a given target point, this function identifies the k nearest neighbors within the k-d tree.

#### Algorithm Steps:
1. **Priority Queue Initialization**: Initialize a priority queue to store nodes based on their distance to the target point.
2. **Perform Nearest Neighbor Search**: Perform nearest neighbor search starting from the root node.
3. **Maintain K Nearest Neighbors**: Maintain a priority queue of the k nearest neighbor nodes found so far.
4. **Retrieve K Nearest Neighbors**: Retrieve the k nearest neighbors from the priority queue.

### 4. Points Within Distance Search

**pointsWithinDistance(queryPoint: Point, distance: Double): List<Point>**

This function locates all points within a specified distance of a query point within the k-d tree.

#### Algorithm Steps:
1. **Priority Queue Initialization**: Initialize a priority queue to store points based on their distance to the query point.
2. **Perform Recursive Search**: Recursively find all points within the specified distance of the query point.
3. **Sort and Retrieve Points**: Sort the points in ascending order of distance and retrieve them from the priority queue.

### 5. Sorting

**sortBy(points: MutableList<Point>, axis: Int): MutableList<Point>**

Implemented as part of the construction process, this function utilizes the Merge Sort algorithm to sort a list of points based on a specific axis.

#### Algorithm Steps:
1. **Base Case**: If the list has 0 or 1 elements, it is already sorted.
2. **Find Middle Index**: Find the middle index of the list.
3. **Recursively Sort Halves**: Recursively sort the left and right halves of the list.
4. **Merge Sorted Halves**: Merge the sorted left and right halves.

### 6. Tree Visualization

**printkdTree()**

Prints the structure of the k-d tree, including each node's value and depth.

## Usage

The kdTree class is versatile and can be applied to various spatial data analysis tasks, including nearest neighbor search, clustering, and efficient range queries in multidimensional space. For example, it can be used in GPS applications to find nearby points of interest such as coffee shops. Examples of usage scenarios include finding all coffee shops within a given radius using Points Within Distance Search, identifying the 5 closest coffee shops using k Nearest Neighbors search, or simply determining the closest coffee shop using nearest neighbor search.

It's important to note that kd trees require more computational power during creation to make future queries faster. The balanced structure of the tree is preserved, and no nodes can be added or removed once created. To modify the tree, a new one must be created. While it's possible to develop algorithms to add, remove, or change node values while keeping the tree balanced, creating a new tree is simpler from a programming perspective.
