package com.github.dgzt.mundus.plugin.ode4j.util;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import org.ode4j.ode.DTriMeshData;

public class Utils3D {

    private static final Vector3 tmpVec = new Vector3();

    public static void fillTriMeshData(
            final GameObject gameObject,
            final ModelInstance modelInstance,
            final DTriMeshData triMeshData
    ) {
        final Array<Float> vertOut = new Array<>();
        final Array<Integer> indexOut = new Array<>();

        getVerticesIndicesFromModel(gameObject, modelInstance, vertOut, indexOut, 0);

        final float[] vertices = new float[vertOut.size];
        final int[] indices = new int[indexOut.size];
        for (int i = 0; i < vertOut.size; ++i) {
            vertices[i] = vertOut.get(i);
        }
        for (int i = 0; i < indexOut.size; ++i) {
            indices[i] = indexOut.get(i);
        }
        triMeshData.build(vertices, indices);
        triMeshData.preprocess();
    }

    public static void fillTriMeshData(
            final Array<Vector3> vertices,
            final IntArray indices,
            final DTriMeshData triMeshData
    ) {
        final float[] verticesOut = new float[vertices.size * 3];
        final int[] indicesOut = new int[indices.size];

        for (int i = 0; i < vertices.size; ++i) {
            final int verticesOutIndex = i * 3;
            final Vector3 vertex = vertices.get(i);

            verticesOut[verticesOutIndex] = vertex.x;
            verticesOut[verticesOutIndex + 1] = vertex.y;
            verticesOut[verticesOutIndex + 2] = vertex.z;
        }
        for (int i = 0; i < indices.size; ++i) {
            indicesOut[i] = indices.get(i);
        }

        triMeshData.build(verticesOut, indicesOut);
        triMeshData.preprocess();
    }

    public static int getVerticesIndicesFromModel(
            final GameObject gameObject,
            ModelInstance modelInstance,
            Array<Float> vertOut,
            Array<Integer> indexOut,
            int indicesOffset
    ) {
        for (int m = 0; m < modelInstance.model.meshes.size; ++m) {
            final Mesh mesh = modelInstance.model.meshes.get(m);

            VertexAttributes vertexAttributes = mesh.getVertexAttributes();
            int offset = vertexAttributes.getOffset(VertexAttributes.Usage.Position);

            int vertexSize = mesh.getVertexSize() / 4;
            int vertCount = mesh.getNumVertices() * mesh.getVertexSize() / 4;

            float[] vertices = new float[vertCount];
            short[] indices = new short[mesh.getNumIndices()];

            mesh.getVertices(vertices);
            mesh.getIndices(indices);

            // Get XYZ vertices position data
            for (int i = 0; i < vertices.length; i += vertexSize) {
                float x = vertices[i + offset];
                float y = vertices[i + 1 + offset];
                float z = vertices[i + 2 + offset];

                // Apply the world transform to the vertices
                tmpVec.set(x, y, z);
                tmpVec.mul(gameObject.getTransform());

                vertOut.add(tmpVec.x);
                vertOut.add(tmpVec.y);
                vertOut.add(tmpVec.z);
            }

            for (short index : indices) {
                indexOut.add((int) index + indicesOffset);
            }

            indicesOffset += vertices.length / vertexSize;
        }
        return indicesOffset;
    }
}
