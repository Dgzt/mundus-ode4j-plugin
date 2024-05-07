package com.github.dgzt.mundus.plugin.ode4j.component;

import com.github.antzGames.gdx.ode4j.ode.DBody;
import com.github.antzGames.gdx.ode4j.ode.DGeom;
import com.github.dgzt.mundus.plugin.ode4j.type.ShapeType;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.AbstractComponent;
import com.mbrlabs.mundus.commons.scene3d.components.Component;

public class Ode4jPhysicsComponent extends AbstractComponent {

    private final ShapeType shapeType;
    private final DGeom geom;
    private DBody body;
    /* User-defined data */
    private Object data;

    public Ode4jPhysicsComponent(
            final GameObject go,
            final ShapeType shapeType,
            final DGeom geom
    ) {
        super(go);

        setType(Type.PHYSICS);
        this.shapeType = shapeType;
        this.geom = geom;
        body = null;
    }

    @Override
    public void update(float delta) {
        // NOOP
    }

    @Override
    public Component clone(final GameObject gameObject) {
        final Ode4jPhysicsComponent clonedComponent = new Ode4jPhysicsComponent(gameObject, shapeType, geom);
        clonedComponent.type = type;
        return clonedComponent;
    }

    public ShapeType getShapeType() {
        return shapeType;
    }

    public DGeom getGeom() {
        return geom;
    }

    public DBody getBody() {
        return body;
    }

    public void setBody(final DBody body) {
        this.body = body;
    }

    /**
     * @return User-defined data.
     */
    public Object getData() {
        return data;
    }

    /**
     * Set the user-defined data.
     * @param data The data.
     */
    public void setData(final Object data) {
        this.data = data;
    }
}
