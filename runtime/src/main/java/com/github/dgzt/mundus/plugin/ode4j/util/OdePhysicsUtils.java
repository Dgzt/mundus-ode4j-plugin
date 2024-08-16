package com.github.dgzt.mundus.plugin.ode4j.util;

import com.badlogic.gdx.math.Vector3;
import com.github.antzGames.gdx.ode4j.ode.DBody;
import com.github.antzGames.gdx.ode4j.ode.DBox;
import com.github.antzGames.gdx.ode4j.ode.DCylinder;
import com.github.antzGames.gdx.ode4j.ode.DMass;
import com.github.antzGames.gdx.ode4j.ode.DSphere;
import com.github.antzGames.gdx.ode4j.ode.OdeHelper;
import com.github.dgzt.mundus.plugin.ode4j.MundusOde4jRuntimePlugin;
import com.github.dgzt.mundus.plugin.ode4j.physics.PhysicsWorld;

public class OdePhysicsUtils {

    public static final double INVALID_MASS = -1.0;

    /**
     * Creates static box shape.
     *
     * @param goPosition The position of game object.
     * @param geomWidth The width value.
     * @param geomHeight The height value.
     * @param geomDepth The depth value.
     * @return The created static box shape.
     */
    public static DBox createBox(
            final Vector3 goPosition,
            final double geomWidth,
            final double geomHeight,
            final double geomDepth
    ) {
        return createBox(goPosition, geomWidth, geomHeight, geomDepth, INVALID_MASS);
    }

    /**
     * Creates box shape. If mass is lesser than 0 then it will be static shape.
     *
     * @param goPosition The position of game object.
     * @param geomWidth The width value.
     * @param geomHeight The height value.
     * @param geomDepth The depth value.
     * @param mass The mass value.
     * @return The created box shape.
     */
    public static DBox createBox(
            final Vector3 goPosition,
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

            body.setMass(MassUtils.createBoxMass(geomWidth, geomHeight, geomDepth, mass));
            body.setAutoDisableDefaults();
        }

        final DBox geom = physicsWorld.createBox(geomWidth, geomHeight, geomDepth);
        if (isStatic(mass)) {
            geom.setPosition(goPosition.x, goPosition.y, goPosition.z);
        } else {
            geom.setBody(body);
        }

        return geom;
    }

    /**
     * Creates static sphere shape.
     *
     * @param goPosition The position of game object.
     * @param geomRadius The radius value.
     * @return The created static sphere shape.
     */
    public static DSphere createSphere(
            final Vector3 goPosition,
            final double geomRadius
    ) {
        return createSphere(goPosition, geomRadius, INVALID_MASS);
    }

    /**
     * Creates static sphere shape. If mass is lesser than 0 then it will be static shape.
     *
     * @param goPosition The position of game object.
     * @param geomRadius The radius value.
     * @param mass The mass value.
     * @return The created sphere shape.
     */
    public static DSphere createSphere(
            final Vector3 goPosition,
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

            final DMass massInfo = OdeHelper.createMass();
            massInfo.setSphere(1.0, geomRadius);
            massInfo.adjust(mass);

            body.setMass(massInfo);
            body.setAutoDisableDefaults();
        }

        final DSphere geom = physicsWorld.createSphere(geomRadius);
        if (isStatic(mass)) {
            geom.setPosition(goPosition.x, goPosition.y, goPosition.z);
        } else {
            geom.setBody(body);
        }

        return geom;
    }

    /**
     * Creates static cylinder shape.
     *
     * @param goPosition The position of game object.
     * @param geomRadius The radius value.
     * @param geomHeight The height value.
     * @return The created static cylinder shape.
     */
    public static DCylinder createCylinder(
            final Vector3 goPosition,
            final double geomRadius,
            final double geomHeight
    ) {
        return createCylinder(goPosition, geomRadius, geomHeight, INVALID_MASS);
    }

    /**
     * Creates cylinder shape. If mass is lesser than 0 then it will be static shape.
     *
     * @param goPosition The position of game object.
     * @param geomRadius The radius value.
     * @param geomHeight The height value.
     * @param mass The mass value.
     * @return The created cylinder shape.
     */
    public static DCylinder createCylinder(
            final Vector3 goPosition,
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

            final DMass massInfo = OdeHelper.createMass();
            massInfo.setCylinder(1.0, 2, geomRadius, geomHeight);
            massInfo.adjust(mass);

            body.setMass(massInfo);
            body.setAutoDisableDefaults();
        }

        final DCylinder geom = physicsWorld.createCylinder(geomRadius, geomHeight);
        if (isStatic(mass)) {
            geom.setPosition(goPosition.x, goPosition.y, goPosition.z);
        } else {
            geom.setBody(body);
        }

        return geom;
    }

    private static boolean isStatic(final double mass) {
        return mass < 0;
    }
}
