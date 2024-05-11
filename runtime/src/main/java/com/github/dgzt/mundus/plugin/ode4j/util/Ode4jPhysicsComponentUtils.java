package com.github.dgzt.mundus.plugin.ode4j.util;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.github.antzGames.gdx.ode4j.ode.DBox;
import com.github.dgzt.mundus.plugin.ode4j.MundusOde4jRuntimePlugin;
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent;
import com.github.dgzt.mundus.plugin.ode4j.type.ShapeType;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import com.mbrlabs.mundus.commons.scene3d.components.ModelComponent;

public class Ode4jPhysicsComponentUtils {

    private static final Vector3 TMP_SCALE = new Vector3();
    private static final Vector3 TMP_POSITION = new Vector3();

    public static Ode4jPhysicsComponent createTerrainPhysicsComponent(final GameObject gameObject) {
        return null; // TODO implement later
    }

    public static Ode4jPhysicsComponent createBoxPhysicsComponent(final GameObject gameObject) {
        final Vector3 goScale = gameObject.getScale(TMP_SCALE);
        final ModelComponent modelComponent = gameObject.findComponentByType(Component.Type.MODEL);
        final BoundingBox bounds = modelComponent.getOrientedBoundingBox().getBounds();
        final double geomWidth = bounds.getWidth() * goScale.x;
        final double geomHeight = bounds.getHeight() * goScale.y;
        final double geomDepth = bounds.getDepth() * goScale.z;

        return createBoxPhysicsComponent(gameObject, geomWidth, geomHeight, geomDepth);
    }

    public static Ode4jPhysicsComponent createBoxPhysicsComponent(
            final GameObject gameObject,
            final double geomWidth,
            final double geomHeight,
            final double geomDepth
    ) {
        final Vector3 goPosition = gameObject.getPosition(TMP_POSITION);
        final DBox geom = MundusOde4jRuntimePlugin.getPhysicsWorld().createBox(geomWidth, geomHeight, geomDepth);
        geom.setPosition(goPosition.x, goPosition.y, goPosition.z);

        return new Ode4jPhysicsComponent(gameObject, ShapeType.BOX, geom);
    }

}
