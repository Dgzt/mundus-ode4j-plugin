package com.github.dgzt.mundus.plugin.ode4j.creator

import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent
import com.github.dgzt.mundus.plugin.ode4j.type.ShapeType
import com.github.dgzt.mundus.plugin.ode4j.util.GameObjectUtils
import com.mbrlabs.mundus.commons.scene3d.GameObject

object ComponentCreator {

    fun create(gameObject: GameObject): Ode4jPhysicsComponent {
        var shapeType: ShapeType
        if (GameObjectUtils.isTerrainGameObject(gameObject)) {
            shapeType = ShapeType.TERRAIN
        } else {
            shapeType = ShapeType.BOX
        }

        return Ode4jPhysicsComponent(gameObject, shapeType)
    }
}
