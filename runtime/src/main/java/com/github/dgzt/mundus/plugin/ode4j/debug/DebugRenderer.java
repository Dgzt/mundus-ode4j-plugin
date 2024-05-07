package com.github.dgzt.mundus.plugin.ode4j.debug;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Array;
import com.github.antzGames.gdx.ode4j.math.DVector3C;
import com.github.antzGames.gdx.ode4j.ode.DBox;
import com.github.dgzt.mundus.plugin.ode4j.MundusOde4jRuntimePlugin;
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent;
import com.github.dgzt.mundus.plugin.ode4j.type.ShapeType;

public class DebugRenderer {

    private boolean enabled = false;
    private ModelBatch modelBatch;

    public void render(final Camera cam) {
        if (!enabled) {
            return;
        }

        if (modelBatch == null) {
            modelBatch = new ModelBatch();
        }

        final Array<Ode4jPhysicsComponent> physicsComponents = MundusOde4jRuntimePlugin.getPhysicsWorld().getPhysicsComponents();

        if (physicsComponents.notEmpty()) {
            modelBatch.begin(cam);
        }

        for (int i = 0; i < physicsComponents.size; ++i) {
            final Ode4jPhysicsComponent physicsComponent = physicsComponents.get(i);

            ModelInstance debugInstance = physicsComponent.getDebugInstance();
            if (debugInstance == null) {
                if (ShapeType.BOX == physicsComponent.getShapeType()) {
                    final DBox boxGeom = (DBox) physicsComponent.getGeom();
                    DVector3C lengths = boxGeom.getLengths();
                    debugInstance = DebugModelBuilder.createBox((float) lengths.get0(), (float) lengths.get1(), (float) lengths.get2());
                }
                physicsComponent.setDebugInstance(debugInstance);
            }

            if (debugInstance != null) {
                modelBatch.render(debugInstance);
            }
        }

        if (physicsComponents.notEmpty()) {
            modelBatch.end();
        }
    }

    public void dispose() {
        if (modelBatch != null) {
            modelBatch = new ModelBatch();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
