package com.github.dgzt.mundus.plugin.ode4j.converter;

import com.badlogic.gdx.utils.OrderedMap;
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent;
import com.github.dgzt.mundus.plugin.ode4j.constant.SaveConstants;
import com.github.dgzt.mundus.plugin.ode4j.type.ShapeType;
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

        return map;
    }

    @Override
    public Component convert(final GameObject gameObject, OrderedMap<String, String> orderedMap) {
        final ShapeType shapeType = ShapeType.valueOf(orderedMap.get(SaveConstants.SHAPE));

        return new Ode4jPhysicsComponent(gameObject, shapeType, null); // TODO Fix null value
    }
}
