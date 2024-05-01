package com.github.dgzt.mundus.plugin.ode4j

import com.badlogic.gdx.utils.OrderedMap
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent
import com.github.dgzt.mundus.plugin.ode4j.constant.SaveConstant
import com.github.dgzt.mundus.plugin.ode4j.creator.ComponentCreator
import com.github.dgzt.mundus.plugin.ode4j.creator.ComponentWidgetCreator
import com.github.dgzt.mundus.plugin.ode4j.manager.Ode4jManager
import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.commons.scene3d.components.Component
import com.mbrlabs.mundus.pluginapi.ComponentExtension
import com.mbrlabs.mundus.pluginapi.DisposeExtension
import com.mbrlabs.mundus.pluginapi.MenuExtension
import com.mbrlabs.mundus.pluginapi.ui.RootWidget
import org.pf4j.Extension
import org.pf4j.Plugin

class MundusOde4jPlugin : Plugin() {

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

        override fun getComponentConfig(component: Component): OrderedMap<String, String>? {
            if (component is Ode4jPhysicsComponent) {
                val map = OrderedMap<String, String>()
                map.put(SaveConstant.SHAPE, component.shapeType.name)

                return map
            } else {
                return null
            }
        }

        override fun loadComponentConfig(gameObject: GameObject, config: OrderedMap<String, String>): Component =
            ComponentCreator.create(gameObject, config)
    }

    @Extension
    class Ode4jDisposeExtension : DisposeExtension {
        override fun dispose() {
            Ode4jManager.physicsWorld.dispose()
        }
    }

}
