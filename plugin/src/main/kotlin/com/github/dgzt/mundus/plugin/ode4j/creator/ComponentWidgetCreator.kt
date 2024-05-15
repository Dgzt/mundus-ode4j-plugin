package com.github.dgzt.mundus.plugin.ode4j.creator

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.github.antzGames.gdx.ode4j.math.DVector3
import com.github.antzGames.gdx.ode4j.ode.DBox
import com.github.antzGames.gdx.ode4j.ode.OdeHelper
import com.github.dgzt.mundus.plugin.ode4j.MundusOde4jRuntimePlugin
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent
import com.github.dgzt.mundus.plugin.ode4j.debug.DebugModelBuilder
import com.github.dgzt.mundus.plugin.ode4j.type.ShapeType
import com.github.dgzt.mundus.plugin.ode4j.util.GameObjectUtils
import com.mbrlabs.mundus.pluginapi.ui.RootWidget
import com.mbrlabs.mundus.pluginapi.ui.RootWidgetCell

object ComponentWidgetCreator {

    private val TMP_VECTOR3 = Vector3()

    private const val BOX = "Box"
    private const val CYLINDER = "Cylinder"

    fun setup(component: Ode4jPhysicsComponent, rootWidget: RootWidget) {
        if (GameObjectUtils.isModelGameObject(component.gameObject)) {
            setupModelComponentWidget(component, rootWidget)
        }
    }

    private fun setupModelComponentWidget(component: Ode4jPhysicsComponent, rootWidget: RootWidget) {
        var innerWidgetCell: RootWidgetCell? = null

        val types = Array<String>()
        types.add(BOX)
//        types.add(CYLINDER)
        rootWidget.addSelectBox(types, getSelectBoxType(component)) {
            innerWidgetCell!!.rootWidget!!.clearWidgets()

            if (it.equals(BOX)) {
                addBoxWidgets(component, innerWidgetCell!!.rootWidget)
            }
        }
        rootWidget.addRow()

        innerWidgetCell = rootWidget.addEmptyWidget()

        if (ShapeType.BOX == component.shapeType) {
            addBoxWidgets(component, innerWidgetCell.rootWidget)
        }
    }

    private fun getSelectBoxType(component: Ode4jPhysicsComponent): String {
        // TODO support other types also
        return BOX
    }

    private fun addBoxWidgets(component: Ode4jPhysicsComponent, rootWidget: RootWidget) {
        var boxGeom = component.geom as DBox
        val length = DVector3()
        boxGeom.getLengths(length)

        var static = component.geom.body == null

        rootWidget.addCheckbox("Static", static) {
            boxGeom.getLengths(length)
            component.body?.destroy()
            component.body = null
            component.geom.destroy()
            component.geom = null

            boxGeom = MundusOde4jRuntimePlugin.getPhysicsWorld().createBox(length.get0(), length.get1(), length.get2())
            component.geom = boxGeom

            val goPosition = component.gameObject.getPosition(Vector3())
            static = it
            if (static) {
                component.geom.setPosition(goPosition.x.toDouble(), goPosition.y.toDouble(), goPosition.z.toDouble())
            } else {
                val body = MundusOde4jRuntimePlugin.getPhysicsWorld().createBody()
                body.setPosition(goPosition.x.toDouble(), goPosition.y.toDouble(), goPosition.z.toDouble())

                // TODO set mass info from UI
                val massInfo = OdeHelper.createMass()
                massInfo.setBox(1.0, length)
                massInfo.adjust(10.0)
                body.mass = massInfo
                body.setAutoDisableDefaults()

                boxGeom.body = body
                component.body = body
            }
        }
        rootWidget.addRow()
        rootWidget.addLabel("Size:")
        rootWidget.addRow()
        rootWidget.addSpinner("Width", 0.1f, Float.MAX_VALUE, length.get0().toFloat(), 0.1f) {
            length.set0(it.toDouble())
            boxGeom.lengths = length
            updateDebugInstanceIfNecessary(component, boxGeom)
        }
        rootWidget.addSpinner("Height", 0.1f, Float.MAX_VALUE, length.get1().toFloat(), 0.1f) {
            length.set1(it.toDouble())
            boxGeom.lengths = length
            updateDebugInstanceIfNecessary(component, boxGeom)
        }
        rootWidget.addSpinner("Depth", 0.1f, Float.MAX_VALUE, length.get2().toFloat(), 0.1f) {
            length.set2(it.toDouble())
            boxGeom.lengths = length
            updateDebugInstanceIfNecessary(component, boxGeom)
        }
    }

    private fun updateDebugInstanceIfNecessary(component: Ode4jPhysicsComponent, geom: DBox) {
        if (component.debugInstance == null) return

        var debugInstance = component.debugInstance
        debugInstance.model.dispose()

        val lengths = geom.lengths
        debugInstance = DebugModelBuilder.createBox(lengths.get0().toFloat(), lengths.get1().toFloat(), lengths.get2().toFloat())
        debugInstance.transform.setTranslation(component.gameObject.getPosition(TMP_VECTOR3))
        component.debugInstance = debugInstance
    }
}
