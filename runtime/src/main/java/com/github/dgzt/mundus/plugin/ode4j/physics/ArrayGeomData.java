package com.github.dgzt.mundus.plugin.ode4j.physics;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;

public class ArrayGeomData {

    private Array<Vector3> vertices = new Array<>();
    private IntArray indices = new IntArray();

    public Array<Vector3> getVertices() {
        return vertices;
    }

    public void setVertices(final Array<Vector3> vertices) {
        this.vertices = vertices;
    }

    public IntArray getIndices() {
        return indices;
    }

    public void setIndices(final IntArray indices) {
        this.indices = indices;
    }
}
