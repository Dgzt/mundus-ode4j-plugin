package com.github.dgzt.mundus.plugin.ode4j.component

import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.commons.scene3d.components.AbstractComponent
import com.mbrlabs.mundus.commons.scene3d.components.Component

class Ode4jPhysicsComponent(gameObject: GameObject) : AbstractComponent(gameObject) {

    init {
        type = Component.Type.PHYSICS
    }

    override fun update(delta: Float) {
        // NOOP
    }

    override fun clone(go: GameObject): Component {
        return Ode4jPhysicsComponent(go)
    }
}
