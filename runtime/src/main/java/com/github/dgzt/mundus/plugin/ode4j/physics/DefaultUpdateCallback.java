package com.github.dgzt.mundus.plugin.ode4j.physics;

import com.badlogic.gdx.utils.Array;
import com.github.dgzt.mundus.plugin.ode4j.component.AbstractOde4jPhysicsComponent;

public class DefaultUpdateCallback implements UpdateCallback {

    @Override
    public void update(final Array<AbstractOde4jPhysicsComponent> physicsComponents) {
        for (int i = 0; i < physicsComponents.size; ++i) {
            physicsComponents.get(i).update();
        }
    }

}
