package com.github.dgzt.mundus.plugin.ode4j.debug;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.github.antzGames.gdx.ode4j.Ode2GdxMathUtils;
import com.github.antzGames.gdx.ode4j.math.DVector3C;
import com.github.antzGames.gdx.ode4j.ode.DBox;
import com.github.antzGames.gdx.ode4j.ode.DCylinder;
import com.github.antzGames.gdx.ode4j.ode.DGeom;
import com.github.antzGames.gdx.ode4j.ode.DSphere;
import com.github.dgzt.mundus.plugin.ode4j.MundusOde4jRuntimePlugin;
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent;
import com.github.dgzt.mundus.plugin.ode4j.type.ShapeType;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import com.mbrlabs.mundus.commons.scene3d.components.ModelComponent;
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent;
import com.mbrlabs.mundus.commons.terrain.Terrain;

public class DebugRenderer {

    private static final Vector3 TMP_VECTOR3 = new Vector3();

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

        modelBatch.begin(cam);
        for (int i = 0; i < physicsComponents.size; ++i) {
            final Ode4jPhysicsComponent physicsComponent = physicsComponents.get(i);

            ModelInstance debugInstance = physicsComponent.getDebugInstance();
            if (debugInstance == null) {
                if (ShapeType.TERRAIN == physicsComponent.getShapeType()) {
                    final TerrainComponent terrainComponent = physicsComponent.gameObject.findComponentByType(Component.Type.TERRAIN);
                    final Terrain terrain = terrainComponent.getTerrainAsset().getTerrain();
                    final DGeom geom = physicsComponent.getGeom();
                    final DVector3C geomPosition = geom.getPosition();
                    final int terrainWidth = terrain.terrainWidth;
                    final int terrainDepth = terrain.terrainDepth;
                    final int vertexResolution = terrain.vertexResolution;

                    debugInstance = DebugModelBuilder.createTerrain(terrainComponent, terrainWidth, terrainDepth, vertexResolution);
                    debugInstance.transform.set(Ode2GdxMathUtils.getGdxQuaternion(geom.getQuaternion()));
                    debugInstance.transform.setTranslation((float) geomPosition.get0(), (float) geomPosition.get1(), (float) geomPosition.get2());
                } else if (ShapeType.BOX == physicsComponent.getShapeType()) {
                    final DBox boxGeom = (DBox) physicsComponent.getGeom();
                    final DVector3C lengths = boxGeom.getLengths();
                    debugInstance = DebugModelBuilder.createBox((float) lengths.get0(), (float) lengths.get1(), (float) lengths.get2());
                    debugInstance.transform.setTranslation(physicsComponent.gameObject.getPosition(TMP_VECTOR3));
                } else if (ShapeType.SPHERE == physicsComponent.getShapeType()) {
                    final DSphere sphereGeom = (DSphere) physicsComponent.getGeom();
                    debugInstance = DebugModelBuilder.createSphere((float) sphereGeom.getRadius());
                    debugInstance.transform.setTranslation(physicsComponent.gameObject.getPosition(TMP_VECTOR3));
                } else if (ShapeType.CYLINDER == physicsComponent.getShapeType()) {
                    final DCylinder cylinderGeom = (DCylinder) physicsComponent.getGeom();
                    debugInstance = DebugModelBuilder.createCylinder((float) cylinderGeom.getRadius(), (float) cylinderGeom.getLength());
                    debugInstance.transform.setTranslation(physicsComponent.gameObject.getPosition(TMP_VECTOR3));
                } else if (ShapeType.MESH == physicsComponent.getShapeType()) {
                    final ModelComponent modelComponent = physicsComponent.gameObject.findComponentByType(Component.Type.MODEL);
                    final ModelInstance modelInstance = modelComponent.getModelInstance();
                    debugInstance = DebugModelBuilder.createLineMesh(modelInstance);
                    debugInstance.transform.setTranslation(physicsComponent.gameObject.getPosition(TMP_VECTOR3));
                }
                physicsComponent.setDebugInstance(debugInstance);
            }

            if (debugInstance != null) {
                modelBatch.render(debugInstance);
            }
        }
        modelBatch.end();
    }

    public void dispose() {
        if (modelBatch != null) {
            modelBatch.dispose();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
