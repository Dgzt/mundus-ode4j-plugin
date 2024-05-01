package com.github.dgzt.mundus.plugin.ode4j.creator

import com.badlogic.gdx.utils.Array
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent
import com.github.dgzt.mundus.plugin.ode4j.util.GameObjectUtils
import com.mbrlabs.mundus.commons.scene3d.components.Component
import com.mbrlabs.mundus.commons.scene3d.components.ModelComponent
import com.mbrlabs.mundus.pluginapi.ui.RootWidget

object ComponentWidgetCreator {

    fun setup(component: Ode4jPhysicsComponent, rootWidget: RootWidget) {
        if (GameObjectUtils.isModelGameObject(component.gameObject)) {
            val modelComponent = component.gameObject.findComponentByType<ModelComponent>(Component.Type.MODEL)
            setupModelComponentWidget(modelComponent, rootWidget)
        }
    }

    private fun setupModelComponentWidget(modelComponent: ModelComponent, rootWidget: RootWidget) {
        val boundingBox = modelComponent.orientedBoundingBox.bounds

        val types = Array<String>()
        types.add("Box")
        rootWidget.addSelectBox(types) {
            // NOOP
        }
        rootWidget.addRow()
        rootWidget.addLabel("Size:")
        rootWidget.addRow()
        rootWidget.addSpinner("Width", 0.1f, Float.MAX_VALUE, boundingBox.width, 0.1f) {
            // NOOP
        }
        rootWidget.addSpinner("Height", 0.1f, Float.MAX_VALUE, boundingBox.height, 0.1f) {
            // NOOP
        }
        rootWidget.addSpinner("Depth", 0.1f, Float.MAX_VALUE, boundingBox.depth, 0.1f) {
            // NOOP
        }
    }
}
