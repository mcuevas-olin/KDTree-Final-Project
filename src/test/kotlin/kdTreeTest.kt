package org.example

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class KdTreeTest {

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
        val sortedPoints = kdTree().sortBy(points, axis)

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
}
