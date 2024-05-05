package com.github.dgzt.mundus.plugin.ode4j.physics;

import com.badlogic.gdx.utils.Array;
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent;

public interface UpdateCallback {

    void update(Array<Ode4jPhysicsComponent> physicsComponents);

}
