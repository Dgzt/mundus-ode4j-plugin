package com.github.dgzt.mundus.plugin.ode4j.util;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.github.antzGames.gdx.ode4j.ode.DBox;
import com.github.antzGames.gdx.ode4j.ode.DCapsule;
import com.github.antzGames.gdx.ode4j.ode.DCylinder;
import com.github.antzGames.gdx.ode4j.ode.DHeightfield;
import com.github.antzGames.gdx.ode4j.ode.DHeightfieldData;
import com.github.antzGames.gdx.ode4j.ode.DSphere;
import com.github.antzGames.gdx.ode4j.ode.DTriMesh;
import com.github.antzGames.gdx.ode4j.ode.DTriMeshData;
import com.github.antzGames.gdx.ode4j.ode.OdeHelper;
import com.github.antzGames.gdx.ode4j.ode.internal.DxTrimeshHeightfield;
import com.github.dgzt.mundus.plugin.ode4j.MundusOde4jRuntimePlugin;
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent;
import com.github.dgzt.mundus.plugin.ode4j.physics.ArrayGeomData;
import com.github.dgzt.mundus.plugin.ode4j.physics.PhysicsWorld;
import com.github.dgzt.mundus.plugin.ode4j.type.ShapeType;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import com.mbrlabs.mundus.commons.scene3d.components.ModelComponent;
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent;
import com.mbrlabs.mundus.commons.terrain.Terrain;

public class Ode4jPhysicsComponentUtils {

    private static final Vector3 TMP_SCALE = new Vector3();
    private static final Vector3 TMP_POSITION = new Vector3();
    private static final Quaternion TMP_QUATERNION = new Quaternion();
    private static final Vector3 TMP_VERTEX_POSITION = new Vector3();

    public static Ode4jPhysicsComponent createTerrainPhysicsComponent(final GameObject gameObject) {
        final TerrainComponent terrainComponent = gameObject.findComponentByType(Component.Type.TERRAIN);
        final Terrain terrain = terrainComponent.getTerrainAsset().getTerrain();
        final double terrainWidth = terrain.terrainWidth;
        final double terrainDepth = terrain.terrainDepth;
        final int vertexResolution = terrain.vertexResolution;
        final Vector3 terrainPosition = gameObject.getPosition(TMP_POSITION);

        final DHeightfield.DHeightfieldGetHeight heightfieldCallback = new DHeightfield.DHeightfieldGetHeight() {
            @Override
            public double call(final Object pUserData, final int x, final int z) {
                return heightfieldCallback(terrainComponent, x, z);
            }
        };

        final DHeightfieldData heightfieldData = OdeHelper.createHeightfieldData();
        heightfieldData.buildCallback(null, heightfieldCallback, terrainWidth, terrainDepth,
                vertexResolution, vertexResolution,
                1.0, 0.0, 0.0, false);

        final DxTrimeshHeightfield heightfield = MundusOde4jRuntimePlugin.getPhysicsWorld().createTrimeshHeightfield(heightfieldData);
        heightfield.setPosition(terrainPosition.x + terrainWidth / 2, terrainPosition.y, terrainPosition.z + terrainDepth / 2);

        return new Ode4jPhysicsComponent(gameObject, ShapeType.TERRAIN, heightfield);
    }

    public static Ode4jPhysicsComponent createTerrainSystemPhysicsComponent(final GameObject gameObject) {
        return new Ode4jPhysicsComponent(gameObject, ShapeType.TERRAIN);
    }

    public static Ode4jPhysicsComponent createBoxPhysicsComponent(
            final GameObject gameObject
    ) {
        return createBoxPhysicsComponent(gameObject, OdePhysicsUtils.INVALID_MASS);
    }

    public static Ode4jPhysicsComponent createBoxPhysicsComponent(
            final GameObject gameObject,
            final double mass
    ) {
        final Vector3 goScale = gameObject.getScale(TMP_SCALE);
        final ModelComponent modelComponent = gameObject.findComponentByType(Component.Type.MODEL);
        final BoundingBox bounds = modelComponent.getOrientedBoundingBox().getBounds();
        final double geomWidth = bounds.getWidth() * goScale.x;
        final double geomHeight = bounds.getHeight() * goScale.y;
        final double geomDepth = bounds.getDepth() * goScale.z;

        return createBoxPhysicsComponent(gameObject, geomWidth, geomHeight, geomDepth, mass);
    }

    public static Ode4jPhysicsComponent createBoxPhysicsComponent(
            final GameObject gameObject,
            final double geomWidth,
            final double geomHeight,
            final double geomDepth
    ) {
        return createBoxPhysicsComponent(gameObject, geomWidth, geomHeight, geomDepth, OdePhysicsUtils.INVALID_MASS);
    }

    public static Ode4jPhysicsComponent createBoxPhysicsComponent(
            final GameObject gameObject,
            final double geomWidth,
            final double geomHeight,
            final double geomDepth,
            final double mass
    ) {
        final Vector3 goPosition = gameObject.getPosition(TMP_POSITION);
        final Quaternion goQuaternion = gameObject.getRotation(TMP_QUATERNION);
        final DBox geom = OdePhysicsUtils.createBox(goPosition, goQuaternion, geomWidth, geomHeight, geomDepth, mass);

        final Ode4jPhysicsComponent physicsComponent = new Ode4jPhysicsComponent(gameObject, ShapeType.BOX, geom);
        if (0 <= mass) {
            physicsComponent.setBody(geom.getBody());
        }

        return physicsComponent;
    }

    public static Ode4jPhysicsComponent createSpherePhysicsComponent(
        final GameObject gameObject,
        final double geomRadius
    ) {
        return createSpherePhysicsComponent(gameObject, geomRadius, OdePhysicsUtils.INVALID_MASS);
    }

    public static Ode4jPhysicsComponent createSpherePhysicsComponent(
        final GameObject gameObject,
        final double geomRadius,
        final double mass
    ) {
        final Vector3 goPosition = gameObject.getPosition(TMP_POSITION);
        final Quaternion goQuaternion = gameObject.getRotation(TMP_QUATERNION);
        final DSphere geom = OdePhysicsUtils.createSphere(goPosition, goQuaternion, geomRadius, mass);

        final Ode4jPhysicsComponent physicsComponent = new Ode4jPhysicsComponent(gameObject, ShapeType.SPHERE, geom);
        if (0 <= mass) {
            physicsComponent.setBody(geom.getBody());
        }

        return physicsComponent;
    }

    public static Ode4jPhysicsComponent createCylinderPhysicsComponent(
            final GameObject gameObject,
            final double geomRadius,
            final double geomHeight
    ) {
        return createCylinderPhysicsComponent(gameObject, geomRadius, geomHeight, OdePhysicsUtils.INVALID_MASS);
    }

    public static Ode4jPhysicsComponent createCylinderPhysicsComponent(
            final GameObject gameObject,
            final double geomRadius,
            final double geomHeight,
            final double mass
    ) {
        final Vector3 goPosition = gameObject.getPosition(TMP_POSITION);
        final Quaternion goQuaternion = gameObject.getRotation(TMP_QUATERNION);

        final DCylinder geom = OdePhysicsUtils.createCylinder(goPosition, goQuaternion, geomRadius, geomHeight, mass);

        final Ode4jPhysicsComponent physicsComponent = new Ode4jPhysicsComponent(gameObject, ShapeType.CYLINDER, geom);
        if (0 <= mass) {
            physicsComponent.setBody(geom.getBody());
        }

        return physicsComponent;
    }

    public static Ode4jPhysicsComponent createCapsulePhysicsComponent(
        final GameObject gameObject,
        final double geomRadius,
        final double geomLength
    ) {
        return createCapsulePhysicsComponent(gameObject, geomRadius, geomLength, OdePhysicsUtils.INVALID_MASS);
    }

    public static Ode4jPhysicsComponent createCapsulePhysicsComponent(
        final GameObject gameObject,
        final double geomRadius,
        final double geomLength,
        final double mass
    ) {
        final Vector3 goPosition = gameObject.getPosition(TMP_POSITION);
        final Quaternion goQuaternion = gameObject.getRotation(TMP_QUATERNION);

        final DCapsule geom = OdePhysicsUtils.createCapsule(goPosition, goQuaternion, geomRadius, geomLength, mass);

        final Ode4jPhysicsComponent physicsComponent = new Ode4jPhysicsComponent(gameObject, ShapeType.CAPSULE, geom);
        if (0 <= mass) {
            physicsComponent.setBody(geom.getBody());
        }

        return physicsComponent;
    }

    public static Ode4jPhysicsComponent createMeshComponent(
        final GameObject gameObject
    ) {
        final PhysicsWorld physicsWorld = MundusOde4jRuntimePlugin.getPhysicsWorld();
        final ModelComponent modelComponent = gameObject.findComponentByType(Component.Type.MODEL);

        final DTriMeshData triMeshData = physicsWorld.createTriMeshData();
        Utils3D.fillTriMeshData(gameObject, modelComponent.getModelInstance(), triMeshData);
        final DTriMesh geom = physicsWorld.createTriMesh(triMeshData);

        return new Ode4jPhysicsComponent(gameObject, ShapeType.MESH, geom);
    }

    public static Ode4jPhysicsComponent createArrayComponent(
       final GameObject gameObject,
       final Array<Vector3> vertices,
       final IntArray indices
    ) {
        return createArrayComponent(gameObject, vertices, indices, OdePhysicsUtils.INVALID_MASS);
    }

    public static Ode4jPhysicsComponent createArrayComponent(
       final GameObject gameObject,
       final Array<Vector3> vertices,
       final IntArray indices,
       final double mass
    ) {
        final ArrayGeomData geomData = new ArrayGeomData();
        geomData.getVertices().clear();
        geomData.getVertices().addAll(vertices);
        geomData.getIndices().clear();
        geomData.getIndices().addAll(indices);

        final Vector3 goPosition = gameObject.getPosition(TMP_POSITION);
        final Quaternion goQuaternion = gameObject.getRotation(TMP_QUATERNION);
        final DTriMesh geom = OdePhysicsUtils.createTriMesh(goPosition, goQuaternion, geomData, mass);

        final Ode4jPhysicsComponent physicsComponent = new Ode4jPhysicsComponent(gameObject, ShapeType.ARRAY, geom);

        if (0 <= mass) {
            physicsComponent.setBody(geom.getBody());
        }

        return physicsComponent;
    }

    public static double heightfieldCallback(
            final TerrainComponent terrainComponent,
            final int cellX,
            final int cellZ
    ) {
        final Terrain terrain = terrainComponent.getTerrainAsset().getTerrain();
        final Vector3 vertexPosition = terrain.getVertexPosition(TMP_VERTEX_POSITION, cellX, cellZ);

        // +0.2 to increase height of the mesh so we can see it if debug instance is visible
        return vertexPosition.y + 0.2;
    }
}
