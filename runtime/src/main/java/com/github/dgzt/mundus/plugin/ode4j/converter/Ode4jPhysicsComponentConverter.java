package com.github.dgzt.mundus.plugin.ode4j.converter;

import com.badlogic.gdx.utils.OrderedMap;
import com.github.antzGames.gdx.ode4j.math.DVector3C;
import com.github.antzGames.gdx.ode4j.ode.DBox;
import com.github.antzGames.gdx.ode4j.ode.DCylinder;
import com.github.antzGames.gdx.ode4j.ode.DSphere;
import com.github.dgzt.mundus.plugin.ode4j.MundusOde4jRuntimePlugin;
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent;
import com.github.dgzt.mundus.plugin.ode4j.constant.SaveConstants;
import com.github.dgzt.mundus.plugin.ode4j.type.ShapeType;
import com.github.dgzt.mundus.plugin.ode4j.util.Ode4jPhysicsComponentUtils;
import com.mbrlabs.mundus.commons.mapper.CustomComponentConverter;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;

public class Ode4jPhysicsComponentConverter implements CustomComponentConverter {

    @Override
    public Component.Type getComponentType() {
        return Component.Type.PHYSICS;
    }

    @Override
    public OrderedMap<String, String> convert(final Component component) {
        if (!(component instanceof Ode4jPhysicsComponent)) {
            return null;
        }

        final OrderedMap<String, String> map = new OrderedMap<>();
        final Ode4jPhysicsComponent ode4jComponent = (Ode4jPhysicsComponent) component;
        map.put(SaveConstants.SHAPE, ode4jComponent.getShapeType().name());

        if (ShapeType.BOX == ode4jComponent.getShapeType()) {
            final DBox boxGeom = (DBox) ode4jComponent.getGeom();
            final DVector3C boxLength = boxGeom.getLengths();

            map.put(SaveConstants.BOX_STATIC, String.valueOf(true));
            map.put(SaveConstants.BOX_WIDTH, String.valueOf(boxLength.get0()));
            map.put(SaveConstants.BOX_HEIGHT, String.valueOf(boxLength.get1()));
            map.put(SaveConstants.BOX_DEPTH, String.valueOf(boxLength.get2()));
        } else if (ShapeType.SPHERE == ode4jComponent.getShapeType()) {
            final DSphere sphereGeom = (DSphere) ode4jComponent.getGeom();
            final double radius = sphereGeom.getRadius();

            map.put(SaveConstants.SPHERE_STATIC, String.valueOf(true));
            map.put(SaveConstants.SPHERE_RADIUS, String.valueOf(radius));
        } else if (ShapeType.CYLINDER == ode4jComponent.getShapeType()) {
            final DCylinder cylinderGeom = (DCylinder) ode4jComponent.getGeom();
            final double radius = cylinderGeom.getRadius();
            final double height = cylinderGeom.getLength();

            map.put(SaveConstants.CYLINDER_STATIC, String.valueOf(true));
            map.put(SaveConstants.CYLINDER_RADIUS, String.valueOf(radius));
            map.put(SaveConstants.CYLINDER_HEIGHT, String.valueOf(height));
        } else if (ShapeType.MESH == ode4jComponent.getShapeType()) {
            // NOOP
        }

        return map;
    }

    @Override
    public Component convert(final GameObject gameObject, OrderedMap<String, String> orderedMap) {
        final ShapeType shapeType = ShapeType.valueOf(orderedMap.get(SaveConstants.SHAPE));

        final Ode4jPhysicsComponent physicsComponent;
        switch (shapeType) {
            case TERRAIN:
                physicsComponent = Ode4jPhysicsComponentUtils.createTerrainPhysicsComponent(gameObject);
                break;
            case BOX:
                final boolean boxStatic = Boolean.parseBoolean(orderedMap.get(SaveConstants.BOX_STATIC));
                final double boxWidth = Double.parseDouble(orderedMap.get(SaveConstants.BOX_WIDTH));
                final double boxHeight = Double.parseDouble(orderedMap.get(SaveConstants.BOX_HEIGHT));
                final double boxDepth = Double.parseDouble(orderedMap.get(SaveConstants.BOX_DEPTH));
                physicsComponent = Ode4jPhysicsComponentUtils.createBoxPhysicsComponent(gameObject, boxStatic, boxWidth, boxHeight, boxDepth);
                break;
            case SPHERE:
                final boolean sphereStatic = Boolean.parseBoolean(orderedMap.get(SaveConstants.SPHERE_STATIC));
                final double sphereRadius = Double.parseDouble(orderedMap.get(SaveConstants.SPHERE_RADIUS));
                physicsComponent = Ode4jPhysicsComponentUtils.createSpherePhysicsComponent(gameObject, sphereStatic, sphereRadius);
                break;
            case CYLINDER:
                final boolean cylinderStatic = Boolean.parseBoolean(orderedMap.get(SaveConstants.CYLINDER_STATIC));
                final double cylinderRadius = Double.parseDouble(orderedMap.get(SaveConstants.CYLINDER_RADIUS));
                final double cylinderHeight = Double.parseDouble(orderedMap.get(SaveConstants.CYLINDER_HEIGHT));
                physicsComponent = Ode4jPhysicsComponentUtils.createCylinderPhysicsComponent(gameObject, cylinderStatic, cylinderRadius, cylinderHeight);
                break;
            case MESH:
                physicsComponent = Ode4jPhysicsComponentUtils.createMeshComponent(gameObject);
                break;
            default: throw new RuntimeException("Not supported shape type");
        }

        MundusOde4jRuntimePlugin.getPhysicsWorld().getPhysicsComponents().add(physicsComponent);
        return physicsComponent;
    }
}
