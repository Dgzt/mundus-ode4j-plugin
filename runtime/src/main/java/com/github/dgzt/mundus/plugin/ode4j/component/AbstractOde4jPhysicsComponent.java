package com.github.dgzt.mundus.plugin.ode4j.component;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.AbstractComponent;

public abstract class AbstractOde4jPhysicsComponent extends AbstractComponent {

    public AbstractOde4jPhysicsComponent(final GameObject go) {
        super(go);

        setType(Type.PHYSICS);
    }

    public abstract void update();

    public abstract void debugRender(ModelBatch modelBatch);

    @Override
    public void update(float delta) {
        // NOOP
    }

}
