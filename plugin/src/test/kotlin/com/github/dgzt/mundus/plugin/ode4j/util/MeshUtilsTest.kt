package com.github.dgzt.mundus.plugin.ode4j.util

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.IntArray
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MeshUtilsTest {

    companion object {
        const val TRIANGLE_VERTEX_NUM = 3
    }

    @Test
    fun testCoplanarShape() {
        // given
        val vertices = Array<Vector3>()
        vertices.add(Vector3(-1.0f, 0.0f, -1.0f)) // bottom left
        vertices.add(Vector3(-1.0f, 0.0f,  1.0f)) // top left
        vertices.add(Vector3( 1.0f, 0.0f, -1.0f)) // bottom right
        vertices.add(Vector3( 1.0f, 0.0f,  1.0f)) // top right

        // and
        val indices = IntArray()

        // when
        MeshUtils.generateIndices(vertices, indices)

        // then
        Assertions.assertEquals(0, indices.size)
    }

    @Test
    fun testPyramid() {
        // given
        val vertices = Array<Vector3>()
        vertices.add(Vector3(-1.0f, 0.0f, -1.0f)) // bottom left
        vertices.add(Vector3(-1.0f, 0.0f,  1.0f)) // top left
        vertices.add(Vector3( 1.0f, 0.0f, -1.0f)) // bottom right
        vertices.add(Vector3( 1.0f, 0.0f,  1.0f)) // top right
        vertices.add(Vector3( 0.0f, 1.0f,  0.0f)) // (top) center

        // and
        val indices = IntArray()

        // when
        MeshUtils.generateIndices(vertices, indices)

        // then
        Assertions.assertEquals(6 * TRIANGLE_VERTEX_NUM, indices.size)
        val expected = IntArray()
        // (top) center - bottom right - bottom left triangle
        expected.addAll(4, 2, 0)
        // (top) center - bottom left - top left triangle
        expected.addAll(4, 0, 1)
        // top right - bottom right - (top) center triangle
        expected.addAll(3, 2, 4)
        // top right - (top) center - top left triangle
        expected.addAll(3, 4, 1)
        // top right - top left - bottom left triangle
        expected.addAll(3, 1, 0)
        // top right - bottom left - bottom right triangle
        expected.addAll(3, 0, 2)
        Assertions.assertEquals(expected, indices)
    }

    @Test
    fun testHouse() {
        // given
        val vertices = Array<Vector3>()
        vertices.add(Vector3(-1.0f, 0.0f, -1.0f)) // near bottom left
        vertices.add(Vector3( 1.0f, 0.0f, -1.0f)) // near bottom right
        vertices.add(Vector3(-1.0f, 0.0f,  1.0f)) // far bottom left
        vertices.add(Vector3( 1.0f, 0.0f,  1.0f)) // far bottom right
        vertices.add(Vector3(-1.0f, 1.0f, -1.0f)) // near top left
        vertices.add(Vector3( 1.0f, 1.0f, -1.0f)) // near top right
        vertices.add(Vector3(-1.0f, 1.0f,  1.0f)) // far top left
        vertices.add(Vector3( 1.0f, 1.0f,  1.0f)) // far top right
        vertices.add(Vector3( 0.0f, 2.0f,  0.0f)) // top of roof

        // and
        val indices = IntArray()

        // when
        MeshUtils.generateIndices(vertices, indices)

        // then
        Assertions.assertEquals(14 * TRIANGLE_VERTEX_NUM, indices.size)
        val expected = IntArray()
        // near top left - far top left - top of roof
        expected.addAll(4, 6, 8)
        // near top left - near bottom left - far bottom left
        expected.addAll(4, 0, 2)
        // near top left - far bottom left - far top left
        expected.addAll(4, 2, 6)
        // far bottom right - far bottom left - near bottom left
        expected.addAll(3, 2, 0)
        // far bottom right - near bottom left - near bottom right
        expected.addAll(3, 0, 1)
        // near top right - near bottom right - near bottom left
        expected.addAll(5, 1, 0)
        // near top right - near bottom left - near top left
        expected.addAll(5, 0, 4)
        // near top right - near top left - top of roof
        expected.addAll(5, 4, 8)
        // far top right - far bottom right - near bottom right
        expected.addAll(7, 3, 1)
        // far top right - near bottom right - near top right
        expected.addAll(7, 1, 5)
        // far top right - near top right - top of roof
        expected.addAll(7, 5, 8)
        // far top right - top of roof - far top left
        expected.addAll(7, 8, 6)
        // far top right - far top left - far bottom left
        expected.addAll(7, 6, 2)
        // far top right - far bottom left - far bottom right
        expected.addAll(7, 2, 3)

        Assertions.assertEquals(expected, indices)
    }

}
