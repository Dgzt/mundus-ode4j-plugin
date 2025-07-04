package com.github.dgzt.mundus.plugin.ode4j.creator

import com.github.dgzt.mundus.plugin.ode4j.MundusOde4jRuntimePlugin
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent
import com.github.dgzt.mundus.plugin.ode4j.util.GameObjectUtils
import com.github.dgzt.mundus.plugin.ode4j.util.Ode4jPhysicsComponentUtils
import com.mbrlabs.mundus.commons.scene3d.GameObject

object ComponentCreator {

    fun create(gameObject: GameObject): Ode4jPhysicsComponent {
        val physicsComponent: Ode4jPhysicsComponent
        if (GameObjectUtils.isTerrainManagerGameObject(gameObject)) {
            physicsComponent = Ode4jPhysicsComponentUtils.createTerrainSystemPhysicsComponent(gameObject)

            if (!GameObjectUtils.hasPhysicsComponent(gameObject)) {
                for (childGameObject in gameObject.children) {
                    if (GameObjectUtils.isTerrainGameObject(childGameObject) && !GameObjectUtils.hasPhysicsComponent(childGameObject)) {
                        childGameObject.addComponent(Ode4jPhysicsComponentUtils.createTerrainPhysicsComponent(childGameObject))
                    }
                }
            }
        } else if (GameObjectUtils.isTerrainGameObject(gameObject)) {
            physicsComponent = Ode4jPhysicsComponentUtils.createTerrainPhysicsComponent(gameObject)
        } else if (GameObjectUtils.isModelGameObject(gameObject)) {
            physicsComponent = Ode4jPhysicsComponentUtils.createBoxPhysicsComponent(gameObject)
        } else {
            throw UnsupportedOperationException("Not supported game object type!")
        }

        MundusOde4jRuntimePlugin.getPhysicsWorld().physicsComponents.add(physicsComponent)
        return physicsComponent
    }
}
