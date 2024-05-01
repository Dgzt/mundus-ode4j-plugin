package com.github.dgzt.mundus.plugin.ode4j.component

import com.github.dgzt.mundus.plugin.ode4j.type.ShapeType
import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.commons.scene3d.components.AbstractComponent
import com.mbrlabs.mundus.commons.scene3d.components.Component

class Ode4jPhysicsComponent(
    gameObject: GameObject,
    var shapeType: ShapeType
) : AbstractComponent(gameObject) {

    init {
        type = Component.Type.PHYSICS
    }

    override fun update(delta: Float) {
        // NOOP
    }

    override fun clone(go: GameObject): Component {
        val clonedComponent = Ode4jPhysicsComponent(go, shapeType)
        clonedComponent.type = type
        return clonedComponent
    }
}
