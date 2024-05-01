package com.github.dgzt.mundus.plugin.ode4j.component;

import com.github.dgzt.mundus.plugin.ode4j.type.ShapeType;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.AbstractComponent;
import com.mbrlabs.mundus.commons.scene3d.components.Component;

public class Ode4jPhysicsComponent extends AbstractComponent {

    private final ShapeType shapeType;

    public Ode4jPhysicsComponent(final GameObject go, final ShapeType shapeType) {
        super(go);

        setType(Type.PHYSICS);
        this.shapeType = shapeType;
    }

    @Override
    public void update(float delta) {
        // NOOP
    }

    @Override
    public Component clone(final GameObject gameObject) {
        final Ode4jPhysicsComponent clonedComponent = new Ode4jPhysicsComponent(gameObject, shapeType);
        clonedComponent.type = type;
        return clonedComponent;
    }

    public ShapeType getShapeType() {
        return shapeType;
    }
}
