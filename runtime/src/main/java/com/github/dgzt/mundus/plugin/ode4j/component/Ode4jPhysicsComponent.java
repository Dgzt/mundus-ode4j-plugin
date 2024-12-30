package com.github.dgzt.mundus.plugin.ode4j.component;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.github.antzGames.gdx.ode4j.ode.DBody;
import com.github.antzGames.gdx.ode4j.ode.DGeom;
import com.github.dgzt.mundus.plugin.ode4j.component.updater.DefaultOde4jPhysicsComponentUpdater;
import com.github.dgzt.mundus.plugin.ode4j.component.updater.Ode4jPhysicsComponentUpdater;
import com.github.dgzt.mundus.plugin.ode4j.type.ShapeType;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;

public class Ode4jPhysicsComponent extends AbstractOde4jPhysicsComponent {

    private ShapeType shapeType;
    private DGeom geom;
    // The body can be null if it is a static game object
    private DBody body;
    // The game object and debug instance updater
    private Ode4jPhysicsComponentUpdater componentUpdater;
    // The debugInstance can be null. The DebugRenderer will create if necessary
    private ModelInstance debugInstance;
    // User-defined data. It can be null
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
        componentUpdater = new DefaultOde4jPhysicsComponentUpdater(this);
    }

    @Override
    public void update() {
        if (body != null) {
            componentUpdater.update();
        }
    }

    @Override
    public void debugRender(ModelBatch modelBatch) {
        if (debugInstance != null) {
            modelBatch.render(debugInstance);
        }
    }

    @Override
    public Component clone(final GameObject gameObject) {
        final Ode4jPhysicsComponent clonedComponent = new Ode4jPhysicsComponent(gameObject, shapeType, geom);
        clonedComponent.type = type;
        // TODO other properties
        return clonedComponent;
    }

    public ShapeType getShapeType() {
        return shapeType;
    }

    public void setShapeType(final ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public DGeom getGeom() {
        return geom;
    }

    public void setGeom(final DGeom geom) {
        this.geom = geom;
    }

    public DBody getBody() {
        return body;
    }

    public void setBody(final DBody body) {
        this.body = body;
    }

    public Ode4jPhysicsComponentUpdater getComponentUpdater() {
        return componentUpdater;
    }

    public void setComponentUpdater(final Ode4jPhysicsComponentUpdater componentUpdater) {
        this.componentUpdater = componentUpdater;
    }

    public ModelInstance getDebugInstance() {
        return debugInstance;
    }

    public void setDebugInstance(final ModelInstance debugInstance) {
        this.debugInstance = debugInstance;
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
