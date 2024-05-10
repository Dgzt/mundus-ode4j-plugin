package com.github.dgzt.mundus.plugin.ode4j.creator

import com.badlogic.gdx.math.Vector3
import com.github.antzGames.gdx.ode4j.ode.DGeom
import com.github.dgzt.mundus.plugin.ode4j.MundusOde4jRuntimePlugin
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent
import com.github.dgzt.mundus.plugin.ode4j.type.ShapeType
import com.github.dgzt.mundus.plugin.ode4j.util.GameObjectUtils
import com.mbrlabs.mundus.commons.scene3d.GameObject

object ComponentCreator {

    private val goPosition = Vector3()
    private val goScale = Vector3()

    fun create(gameObject: GameObject): Ode4jPhysicsComponent {
        gameObject.getPosition(goPosition)
        gameObject.getScale(goScale)

        val shapeType: ShapeType
        var geom: DGeom? = null
        if (GameObjectUtils.isTerrainGameObject(gameObject)) {
            shapeType = ShapeType.TERRAIN
        } else {
            shapeType = ShapeType.BOX
            val modelComponent = GameObjectUtils.getModelComponent(gameObject)
            val bounds = modelComponent.orientedBoundingBox.bounds
            val geomWidth = bounds.width * goScale.x
            val geomHeight = bounds.height * goScale.y
            val geomDepth = bounds.depth * goScale.z

            geom = MundusOde4jRuntimePlugin.getPhysicsWorld().createBox(geomWidth, geomHeight, geomDepth)
            geom.setPosition(goPosition.x.toDouble(), goPosition.y.toDouble(), goPosition.z.toDouble())
        }

        val physicsComponent = Ode4jPhysicsComponent(gameObject, shapeType, geom)
        MundusOde4jRuntimePlugin.getPhysicsWorld().physicsComponents.add(physicsComponent)

        return physicsComponent
    }
}
