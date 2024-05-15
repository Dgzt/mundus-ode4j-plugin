package com.github.dgzt.mundus.plugin.ode4j.converter;

import com.badlogic.gdx.utils.OrderedMap;
import com.github.antzGames.gdx.ode4j.math.DVector3C;
import com.github.antzGames.gdx.ode4j.ode.DBox;
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
            case BOX: {
                final boolean geomStatic = Boolean.parseBoolean(orderedMap.get(SaveConstants.BOX_STATIC));
                final double geomWidth = Double.parseDouble(orderedMap.get(SaveConstants.BOX_WIDTH));
                final double geomHeight = Double.parseDouble(orderedMap.get(SaveConstants.BOX_HEIGHT));
                final double geomDepth = Double.parseDouble(orderedMap.get(SaveConstants.BOX_DEPTH));
                physicsComponent = Ode4jPhysicsComponentUtils.createBoxPhysicsComponent(gameObject, geomStatic, geomWidth, geomHeight, geomDepth);
                break;
            }
            default: throw new RuntimeException("Not supported shape type");
        }

        MundusOde4jRuntimePlugin.getPhysicsWorld().getPhysicsComponents().add(physicsComponent);
        return physicsComponent;
    }
}
