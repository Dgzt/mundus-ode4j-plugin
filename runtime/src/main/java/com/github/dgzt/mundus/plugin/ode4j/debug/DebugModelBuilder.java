package com.github.dgzt.mundus.plugin.ode4j.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.CylinderShapeBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.SphereShapeBuilder;
import com.badlogic.gdx.math.Vector3;
import com.github.dgzt.mundus.plugin.ode4j.util.Ode4jPhysicsComponentUtils;
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent;

public class DebugModelBuilder {

    public static ModelInstance createTerrain(
            final TerrainComponent terrainComponent,
            final int terrainWidth,
            final int terrainDepth,
            final int vertexResolution
    ) {
        // Set ox and oz to zero for DHEIGHTFIELD_CORNER_ORIGIN mode
        final int ox = (-terrainWidth / 2);
        final int oz = (-terrainDepth / 2);

        final float wsamp = terrainWidth / ((float)vertexResolution - 1);
        final float dsamp = terrainDepth / ((float)vertexResolution - 1);

        final ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();

        int meshNum = 0;
        MeshPartBuilder meshPartBuilder = null;

        // Draw 2 triangles per ABCD box: ABD and AC(D)
        for (int i = 0; i < vertexResolution - 1; ++i) {

            // Split model into more meshes because the vertices num can be more than 64k
            if (i % 30 == 0) {
                meshPartBuilder = modelBuilder.part("line" + (++meshNum), 1, 3, new Material());
                meshPartBuilder.setColor(Color.CYAN);
            }

            for (int j = 0; j < vertexResolution - 1; ++j) {
                final float[] a = new float[3];
                final float[] b = new float[3];
                final float[] c = new float[3];
                final float[] d = new float[3];

                a[0] = ox + (i) * wsamp;
                a[1] = (float) Ode4jPhysicsComponentUtils.heightfieldCallback(terrainComponent, (i), (j));
                a[2] = oz + (j) * dsamp;

                b[0] = ox + (i + 1) * wsamp;
                b[1] = (float) Ode4jPhysicsComponentUtils.heightfieldCallback(terrainComponent, (i + 1), (j));
                b[2] = oz + (j) * dsamp;

                c[0] = ox + (i) * wsamp;
                c[1] = (float) Ode4jPhysicsComponentUtils.heightfieldCallback(terrainComponent, (i), (j + 1));
                c[2] = oz + (j + 1) * dsamp;

                d[0] = ox + (i + 1) * wsamp;
                d[1] = (float) Ode4jPhysicsComponentUtils.heightfieldCallback(terrainComponent, (i + 1), (j + 1));
                d[2] = oz + (j + 1) * dsamp;

                meshPartBuilder.line(a[0], a[1], a[2], b[0], b[1], b[2]);
                meshPartBuilder.line(b[0], b[1], b[2], d[0], d[1], d[2]);
                meshPartBuilder.line(a[0], a[1], a[2], d[0], d[1], d[2]);
                meshPartBuilder.line(a[0], a[1], a[2], c[0], c[1], c[2]);
                meshPartBuilder.line(c[0], c[1], c[2], d[0], d[1], d[2]);
            }
        }

        final Model model = modelBuilder.end();
        return new ModelInstance(model);
    }

    public static ModelInstance createBox(
            final float width,
            final float height,
            final float depth
    ) {
        final ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        final MeshPartBuilder meshPartBuilder = modelBuilder.part(
                "part",
                GL30.GL_LINES,
                VertexAttributes.Usage.Position,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)));
        BoxShapeBuilder.build(meshPartBuilder, width, height, depth);
        final Model model = modelBuilder.end();
        return new ModelInstance(model);
    }

    public static ModelInstance createSphere(
            final float radius
    ) {
        final ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        final MeshPartBuilder meshPartBuilder = modelBuilder.part(
                "part",
                GL30.GL_LINES,
                VertexAttributes.Usage.Position,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)));
        final float diameter = radius * 2;
        SphereShapeBuilder.build(meshPartBuilder, diameter, diameter, diameter, 8, 8);
        final Model model = modelBuilder.end();
        return new ModelInstance(model);
    }

    public static ModelInstance createCylinder(
        final float radius,
        final float height
    ) {
        final ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        final MeshPartBuilder meshPartBuilder = modelBuilder.part(
                "part",
                GL30.GL_LINES,
                VertexAttributes.Usage.Position,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)));
        final float diameter = radius * 2;
        CylinderShapeBuilder.build(meshPartBuilder, diameter, height, diameter, 12);
        final Model model = modelBuilder.end();

        rotateMesh(model);

        return new ModelInstance(model);
    }

    // LibGDX cylinder is oriented along Y axis,  ODE4j on Z axis
    // rotate mesh to match ODE definition of a cylinder with the main axis on Z instead of Y
    // this hard-codes the rotation into the mesh so that we can later use transforms as normal.
    private static void rotateMesh(Model model){
        Vector3 v = new Vector3();
        Mesh mesh = model.meshes.first();
        int n = mesh.getNumVertices();
        int stride = mesh.getVertexSize() / 4;  // size of vertex in number of floats
        float [] vertices = new float[stride*n];
        mesh.getVertices(vertices);
        for(int i = 0 ; i < n; i++) {
            v.set(vertices[i*stride], vertices[i*stride+1], vertices[i*stride+2]);
            v.rotate(Vector3.X, 90);
            vertices[i*stride] = v.x;
            vertices[i*stride+1] = v.y;
            vertices[i*stride+2] = v.z;
        }
        mesh.setVertices(vertices);
    }
}
