package com.github.dgzt.mundus.plugin.ode4j.util

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.IntArray
import com.github.quickhull3d.Point3d
import com.github.quickhull3d.QuickHull3D
import com.sun.j3d.utils.geometry.GeometryInfo

object MeshUtils {

    fun generateIndices(vertices: Array<Vector3>, indices: IntArray) {
        indices.clear()

        if (vertices.size < 3) {
            throw RuntimeException("The number of vertices should be more then 3!")
        } else if (vertices.size == 3) {
            indices.addAll(0, 1, 2)
            return
        }

        val faceIndices = generateFaces(vertices)

        // Generates indices for only non-coplanar shapes
        if (faceIndices.isNotEmpty()) {
            var gPoints = arrayOf<javax.vecmath.Point3d>()
            for (vertex in vertices) {
                gPoints += javax.vecmath.Point3d(vertex.x.toDouble(), vertex.y.toDouble(), vertex.z.toDouble())
            }

            var coordinateIndices = intArrayOf()
            for (face in faceIndices) {
                coordinateIndices += face
            }

            var stripCounts = intArrayOf()
            for (face in faceIndices) {
                stripCounts += face.size
            }

            val gi = GeometryInfo(GeometryInfo.POLYGON_ARRAY)
            gi.setCoordinates(gPoints)
            gi.coordinateIndices = coordinateIndices
            gi.stripCounts = stripCounts

            gi.convertToIndexedTriangles()

            for (i in gi.coordinateIndices) {
                indices.add(i)
            }
        }
    }

    private fun generateFaces(vertices: Array<Vector3>): kotlin.Array<kotlin.IntArray> {
        var points = arrayOf<Point3d>()
        for (vertex in vertices) {
            points += Point3d(vertex.x.toDouble(), vertex.y.toDouble(), vertex.z.toDouble())
        }

        try {
            val hull = QuickHull3D()
            hull.build(points)
            return hull.faces
        } catch (ex: IllegalArgumentException) {
            if (ex.message == "Input points appear to be coplanar") {
                return arrayOf()
            } else {
                throw ex
            }
        }
    }

}
