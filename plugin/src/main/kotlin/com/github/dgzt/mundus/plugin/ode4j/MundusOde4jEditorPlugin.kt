package com.github.dgzt.mundus.plugin.ode4j

import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent
import com.github.dgzt.mundus.plugin.ode4j.converter.Ode4jPhysicsComponentConverter
import com.github.dgzt.mundus.plugin.ode4j.creator.ComponentCreator
import com.github.dgzt.mundus.plugin.ode4j.creator.ComponentWidgetCreator
import com.mbrlabs.mundus.commons.mapper.CustomComponentConverter
import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.commons.scene3d.components.Component
import com.mbrlabs.mundus.pluginapi.ComponentExtension
import com.mbrlabs.mundus.pluginapi.DisposeExtension
import com.mbrlabs.mundus.pluginapi.MenuExtension
import com.mbrlabs.mundus.pluginapi.ui.RootWidget
import org.pf4j.Extension
import org.pf4j.Plugin

class MundusOde4jEditorPlugin : Plugin() {

    @Extension
    class Ode4jMenuExtension : MenuExtension {

        companion object {
            const val PAD = 5f
        }

        override fun getMenuName(): String = "Ode4j Physics"

        override fun setupDialogRootWidget(root: RootWidget) {
            root.addCheckbox("Debug render") {
                PropertyManager.debugRender = it
            }.setPad(PAD, PAD, PAD, PAD)
        }

    }

    @Extension
    class Ode4jComponentExtension : ComponentExtension {
        override fun getComponentType(): Component.Type = Component.Type.PHYSICS

        override fun getComponentName(): String = "Ode4j Physics"

        override fun createComponent(gameObject: GameObject): Component = ComponentCreator.create(gameObject)

        override fun setupComponentInspectorWidget(component: Component, rootWidget: RootWidget) =
            ComponentWidgetCreator.setup(component as Ode4jPhysicsComponent, rootWidget)

        override fun getConverter(): CustomComponentConverter = Ode4jPhysicsComponentConverter()
    }

    @Extension
    class Ode4jDisposeExtension : DisposeExtension {
        override fun dispose() {
            // TODO dispose ode4j
        }
    }

}