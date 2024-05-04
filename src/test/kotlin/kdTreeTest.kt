package org.example

import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class KdTreeTest {

    @Test
    fun testBuildKdTree() {
        // Pretend dataset of multidimensional points (2D)
        val points = mutableListOf(
            mutableListOf(3, 6),
            mutableListOf(17, 15),
            mutableListOf(13, 15),
            mutableListOf(6, 12),
            mutableListOf(9, 1),
            mutableListOf(2, 7),
            mutableListOf(10, 19)
        )

        // Construct the kd-tree
        val kdTree = kdTree(points)

        // Verify that the root node is not null
        assertNotNull(kdTree.rootNode)

        // Optionally, you can print the kd-tree structure for manual inspection
        kdTree.printkdTree()
    }

    @Test
    fun testSortByAxis() {
        // Pretend dataset of multidimensional points (3D)
        val points = mutableListOf(
            mutableListOf(5, 2, 9),
            mutableListOf(3, 7, 4),
            mutableListOf(8, 1, 6),
            mutableListOf(6, 4, 2),
            mutableListOf(2, 9, 8)
        )

        // Axis to sort along (let's sort along the y-axis, which is axis 1)
        val axis = 1

        // Sort the points based on the y-axis using the sortBy function
        val sortedPoints = kdTree(points).sortBy(points, axis)

        // Expected sorted points based on the y-axis
        val expectedSortedPoints = mutableListOf(
            mutableListOf(8, 1, 6),
            mutableListOf(5, 2, 9),
            mutableListOf(6, 4, 2),
            mutableListOf(3, 7, 4),
            mutableListOf(2, 9, 8)
        )

        // Verify that the sorted points match the expected sorted points
        assertEquals(expectedSortedPoints, sortedPoints)
    }

    @Test
    fun testNearestNeighborSearch() {
        // Pretend dataset of multidimensional points (3D)
        val points = mutableListOf(
            mutableListOf(5, 2, 9),
            mutableListOf(3, 7, 4),
            mutableListOf(8, 1, 6),
            mutableListOf(6, 4, 2),
            mutableListOf(2, 9, 8)
        )

        // Create a kd-tree from the points
        val tree = kdTree(points)
        tree.printkdTree()

        // Target point for nearest neighbor search
        val target = mutableListOf(4, 3, 5)

        // Perform nearest neighbor search
        val nearestNeighbor = tree.nearestNeighbor(target)

        // Expected nearest neighbor based on visual inspection of the points
        val expectedNearestNeighbor = mutableListOf(6, 4, 2)

        // Verify that the nearest neighbor matches the expected nearest neighbor
        assertEquals(expectedNearestNeighbor, nearestNeighbor?.value)
    }

    @Test
    fun testKNearestNeighbors() {
        // Pretend dataset of multidimensional points (2D)
        val points = mutableListOf(
            mutableListOf(3, 4),
            mutableListOf(12, 13),
            mutableListOf(13, 15),
            mutableListOf(2, 17),
            mutableListOf(9, 8),
            mutableListOf(2, 7),
            mutableListOf(10, 11)
        )

        // Construct the kd-tree
        val kdTree = kdTree(points)

        // Target point for k nearest neighbors search
        val target = mutableListOf(10, 10)

        // k value for the k nearest neighbors search
        val k = 3

        // Perform k nearest neighbors search
        val kNearestNeighbors = kdTree.kNearestNeighbors(target, k)

        // Expected k nearest neighbors based on visual inspection of the points
        val expectedKNearestNeighbors = listOf(
            mutableListOf(10, 11),
            mutableListOf(9, 8),
            mutableListOf(12, 13)
        )

        // Verify that the k nearest neighbors match the expected k nearest neighbors
        assertIterableEquals(expectedKNearestNeighbors, kNearestNeighbors.map { it.value })
    }

    @Test
    fun testPointsWithinDistance() {
        // Pretend dataset of multidimensional points (2D)
        val points = mutableListOf(
            mutableListOf(3, 4),
            mutableListOf(12, 13),
            mutableListOf(13, 15),
            mutableListOf(2, 17),
            mutableListOf(9, 8),
            mutableListOf(2, 7),
            mutableListOf(10, 11)
        )

        // Construct the kd-tree
        val kdTree = kdTree(points)

        // Query point for points within distance search
        val queryPoint = mutableListOf(10, 10)

        // Distance within which to find nearby points
        val distance = 5.0

        // Perform points within distance search
        val nearbyPoints = kdTree.pointsWithinDistance(queryPoint, distance)

        // Expected points within distance based on visual inspection of the points
        val expectedNearbyPoints = listOf(
            mutableListOf(10, 11),
            mutableListOf(9, 8),
            mutableListOf(12, 13)
        )

        // Verify that the points within distance match the expected points
        assertIterableEquals(expectedNearbyPoints, nearbyPoints)
    }

}
