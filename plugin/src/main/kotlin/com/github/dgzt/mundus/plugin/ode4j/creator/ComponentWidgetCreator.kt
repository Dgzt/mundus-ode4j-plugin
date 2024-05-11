package com.github.dgzt.mundus.plugin.ode4j.creator

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.github.antzGames.gdx.ode4j.math.DVector3
import com.github.antzGames.gdx.ode4j.ode.DBox
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent
import com.github.dgzt.mundus.plugin.ode4j.debug.DebugModelBuilder
import com.github.dgzt.mundus.plugin.ode4j.type.ShapeType
import com.github.dgzt.mundus.plugin.ode4j.util.GameObjectUtils
import com.mbrlabs.mundus.pluginapi.ui.RootWidget
import com.mbrlabs.mundus.pluginapi.ui.RootWidgetCell

object ComponentWidgetCreator {

    private val TMP_DVECTOR3 = DVector3()
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
        val boxGeom = component.geom as DBox
        boxGeom.getLengths(TMP_DVECTOR3)

        rootWidget.addLabel("Size:")
        rootWidget.addRow()
        rootWidget.addSpinner("Width", 0.1f, Float.MAX_VALUE, TMP_DVECTOR3.get0().toFloat(), 0.1f) {
            val currentLength = boxGeom.lengths
            boxGeom.setLengths(it.toDouble(), currentLength.get1(), currentLength.get2())
            updateDebugInstanceIfNecessary(component, boxGeom)
        }
        rootWidget.addSpinner("Height", 0.1f, Float.MAX_VALUE, TMP_DVECTOR3.get1().toFloat(), 0.1f) {
            val currentLength = boxGeom.lengths
            boxGeom.setLengths(currentLength.get0(), it.toDouble(), currentLength.get2())
            updateDebugInstanceIfNecessary(component, boxGeom)
        }
        rootWidget.addSpinner("Depth", 0.1f, Float.MAX_VALUE, TMP_DVECTOR3.get2().toFloat(), 0.1f) {
            val currentLength = boxGeom.lengths
            boxGeom.setLengths(currentLength.get0(), currentLength.get1(), it.toDouble())
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
