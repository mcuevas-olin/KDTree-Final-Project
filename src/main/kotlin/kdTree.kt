package org.example


// Define a typealias for Point representing a multidimensional array
typealias Point = MutableList<Int>


/**
 * Represents a node in a binary tree.
 *
 * @param Point The type of data point stored in the node.
 * @property value The value stored in the node.
 */
class TreeNode<Point>(var value: Point) {
    var parent: TreeNode<Point>? = null  // Reference to the parent node
    var left: TreeNode<Point>? = null    // Reference to the left child node
    var right: TreeNode<Point>? = null   // Reference to the right child node
}


class kdTree(points: MutableList<Point>) {


    init {
        val rootNode : TreeNode<Point>? = buildKdTree(points)
    }

    /**
     * Constructs a k-d tree from a list of points.
     *
     * @param points The list of points to construct the k-d tree from.
     * @param depth The current depth in the tree (initially 0).
     * @return The root node of the constructed k-d tree.
     */
    fun buildKdTree(points: List<Point>, depth: Int = 0): TreeNode<Point>? {
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
        root.left = buildKdTree(sortedPoints.subList(0, medianIndex), depth + 1)

        // Recursively construct the right subtree with points after the median
        root.right = buildKdTree(sortedPoints.subList(medianIndex + 1, sortedPoints.size), depth + 1)

        // Return the root of the k-d tree
        return root
    }


    private fun euclideanDistance(point1: Point, point2: Point): Double {
        require(point1.size == point2.size) { "Points must have the same dimensions" }

        var sumOfSquares = 0.0
        for (i in point1.indices) {
            val diff = point1[i] - point2[i]
            sumOfSquares += diff * diff
        }

        return Math.sqrt(sumOfSquares)
    }

    /**
     * Finds the nearest neighbor to a given target point in the k-d tree.
     *
     * @param target The target point for which to find the nearest neighbor.
     * @return The nearest neighbor to the target point.
     */
    fun nearestNeighbor(target: Point): TreeNode<Point>? {
        return nearestNeighborSearch(rootNode, target, rootNode)
    }

    /**
     * Helper function for performing nearest neighbor search recursively.
     *
     * @param node The current node being considered.
     * @param target The target point for which to find the nearest neighbor.
     * @param currentBestNode The current best node found so far.
     * @return The nearest neighbor to the target point.
     */
    private fun nearestNeighborSearch(node: TreeNode<Point>?, target: Point, currentBestNode: TreeNode<Point>?): TreeNode<Point>? {
        if (node == null) {
            return currentBestNode
        }

        var nearestNode = currentBestNode
        var nearestDistance = currentBestNode?.let { euclideanDistance(it.value, target) } ?: Double.MAX_VALUE
        val currentDistance = euclideanDistance(node.value, target)

        if (currentDistance < nearestDistance) {
            nearestNode = node
            nearestDistance = currentDistance
        }

        val axis = node.value.size // Assuming the node's value represents the dimensionality of the point

        val nearerChild: TreeNode<Point>?
        val furtherChild: TreeNode<Point>?
        if (target[axis % node.value.size] < node.value[axis % node.value.size]) {
            nearerChild = node.left
            furtherChild = node.right
        } else {
            nearerChild = node.right
            furtherChild = node.left
        }

        // Recursively search the nearer child
        val nearerNeighbor = nearestNeighborSearch(nearerChild, target, nearestNode)

        if (nearerNeighbor != null && euclideanDistance(nearerNeighbor.value, target) < nearestDistance) {
            nearestNode = nearerNeighbor
            nearestDistance = euclideanDistance(nearerNeighbor.value, target)
        }

        // Check if it's necessary to search the further child
        if (abs(target[axis % node.value.size] - node.value[axis % node.value.size]) < nearestDistance) {
            val furtherNeighbor = nearestNeighborSearch(furtherChild, target, nearestNode)
            if (furtherNeighbor != null && euclideanDistance(furtherNeighbor.value, target) < nearestDistance) {
                nearestNode = furtherNeighbor
            }
        }

        return nearestNode
    }




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

}

/*
/**
 * Implementation of Merge Sort algorithm in Kotlin.
 *
 * @param list The input MutableList to be sorted.
 * @return A MutableList containing elements of the input list sorted in ascending order.
 */
fun <T: Comparable<T>> MergeSort(list: MutableList<T>): MutableList<T>{
    // Base case: If the list has 0 or 1 elements, it is already sorted
    if (list.size <= 1) {
        return list
    }

    // Find the middle index of the list
    val middle = list.size / 2

    // Recursively sort the left half of the list
    val leftHalf = MergeSort(list.subList(0, middle))

    // Recursively sort the right half of the list
    val rightHalf = MergeSort(list.subList(middle, list.size))

    // Merge the sorted left and right halves
    return merge(leftHalf, rightHalf)
}

/**
 * Merge two sorted lists into one sorted list.
 *
 * @param left The first sorted MutableList.
 * @param right The second sorted MutableList.
 * @return A MutableList containing elements of the input lists merged and sorted in ascending order.
 */
fun <T: Comparable<T>> merge(left: MutableList<T>, right: MutableList<T>): MutableList<T> {
    // Initialize an empty list to store the merged result
    val result = mutableListOf<T>()

    // Initialize indices for traversing left and right lists
    var leftIndex = 0
    var rightIndex = 0

    // Compare elements from both lists and add the smaller one to the result
    while (leftIndex < left.size && rightIndex < right.size) {
        if (left[leftIndex] < right[rightIndex]) {
            result.add(left[leftIndex])
            leftIndex++
        } else {
            result.add(right[rightIndex])
            rightIndex++
        }
    }

    // Append the remaining elements of left and right lists
    while (leftIndex < left.size) {
        result.add(left[leftIndex])
        leftIndex++
    }
    while (rightIndex < right.size) {
        result.add(right[rightIndex])
        rightIndex++
    }

    // Return the merged and sorted list
    return result
}

*/