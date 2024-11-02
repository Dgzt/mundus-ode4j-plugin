package com.github.dgzt.mundus.plugin.ode4j.util;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.github.antzGames.gdx.ode4j.Ode2GdxMathUtils;
import com.github.antzGames.gdx.ode4j.ode.DBody;
import com.github.antzGames.gdx.ode4j.ode.DBox;
import com.github.antzGames.gdx.ode4j.ode.DCapsule;
import com.github.antzGames.gdx.ode4j.ode.DCylinder;
import com.github.antzGames.gdx.ode4j.ode.DSphere;
import com.github.antzGames.gdx.ode4j.ode.DTriMesh;
import com.github.antzGames.gdx.ode4j.ode.DTriMeshData;
import com.github.dgzt.mundus.plugin.ode4j.MundusOde4jRuntimePlugin;
import com.github.dgzt.mundus.plugin.ode4j.physics.ArrayGeomData;
import com.github.dgzt.mundus.plugin.ode4j.physics.PhysicsWorld;

public class OdePhysicsUtils {

    public static final double INVALID_MASS = -1.0;

    /**
     * Creates static box shape.
     *
     * @param goPosition The position of game object.
     * @param goRotation The rotation of game object. This can be null.
     * @param geomWidth The width value.
     * @param geomHeight The height value.
     * @param geomDepth The depth value.
     * @return The created static box shape.
     */
    public static DBox createBox(
            final Vector3 goPosition,
            final Quaternion goRotation,
            final double geomWidth,
            final double geomHeight,
            final double geomDepth
    ) {
        return createBox(goPosition, goRotation, geomWidth, geomHeight, geomDepth, INVALID_MASS);
    }

    /**
     * Creates box shape. If mass is lesser than 0 then it will be static shape.
     *
     * @param goPosition The position of game object.
     * @param goRotation The rotation of game object. This can be null.
     * @param geomWidth The width value.
     * @param geomHeight The height value.
     * @param geomDepth The depth value.
     * @param mass The mass value.
     * @return The created box shape.
     */
    public static DBox createBox(
            final Vector3 goPosition,
            final Quaternion goRotation,
            final double geomWidth,
            final double geomHeight,
            final double geomDepth,
            final double mass
    ) {
        final PhysicsWorld physicsWorld = MundusOde4jRuntimePlugin.getPhysicsWorld();

        final DBody body;
        if (isStatic(mass)) {
            body = null;
        } else {
            body = physicsWorld.createBody();
            body.setPosition(goPosition.x, goPosition.y, goPosition.z);
            if (goRotation != null) {
                body.setQuaternion(Ode2GdxMathUtils.getOde4jQuaternion(goRotation));
            }

            body.setMass(MassUtils.createBoxMass(geomWidth, geomHeight, geomDepth, mass));
            body.setAutoDisableDefaults();
        }

        final DBox geom = physicsWorld.createBox(geomWidth, geomHeight, geomDepth);
        if (isStatic(mass)) {
            geom.setPosition(goPosition.x, goPosition.y, goPosition.z);
            if (goRotation != null) {
                geom.setQuaternion(Ode2GdxMathUtils.getOde4jQuaternion(goRotation));
            }
        } else {
            geom.setBody(body);
        }

        return geom;
    }

    /**
     * Creates static sphere shape.
     *
     * @param goPosition The position of game object.
     * @param goRotation The rotation of game object. This can be null.
     * @param geomRadius The radius value.
     * @return The created static sphere shape.
     */
    public static DSphere createSphere(
            final Vector3 goPosition,
            final Quaternion goRotation,
            final double geomRadius
    ) {
        return createSphere(goPosition, goRotation, geomRadius, INVALID_MASS);
    }

    /**
     * Creates static sphere shape. If mass is lesser than 0 then it will be static shape.
     *
     * @param goPosition The position of game object.
     * @param goRotation The rotation of game object. This can be null.
     * @param geomRadius The radius value.
     * @param mass The mass value.
     * @return The created sphere shape.
     */
    public static DSphere createSphere(
            final Vector3 goPosition,
            final Quaternion goRotation,
            final double geomRadius,
            final double mass
    ) {
        final PhysicsWorld physicsWorld = MundusOde4jRuntimePlugin.getPhysicsWorld();

        final DBody body;
        if (isStatic(mass)) {
            body = null;
        } else {
            body = physicsWorld.createBody();
            body.setPosition(goPosition.x, goPosition.y, goPosition.z);
            if (goRotation != null) {
                body.setQuaternion(Ode2GdxMathUtils.getOde4jQuaternion(goRotation));
            }

            body.setMass(MassUtils.createSphereMass(geomRadius, mass));
            body.setAutoDisableDefaults();
        }

        final DSphere geom = physicsWorld.createSphere(geomRadius);
        if (isStatic(mass)) {
            geom.setPosition(goPosition.x, goPosition.y, goPosition.z);
            if (goRotation != null) {
                geom.setQuaternion(Ode2GdxMathUtils.getOde4jQuaternion(goRotation));
            }
        } else {
            geom.setBody(body);
        }

        return geom;
    }

    /**
     * Creates static cylinder shape.
     *
     * @param goPosition The position of game object.
     * @param goRotation The rotation of game object. This can be null.
     * @param geomRadius The radius value.
     * @param geomHeight The height value.
     * @return The created static cylinder shape.
     */
    public static DCylinder createCylinder(
            final Vector3 goPosition,
            final Quaternion goRotation,
            final double geomRadius,
            final double geomHeight
    ) {
        return createCylinder(goPosition, goRotation, geomRadius, geomHeight, INVALID_MASS);
    }

    /**
     * Creates cylinder shape. If mass is lesser than 0 then it will be static shape.
     *
     * @param goPosition The position of game object.
     * @param goRotation The rotation of game object. This can be null.
     * @param geomRadius The radius value.
     * @param geomHeight The height value.
     * @param mass The mass value.
     * @return The created cylinder shape.
     */
    public static DCylinder createCylinder(
            final Vector3 goPosition,
            final Quaternion goRotation,
            final double geomRadius,
            final double geomHeight,
            final double mass
    ) {
        final PhysicsWorld physicsWorld = MundusOde4jRuntimePlugin.getPhysicsWorld();

        final DBody body;
        if (isStatic(mass)) {
            body = null;
        } else {
            body = physicsWorld.createBody();
            body.setPosition(goPosition.x, goPosition.y, goPosition.z);
            if (goRotation != null) {
                body.setQuaternion(Ode2GdxMathUtils.getOde4jQuaternion(goRotation));
            }

            body.setMass(MassUtils.createCylinderMass(geomRadius, geomHeight, mass));
            body.setAutoDisableDefaults();
        }

        final DCylinder geom = physicsWorld.createCylinder(geomRadius, geomHeight);
        if (isStatic(mass)) {
            geom.setPosition(goPosition.x, goPosition.y, goPosition.z);
            if (goRotation != null) {
                geom.setQuaternion(Ode2GdxMathUtils.getOde4jQuaternion(goRotation));
            }
        } else {
            geom.setBody(body);
        }

        return geom;
    }

    /**
     * Creates static capsule shape.
     *
     * @param goPosition The position of game object.
     * @param goRotation The rotation of game object. This can be null.
     * @param geomRadius The radius value.
     * @param geomLength The length value.
     * @return The created capsule shape.
     */
    public static DCapsule createCapsule(
            final Vector3 goPosition,
            final Quaternion goRotation,
            final double geomRadius,
            final double geomLength
    ) {
        return createCapsule(goPosition, goRotation, geomRadius, geomLength, INVALID_MASS);
    }

    /**
     * Creates capsule shape. If mass is lesser than 0 then it will be static shape.
     *
     * @param goPosition The position of game object.
     * @param goRotation The rotation of game object. This can be null.
     * @param geomRadius The radius value.
     * @param geomLength The length value.
     * @param mass The mass value.
     * @return The created cylinder shape.
     */
    public static DCapsule createCapsule(
            final Vector3 goPosition,
            final Quaternion goRotation,
            final double geomRadius,
            final double geomLength,
            final double mass
    ) {
        final PhysicsWorld physicsWorld = MundusOde4jRuntimePlugin.getPhysicsWorld();

        final DBody body;
        if (isStatic(mass)) {
            body = null;
        } else {
            body = physicsWorld.createBody();
            body.setPosition(goPosition.x, goPosition.y, goPosition.z);
            if (goRotation != null) {
                body.setQuaternion(Ode2GdxMathUtils.getOde4jQuaternion(goRotation));
            }

            body.setMass(MassUtils.createCapsuleMass(geomRadius, geomLength, mass));
            body.setAutoDisableDefaults();
        }

        final DCapsule geom = physicsWorld.createCapsule(geomRadius, geomLength);
        if (isStatic(mass)) {
            geom.setPosition(goPosition.x, goPosition.y, goPosition.z);
            if (goRotation != null) {
                geom.setQuaternion(Ode2GdxMathUtils.getOde4jQuaternion(goRotation));
            }
        } else {
            geom.setBody(body);
        }

        return geom;
    }

    public static DTriMesh createTriMesh(
            final Vector3 goPosition,
            final Quaternion goRotation,
            final ArrayGeomData geomData
    ) {
        return createTriMesh(goPosition, goRotation, geomData, INVALID_MASS);
    }

    public static DTriMesh createTriMesh(
            final Vector3 goPosition,
            final Quaternion goRotation,
            final ArrayGeomData geomData,
            final double mass
    ) {
        final PhysicsWorld physicsWorld = MundusOde4jRuntimePlugin.getPhysicsWorld();

        final DTriMeshData triMeshData = physicsWorld.createTriMeshData();
        Utils3D.fillTriMeshData(geomData.getVertices(), geomData.getIndices(), triMeshData);
        final DTriMesh geom = physicsWorld.createTriMesh(triMeshData);
        geom.setData(geomData);

        if (isStatic(mass)) {
            geom.setPosition(goPosition.x, goPosition.y, goPosition.z);
            if (goRotation != null) {
                geom.setQuaternion(Ode2GdxMathUtils.getOde4jQuaternion(goRotation));
            }
        } else {
            final DBody body = physicsWorld.createBody();
            body.setPosition(goPosition.x, goPosition.y, goPosition.z);
            if (goRotation != null) {
                body.setQuaternion(Ode2GdxMathUtils.getOde4jQuaternion(goRotation));
            }

            body.setMass(MassUtils.createArrayMass(geom, mass));
            body.setAutoDisableDefaults();

            geom.setBody(body);
        }

        return geom;
    }

    private static boolean isStatic(final double mass) {
        return mass < 0;
    }
}
