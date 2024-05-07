package com.github.dgzt.mundus.plugin.ode4j.creator

import com.github.antzGames.gdx.ode4j.ode.DGeom
import com.github.dgzt.mundus.plugin.ode4j.MundusOde4jRuntimePlugin
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent
import com.github.dgzt.mundus.plugin.ode4j.type.ShapeType
import com.github.dgzt.mundus.plugin.ode4j.util.GameObjectUtils
import com.mbrlabs.mundus.commons.scene3d.GameObject

object ComponentCreator {

    fun create(gameObject: GameObject): Ode4jPhysicsComponent {
        val shapeType: ShapeType
        var geom: DGeom? = null
        if (GameObjectUtils.isTerrainGameObject(gameObject)) {
            shapeType = ShapeType.TERRAIN
        } else {
            shapeType = ShapeType.BOX
            val modelComponent = GameObjectUtils.getModelComponent(gameObject)
            val bounds = modelComponent.orientedBoundingBox.bounds
            geom = MundusOde4jRuntimePlugin.getPhysicsWorld().createBox(bounds.width, bounds.height, bounds.depth)
        }

        val physicsComponent = Ode4jPhysicsComponent(gameObject, shapeType, geom)
        MundusOde4jRuntimePlugin.getPhysicsWorld().physicsComponents.add(physicsComponent)

        return physicsComponent
    }
}
