package com.github.dgzt.mundus.plugin.ode4j

import com.badlogic.gdx.utils.Array
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent
import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.commons.scene3d.components.Component
import com.mbrlabs.mundus.commons.scene3d.components.ModelComponent
import com.mbrlabs.mundus.pluginapi.ui.RootWidget

object ComponentWidgetCreator {

    fun setup(component: Ode4jPhysicsComponent, rootWidget: RootWidget) {
        if (isModelGameObject(component.gameObject)) {
            setupModelComponentWidget(rootWidget)
        }
    }

    private fun setupModelComponentWidget(rootWidget: RootWidget) {
        val types = Array<String>()
        types.add("Box")
        rootWidget.addSelectBox(types) {
            // NOOP
        }
    }

    private fun isModelGameObject(gameObject: GameObject): Boolean {
        return gameObject.findComponentByType<ModelComponent>(Component.Type.MODEL) != null
    }
}
