package com.github.dgzt.mundus.plugin.ode4j.util

import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent
import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.commons.scene3d.components.Component
import com.mbrlabs.mundus.commons.scene3d.components.ModelComponent
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent
import com.mbrlabs.mundus.commons.scene3d.components.TerrainManagerComponent

object GameObjectUtils {

    fun isTerrainManagerGameObject(gameObject: GameObject): Boolean {
        return gameObject.findComponentByType<TerrainManagerComponent>(Component.Type.TERRAIN_MANAGER) != null
    }

    fun isTerrainGameObject(gameObject: GameObject): Boolean {
        return gameObject.findComponentByType<ModelComponent>(Component.Type.TERRAIN) != null
    }

    fun isModelGameObject(gameObject: GameObject): Boolean {
        return gameObject.findComponentByType<ModelComponent>(Component.Type.MODEL) != null
    }

    fun hasPhysicsComponent(gameObject: GameObject): Boolean {
        return gameObject.findComponentByType<Ode4jPhysicsComponent>(Component.Type.PHYSICS) != null
    }

    fun getTerrainComponent(gameObject: GameObject): TerrainComponent {
        return gameObject.findComponentByType(Component.Type.TERRAIN)
    }

    fun getModelComponent(gameObject: GameObject): ModelComponent {
        return gameObject.findComponentByType(Component.Type.MODEL)
    }

    fun getPhysicsComponent(gameObject: GameObject): Ode4jPhysicsComponent {
        return gameObject.findComponentByType(Component.Type.PHYSICS)
    }
}
