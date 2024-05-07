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

public class DebugModelBuilder {

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
