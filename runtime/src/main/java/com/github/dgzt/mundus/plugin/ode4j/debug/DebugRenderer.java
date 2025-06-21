package com.github.dgzt.mundus.plugin.ode4j.debug;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.github.dgzt.mundus.plugin.ode4j.MundusOde4jRuntimePlugin;
import com.github.dgzt.mundus.plugin.ode4j.component.AbstractOde4jPhysicsComponent;
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent;
import com.github.dgzt.mundus.plugin.ode4j.physics.ArrayGeomData;
import com.github.dgzt.mundus.plugin.ode4j.type.ShapeType;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import com.mbrlabs.mundus.commons.scene3d.components.ModelComponent;
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent;
import com.mbrlabs.mundus.commons.terrain.Terrain;
import org.ode4j.Ode2GdxMathUtils;
import org.ode4j.math.DVector3C;
import org.ode4j.ode.DBox;
import org.ode4j.ode.DCapsule;
import org.ode4j.ode.DCylinder;
import org.ode4j.ode.DGeom;
import org.ode4j.ode.DSphere;

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

        final Array<AbstractOde4jPhysicsComponent> physicsComponents = MundusOde4jRuntimePlugin.getPhysicsWorld().getPhysicsComponents();

        modelBatch.begin(cam);
        for (int i = 0; i < physicsComponents.size; ++i) {
            final AbstractOde4jPhysicsComponent abstractOde4jPhysicsComponent = physicsComponents.get(i);

            if (abstractOde4jPhysicsComponent instanceof Ode4jPhysicsComponent) {
                final Ode4jPhysicsComponent physicsComponent = (Ode4jPhysicsComponent) abstractOde4jPhysicsComponent;

                ModelInstance debugInstance = physicsComponent.getDebugInstance();
                if (debugInstance == null) {
                    final GameObject gameObject = physicsComponent.gameObject;

                    if (ShapeType.TERRAIN == physicsComponent.getShapeType()) {
                        final TerrainComponent terrainComponent = gameObject.findComponentByType(Component.Type.TERRAIN);
                        // If the terrain component is null then the game object is a parent terrain system game object
                        if (terrainComponent != null) {
                            final Terrain terrain = terrainComponent.getTerrainAsset().getTerrain();
                            final DGeom geom = physicsComponent.getGeom();
                            final DVector3C geomPosition = geom.getPosition();
                            final int terrainWidth = terrain.terrainWidth;
                            final int terrainDepth = terrain.terrainDepth;
                            final int vertexResolution = terrain.vertexResolution;

                            debugInstance = DebugModelBuilder.createTerrain(terrainComponent, terrainWidth, terrainDepth, vertexResolution);
                            debugInstance.transform.set(Ode2GdxMathUtils.getGdxQuaternion(geom.getQuaternion()));
                            debugInstance.transform.setTranslation((float) geomPosition.get0(), (float) geomPosition.get1(), (float) geomPosition.get2());
                        }
                    } else if (ShapeType.BOX == physicsComponent.getShapeType()) {
                        final DBox boxGeom = (DBox) physicsComponent.getGeom();
                        final DVector3C position = boxGeom.getPosition();
                        final DVector3C lengths = boxGeom.getLengths();
                        debugInstance = DebugModelBuilder.createBox((float) lengths.get0(), (float) lengths.get1(), (float) lengths.get2());
                        debugInstance.transform.set(Ode2GdxMathUtils.getGdxQuaternion(boxGeom.getQuaternion()));
                        debugInstance.transform.setTranslation((float) position.get0(), (float) position.get1(), (float) position.get2());
                    } else if (ShapeType.SPHERE == physicsComponent.getShapeType()) {
                        final DSphere sphereGeom = (DSphere) physicsComponent.getGeom();
                        final DVector3C position = sphereGeom.getPosition();
                        debugInstance = DebugModelBuilder.createSphere((float) sphereGeom.getRadius());
                        debugInstance.transform.set(Ode2GdxMathUtils.getGdxQuaternion(sphereGeom.getQuaternion()));
                        debugInstance.transform.setTranslation((float) position.get0(), (float) position.get1(), (float) position.get2());
                    } else if (ShapeType.CYLINDER == physicsComponent.getShapeType()) {
                        final DCylinder cylinderGeom = (DCylinder) physicsComponent.getGeom();
                        final DVector3C position = cylinderGeom.getPosition();
                        debugInstance = DebugModelBuilder.createCylinder((float) cylinderGeom.getRadius(), (float) cylinderGeom.getLength());
                        debugInstance.transform.set(Ode2GdxMathUtils.getGdxQuaternion(cylinderGeom.getQuaternion()));
                        debugInstance.transform.setTranslation((float) position.get0(), (float) position.get1(), (float) position.get2());
                    } else if (ShapeType.CAPSULE == physicsComponent.getShapeType()) {
                        final DCapsule capsuleGeom = (DCapsule) physicsComponent.getGeom();
                        final DVector3C position = capsuleGeom.getPosition();
                        debugInstance = DebugModelBuilder.createCapsule((float) capsuleGeom.getRadius(), (float) capsuleGeom.getLength());
                        debugInstance.transform.set(Ode2GdxMathUtils.getGdxQuaternion(capsuleGeom.getQuaternion()));
                        debugInstance.transform.setTranslation((float) position.get0(), (float) position.get1(), (float) position.get2());
                    } else if (ShapeType.MESH == physicsComponent.getShapeType()) {
                        final ModelComponent modelComponent = gameObject.findComponentByType(Component.Type.MODEL);
                        final ModelInstance modelInstance = modelComponent.getModelInstance();
                        debugInstance = DebugModelBuilder.createLineMesh(modelInstance);
                        debugInstance.transform.mulLeft(physicsComponent.gameObject.getTransform());
                    } else if (ShapeType.ARRAY == physicsComponent.getShapeType()) {
                        final DGeom arrayGeom = physicsComponent.getGeom();
                        final DVector3C position = arrayGeom.getPosition();
                        final ArrayGeomData arrayGeomData = (ArrayGeomData) arrayGeom.getData();
                        final IntArray indices = arrayGeomData.getIndices();
                        if (indices.notEmpty()) {
                            debugInstance = DebugModelBuilder.createLineMesh(arrayGeomData.getVertices(), indices);
                            debugInstance.transform.set(Ode2GdxMathUtils.getGdxQuaternion(arrayGeom.getQuaternion()));
                            debugInstance.transform.setTranslation((float) position.get0(), (float) position.get1(), (float) position.get2());
                        }
                    }
                    physicsComponent.setDebugInstance(debugInstance);
                }
            }

            abstractOde4jPhysicsComponent.debugRender(modelBatch);
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
