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

```kotlin
// Upon instantiation, the kdTree class requires input of a multidimensional array created from mutable lists filled with integers.
class kdTree(points: MutableList<Point>) {
    var rootNode: TreeNode<Point>? = null

    init {
        // The initialization invokes buildkdTree to construct the kdTree from the provided points.
        rootNode = buildkdTree(points)
    }

    /**
     * Constructs a k-d tree from a list of points.
     *
     * @param points The list of points to construct the k-d tree from.
     * @param depth The current depth in the tree (initially 0).
     * @return The root node of the constructed k-d tree.
     */
    private fun buildkdTree(points: MutableList<Point>, depth: Int = 0): TreeNode<Point>? {
        // Base case: If the list of points is empty, return null
        if (points.isEmpty()) {
            return null
        }

        // Determine the dimension (axis) to split based on depth
        val axis = depth % points[0].size

        // Sort the points based on the current axis
        val sortedPoints = sortBy(points.toMutableList(), axis)

        // Find the index of the median point
        val medianIndex = sortedPoints.size / 2

        // Select the median point as the pivot element
        val medianPoint = sortedPoints[medianIndex]

        // Create a node for the k-d tree with the pivot point
        val root = TreeNode(medianPoint)

        // Recursively construct the left subtree with points before the median
        root.left = buildkdTree(sortedPoints.subList(0, medianIndex), depth + 1)

        // Recursively construct the right subtree with points after the median
        root.right = buildkdTree(sortedPoints.subList(medianIndex + 1, sortedPoints.size), depth + 1)

        // Return the root of the k-d tree
        return root
    }
}


### 2. Nearest Neighbor Search

**nearestNeighbor(target: Point): TreeNode<Point>?**

This function returns the node of the nearest neighbor to a given target point of the same dimensionality within the k-d tree.

#### Algorithm Steps:

1. **Priority Queue Initialization**: Initialize a priority queue to store nodes based on their distance to the target point.
2. **Offer Root Node**: Offer the root node to the priority queue.
3. **Search Iteration**: While the priority queue is not empty, poll a node from the queue.
4. **Update Nearest Node**: Update the nearest node and distance if the current node is closer to the target point.
5. **Determine Child Nodes**: Determine which child node to search first based on the target point's position relative to the current node.
6. **Recursively Search Child Nodes**: Recursively search the nearer child node.
7. **Offer Further Child Node**: If the distance between the splitting plane of the current node and the target point is less than the current nearest distance, offer the further child node to the priority queue.

```kotlin
/**
 * Finds the nearest neighbor to a given target point in the k-d tree.
 *
 * @param target The target point for which to find the nearest neighbor.
 * @return The nearest neighbor to the target point.
 */
fun nearestNeighbor(target: Point): TreeNode<Point>? {
    val nearestNeighborQueue = PriorityQueue<TreeNode<Point>>(Comparator { n1, n2 ->
        // Compare nodes based on their distance to the target point
        euclideanDistance(n1.value, target).compareTo(euclideanDistance(n2.value, target))
    })

    // Initialize with the root node
    nearestNeighborQueue.offer(rootNode)

    // Initialize the nearest node and distance
    var nearestNode: TreeNode<Point>? = null
    var nearestDistance = Double.MAX_VALUE

    while (nearestNeighborQueue.isNotEmpty()) {
        val node = nearestNeighborQueue.poll()
        val currentDistance = euclideanDistance(node.value, target)

        // Update nearest node and distance if current node is closer
        if (currentDistance < nearestDistance) {
            nearestNode = node
            nearestDistance = currentDistance
        }

        // Determine which child node to search first based on the target point's position relative to the current node
        val axis = node.value.size
        val nearerChild = if (target[axis % node.value.size] < node.value[axis % node.value.size]) node.left else node.right
        val furtherChild = if (nearerChild == node.left) node.right else node.left

        // Recursively search the nearer child
        nearerChild?.let { nearestNeighborQueue.offer(it) }

        // Check if it's necessary to search the further child
        if (furtherChild != null && abs(target[axis % node.value.size] - node.value[axis % node.value.size]) < nearestDistance) {
            nearestNeighborQueue.offer(furtherChild)
        }
    }
```
  


### 3. k Nearest Neighbors Search

**kNearestNeighbors(target: Point, k: Int): List<TreeNode<Point>>**

For a given target point, this function identifies the k nearest neighbors within the k-d tree.

#### Algorithm Steps:
1. **Priority Queue Initialization**: Initializes a priority queue `nearestNeighbors` to store nodes based on their distance to the target point.
2. **Perform Nearest Neighbor Search**: Invokes the `kNearestNeighborsSearch` function to perform the search starting from the root node. This function differs from the standard nearest neighbor search in that it maintains a priority queue of the k nearest neighbor nodes found so far.
3. **Maintain K Nearest Neighbors**: Maintains a priority queue `nearestNeighbors` containing the current k nearest neighbor nodes found so far. It updates this queue as new nearest neighbors are discovered during the search.
4. **Retrieve K Nearest Neighbors**: Retrieves the k nearest neighbors from the priority queue `nearestNeighbors`. These neighbors are then returned as a list.

```kotlin
/**
 * Finds the k nearest neighbors to a given target point in the k-d tree.
 *
 * @param target The target point for which to find the k nearest neighbors.
 * @param k The number of nearest neighbors to find.
 * @return A list containing the k nearest neighbors to the target point.
 */
fun kNearestNeighbors(target: Point, k: Int): List<TreeNode<Point>> {
    // Initialize a priority queue to store the nearest neighbors
    val nearestNeighbors = PriorityQueue<TreeNode<Point>> { n1, n2 ->
        // Compare nodes based on their distance to the target point
        euclideanDistance(n1.value, target).compareTo(euclideanDistance(n2.value, target))
    }

    // Perform nearest neighbor search starting from the root node
    kNearestNeighborsSearch(rootNode, target, k, nearestNeighbors)

    // Initialize a list to store the k nearest neighbors
    val result = mutableListOf<TreeNode<Point>>()

    // Add the k nearest neighbors from the priority queue to the list
    repeat(k) {
        nearestNeighbors.poll()?.let { result.add(it) }
    }

    // Return the list of k nearest neighbors
    return result
}

/**
 * Recursively finds the k nearest neighbors to a given target point in the k-d tree.
 *
 * @param node The current node being considered.
 * @param target The target point for which to find the k nearest neighbors.
 * @param k The number of nearest neighbors to find.
 * @param currentBestNodes A priority queue containing the current k nearest neighbor nodes found so far.
 * @return A priority queue of the k nearest neighbor nodes to the target point.
 */
private fun kNearestNeighborsSearch(node: TreeNode<Point>?, target: Point, k: Int, currentBestNodes: PriorityQueue<TreeNode<Point>>): PriorityQueue<TreeNode<Point>> {
    if (node == null) {
        return currentBestNodes
    }

    // Calculate the distance between the target point and the current node
    val currentDistance = euclideanDistance(node.value, target)

    // Update the priority queue of k nearest nodes if necessary
    if (currentBestNodes.size < k || currentDistance < euclideanDistance(currentBestNodes.peek().value, target)) {
        currentBestNodes.offer(node)

        // If the queue exceeds k elements, remove the farthest node
        if (currentBestNodes.size > k) {
            currentBestNodes.poll()
        }
    }

    val axis = node.value.size // Assuming the node's value represents the dimensionality of the point

    // Recursively search the nearer child
    val nearerChild = if (target[axis % node.value.size] < node.value[axis % node.value.size]) node.left else node.right
    kNearestNeighborsSearch(nearerChild, target, k, currentBestNodes)

    // Check if it's necessary to search the further child
    if (abs(target[axis % node.value.size] - node.value[axis % node.value.size]) < euclideanDistance(currentBestNodes.peek().value, target)) {
        val furtherChild = if (nearerChild == node.left) node.right else node.left
        kNearestNeighborsSearch(furtherChild, target, k, currentBestNodes)
    }

    return currentBestNodes
}
```

### 4. Points Within Distance Search

**pointsWithinDistance(queryPoint: Point, distance: Double): List<Point>**

This function locates all points within a specified distance of a query point within the k-d tree.
#### Algorithm Steps:

1. **Priority Queue Initialization**: Initialize a priority queue `nearbyPoints` to store points based on their distance to the query point.
2. **Perform Recursive Search**: Recursively find all points within the specified distance of the query point using the `findPointsWithinDistance` function. The main difference compared to the nearest neighbor searches is that it determines whether or not it will search the subtrees by if it would contain points within the distance range.
3. **Sort and Retrieve Points**: Sort the points in ascending order of distance and retrieve them from the priority queue.

```kotlin
/**
 * Finds all points within a given distance of a query point in the k-d tree.
 *
 * @param queryPoint The query point for which to find nearby points.
 * @param distance The maximum distance within which points should be considered.
 * @return A list containing all points within the specified distance of the query point.
 */
fun pointsWithinDistance(queryPoint: Point, distance: Double): List<Point> {
    val nearbyPoints = PriorityQueue<Pair<Double, Point>>(compareByDescending { it.first })
    findPointsWithinDistance(rootNode, queryPoint, distance, nearbyPoints)
    return nearbyPoints.sortedBy { it.first }.map { it.second }
}

/**
 * Recursively finds all points within a given distance of a query point in the k-d tree.
 *
 * @param node The current node being considered.
 * @param queryPoint The query point for which to find nearby points.
 * @param distance The maximum distance within which points should be considered.
 * @param nearbyPoints A priority queue to store points within the specified distance.
 */
private fun findPointsWithinDistance(node: TreeNode<Point>?, queryPoint: Point, distance: Double, nearbyPoints: PriorityQueue<Pair<Double, Point>>) {
    if (node == null) {
        return
    }

    val currentDistance = euclideanDistance(node.value, queryPoint)

    // If the current node is within the specified distance, add it to the priority queue
    if (currentDistance <= distance) {
        nearbyPoints.offer(currentDistance to node.value)
    }

    val axis = node.value.size // Assuming the node's value represents the dimensionality of the point

    // Recursively search the subtree that contains points within the distance range
    if (queryPoint[axis % node.value.size] - distance <= node.value[axis % node.value.size]) {
        findPointsWithinDistance(node.left, queryPoint, distance, nearbyPoints)
    }
    if (queryPoint[axis % node.value.size] + distance >= node.value[axis % node.value.size]) {
        findPointsWithinDistance(node.right, queryPoint, distance, nearbyPoints)
    }
}


### 5. Sorting

**sortBy(points: MutableList<Point>, axis: Int): MutableList<Point>**

Implemented as part of the construction process, this function utilizes the Merge Sort algorithm to sort a list of points based on a specific axis.

#### Algorithm Steps:

1. **Base Case**: If the list has 0 or 1 elements, it is already sorted.
2. **Find Middle Index**: Find the middle index of the list.
3. **Recursively Sort Halves**: Recursively sort the left and right halves of the list.
4. **Merge Sorted Halves**: Merge the sorted left and right halves into a sorted list.

```kotlin
/**
 * Implementation of Merge Sort algorithm to sort a list of points based on a specific axis.
 *
 * @param points The input MutableList of points to be sorted.
 * @param axis The axis (dimension) along which to sort the points.
 * @return A MutableList containing elements of the input list sorted in ascending order based on the specified axis.
 */
fun sortBy(points: MutableList<Point>, axis: Int): MutableList<Point> {
    // Base case: If the list has 0 or 1 elements, it is already sorted
    if (points.size <= 1) {
        return points
    }

    // Find the middle index of the list
    val middle = points.size / 2

    // Recursively sort the left half of the list
    val leftHalf = sortBy(points.subList(0, middle), axis)

    // Recursively sort the right half of the list
    val rightHalf = sortBy(points.subList(middle, points.size), axis)

    // Merge the sorted left and right halves
    return merge(leftHalf, rightHalf, axis)
}

/**
 * Merge two sorted lists of points into one sorted list based on a specific axis.
 *
 * @param left The first sorted MutableList of points.
 * @param right The second sorted MutableList of points.
 * @param axis The axis (dimension) along which to merge the lists.
 * @return A MutableList containing elements of the input lists merged and sorted in ascending order based on the specified axis.
 */
private fun merge(left: MutableList<Point>, right: MutableList<Point>, axis: Int): MutableList<Point> {
    // Initialize an empty list to store the merged result
    val result = mutableListOf<Point>()

    // Initialize indices for traversing left and right lists
    var leftIndex = 0
    var rightIndex = 0

    // Compare elements from both lists and add the smaller one to the result
    while (leftIndex < left.size && rightIndex < right.size) {
        if (left[leftIndex][axis] < right[rightIndex][axis]) {
            result.add(left[leftIndex])
            leftIndex++
        } else {
            result.add(right[rightIndex])
            rightIndex++
        }
    }

    // Add remaining elements from left list
    result.addAll(left.subList(leftIndex, left.size))

    // Add remaining elements from right list
    result.addAll(right.subList(rightIndex, right.size))

    return result
}
```

### 6. Tree Visualization

**printkdTree()**

Prints the structure of the k-d tree, including each node's value and depth.

## Usage

The kdTree class is versatile and can be applied to various spatial data analysis tasks, including nearest neighbor search, clustering, and efficient range queries in multidimensional space. For example, it can be used in GPS applications to find nearby points of interest such as coffee shops. Examples of usage scenarios include finding all coffee shops within a given radius using Points Within Distance Search, identifying the 5 closest coffee shops using k Nearest Neighbors search, or simply determining the closest coffee shop using nearest neighbor search.

It's important to note that kd trees require more computational power during creation to make future queries faster. The balanced structure of the tree is preserved, and no nodes can be added or removed once created. To modify the tree, a new one must be created. While it's possible to develop algorithms to add, remove, or change node values while keeping the tree balanced, creating a new tree is simpler from a programming perspective.
