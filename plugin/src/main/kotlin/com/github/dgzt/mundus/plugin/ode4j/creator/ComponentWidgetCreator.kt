package com.github.dgzt.mundus.plugin.ode4j.creator

import com.badlogic.gdx.utils.Array
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent
import com.github.dgzt.mundus.plugin.ode4j.util.GameObjectUtils
import com.mbrlabs.mundus.pluginapi.ui.RootWidget

object ComponentWidgetCreator {

    fun setup(component: Ode4jPhysicsComponent, rootWidget: RootWidget) {
        if (GameObjectUtils.isModelGameObject(component.gameObject)) {
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
}
