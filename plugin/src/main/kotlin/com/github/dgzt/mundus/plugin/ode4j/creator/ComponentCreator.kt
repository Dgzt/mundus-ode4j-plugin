package com.github.dgzt.mundus.plugin.ode4j.creator

import com.badlogic.gdx.utils.OrderedMap
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent
import com.github.dgzt.mundus.plugin.ode4j.constant.SaveConstant
import com.github.dgzt.mundus.plugin.ode4j.type.ShapeType
import com.github.dgzt.mundus.plugin.ode4j.util.GameObjectUtils
import com.mbrlabs.mundus.commons.scene3d.GameObject

object ComponentCreator {

    fun create(gameObject: GameObject): Ode4jPhysicsComponent {
        val shapeType: ShapeType
        if (GameObjectUtils.isTerrainGameObject(gameObject)) {
            shapeType = ShapeType.TERRAIN
        } else {
            shapeType = ShapeType.BOX
        }

        return Ode4jPhysicsComponent(gameObject, shapeType)
    }

    fun create(gameObject: GameObject, config: OrderedMap<String, String>): Ode4jPhysicsComponent {
        val shapeType = ShapeType.valueOf(config.get(SaveConstant.SHAPE))
        return Ode4jPhysicsComponent(gameObject, shapeType)
    }
}
