package com.github.dgzt.mundus.plugin.ode4j.util;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.github.antzGames.gdx.ode4j.ode.DBody;
import com.github.antzGames.gdx.ode4j.ode.DBox;
import com.github.antzGames.gdx.ode4j.ode.DCylinder;
import com.github.antzGames.gdx.ode4j.ode.DHeightfield;
import com.github.antzGames.gdx.ode4j.ode.DHeightfieldData;
import com.github.antzGames.gdx.ode4j.ode.DMass;
import com.github.antzGames.gdx.ode4j.ode.OdeHelper;
import com.github.antzGames.gdx.ode4j.ode.internal.DxTrimeshHeightfield;
import com.github.dgzt.mundus.plugin.ode4j.MundusOde4jRuntimePlugin;
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent;
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

    public static Ode4jPhysicsComponent createBoxPhysicsComponent(
            final GameObject gameObject,
            final boolean isStatic
    ) {
        final Vector3 goScale = gameObject.getScale(TMP_SCALE);
        final ModelComponent modelComponent = gameObject.findComponentByType(Component.Type.MODEL);
        final BoundingBox bounds = modelComponent.getOrientedBoundingBox().getBounds();
        final double geomWidth = bounds.getWidth() * goScale.x;
        final double geomHeight = bounds.getHeight() * goScale.y;
        final double geomDepth = bounds.getDepth() * goScale.z;

        return createBoxPhysicsComponent(gameObject, isStatic, geomWidth, geomHeight, geomDepth);
    }

    public static Ode4jPhysicsComponent createBoxPhysicsComponent(
            final GameObject gameObject,
            final boolean isStatic,
            final double geomWidth,
            final double geomHeight,
            final double geomDepth
    ) {
        final PhysicsWorld physicsWorld = MundusOde4jRuntimePlugin.getPhysicsWorld();
        final Vector3 goPosition = gameObject.getPosition(TMP_POSITION);

        final DBody body;
        if (!isStatic) {
            body = physicsWorld.createBody();
            body.setPosition(goPosition.x, goPosition.y, goPosition.z);

            final DMass massInfo = OdeHelper.createMass();
            massInfo.setBox(1.0, geomWidth, geomHeight, geomDepth);
            massInfo.adjust(10.0);

            body.setMass(massInfo);
            body.setAutoDisableDefaults();
        } else {
            body = null;
        }

        final DBox geom = physicsWorld.createBox(geomWidth, geomHeight, geomDepth);
        if (isStatic) {
            geom.setPosition(goPosition.x, goPosition.y, goPosition.z);
        } else {
            geom.setBody(body);
        }

        final Ode4jPhysicsComponent physicsComponent = new Ode4jPhysicsComponent(gameObject, ShapeType.BOX, geom);
        if (!isStatic) {
            physicsComponent.setBody(body);
        }

        return physicsComponent;
    }

    public static Ode4jPhysicsComponent createCylinderPhysicsComponent(
            final GameObject gameObject,
            final double geomRadius,
            final double geomHeight
    ) {
        final PhysicsWorld physicsWorld = MundusOde4jRuntimePlugin.getPhysicsWorld();
        final Vector3 goPosition = gameObject.getPosition(TMP_POSITION);

        final DCylinder geom = physicsWorld.createCylinder(geomRadius, geomHeight);
        geom.setPosition(goPosition.x, goPosition.y, goPosition.z);

        return new Ode4jPhysicsComponent(gameObject, ShapeType.CYLINDER, geom);
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
