package com.github.dgzt.mundus.plugin.ode4j.physics;

import com.badlogic.gdx.utils.Array;
import com.github.dgzt.mundus.plugin.ode4j.component.AbstractOde4jPhysicsComponent;

public interface UpdateCallback {

    void update(Array<AbstractOde4jPhysicsComponent> physicsComponents);

}
