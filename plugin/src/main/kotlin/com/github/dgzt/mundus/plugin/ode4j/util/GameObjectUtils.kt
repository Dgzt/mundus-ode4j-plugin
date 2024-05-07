package com.github.dgzt.mundus.plugin.ode4j.util

import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.commons.scene3d.components.Component
import com.mbrlabs.mundus.commons.scene3d.components.ModelComponent

object GameObjectUtils {

    fun isTerrainGameObject(gameObject: GameObject): Boolean {
        return gameObject.findComponentByType<ModelComponent>(Component.Type.TERRAIN) != null
    }

    fun isModelGameObject(gameObject: GameObject): Boolean {
        return gameObject.findComponentByType<ModelComponent>(Component.Type.MODEL) != null
    }

    fun getModelComponent(gameObject: GameObject): ModelComponent {
        return gameObject.findComponentByType(Component.Type.MODEL)
    }
}
