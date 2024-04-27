package com.github.dgzt.mundus.plugin.ode4j

import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent
import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.commons.scene3d.components.Component
import com.mbrlabs.mundus.pluginapi.ComponentExtension
import com.mbrlabs.mundus.pluginapi.MenuExtension
import com.mbrlabs.mundus.pluginapi.ui.RootWidget
import org.pf4j.Extension
import org.pf4j.Plugin

class MundusOde4jPlugin : Plugin() {

    @Extension
    class YourMenuExtension : MenuExtension {

        companion object {
            const val PAD = 5f
        }

        override fun getMenuName(): String = "Ode4j Physics"

        override fun setupDialogRootWidget(root: RootWidget) {
            root.addLabel("").setPad(PAD, PAD, PAD, PAD)
        }

    }

    @Extension
    class Ode4jComponentExtension : ComponentExtension {
        override fun getComponentName(): String = "Ode4j Physics"

        override fun createComponent(gameObject: GameObject): Component = Ode4jPhysicsComponent(gameObject)
    }

}