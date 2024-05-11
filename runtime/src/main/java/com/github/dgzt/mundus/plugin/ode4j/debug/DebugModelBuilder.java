package com.github.dgzt.mundus.plugin.ode4j.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
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

        final int wsamp = terrainWidth / (vertexResolution - 1);
        final int dsamp = terrainDepth / (vertexResolution - 1);

        final ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        final MeshPartBuilder meshPartBuilder = modelBuilder.part("line", 1, 3, new Material());
        meshPartBuilder.setColor(Color.CYAN);

        // Draw 2 triangles per ABCD box: ABC and BCD
        for (int i = 0; i < vertexResolution; ++i) {
            for (int j = 0; j < vertexResolution; ++j) {
                final float[] a = new float[3];
                final float[] b = new float[3];
                final float[] c = new float[3];
                final float[] d = new float[3];

                a[0] = ox + (i) * wsamp;
                a[1] = (float) Ode4jPhysicsComponentUtils.heightfieldCallback(terrainComponent, (i) * wsamp, (j) * dsamp);
                a[2] = oz + (j) * dsamp;

                b[0] = ox + (i + 1) * wsamp;
                b[1] = (float) Ode4jPhysicsComponentUtils.heightfieldCallback(terrainComponent, (i + 1) * wsamp, (j) * dsamp);
                b[2] = oz + (j) * dsamp;

                c[0] = ox + (i) * wsamp;
                c[1] = (float) Ode4jPhysicsComponentUtils.heightfieldCallback(terrainComponent, (i) * wsamp, (j + 1) * dsamp);
                c[2] = oz + (j + 1) * dsamp;

                d[0] = ox + (i + 1) * wsamp;
                d[1] = (float) Ode4jPhysicsComponentUtils.heightfieldCallback(terrainComponent, (i + 1) * wsamp, (j + 1) * dsamp);
                d[2] = oz + (j + 1) * dsamp;

                meshPartBuilder.line(a[0], a[1], a[2], b[0], b[1], b[2]);
                meshPartBuilder.line(b[0], b[1], b[2], c[0], c[1], c[2]);
                meshPartBuilder.line(c[0], c[1], c[2], a[0], a[1], a[2]);
                meshPartBuilder.line(b[0], b[1], b[2], c[0], c[1], c[2]);
                meshPartBuilder.line(c[0], c[1], c[2], d[0], d[1], d[2]);
                meshPartBuilder.line(d[0], d[1], d[2], b[0], b[1], b[2]);
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
}
