package com.github.dgzt.mundus.plugin.ode4j.creator

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.github.antzGames.gdx.ode4j.math.DVector3
import com.github.antzGames.gdx.ode4j.ode.DBox
import com.github.antzGames.gdx.ode4j.ode.DCylinder
import com.github.antzGames.gdx.ode4j.ode.DSphere
import com.github.antzGames.gdx.ode4j.ode.OdeHelper
import com.github.dgzt.mundus.plugin.ode4j.MundusOde4jRuntimePlugin
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent
import com.github.dgzt.mundus.plugin.ode4j.debug.DebugModelBuilder
import com.github.dgzt.mundus.plugin.ode4j.type.ShapeType
import com.github.dgzt.mundus.plugin.ode4j.util.GameObjectUtils
import com.mbrlabs.mundus.commons.scene3d.components.Component
import com.mbrlabs.mundus.commons.scene3d.components.ModelComponent
import com.mbrlabs.mundus.pluginapi.ui.RootWidget
import com.mbrlabs.mundus.pluginapi.ui.RootWidgetCell

object ComponentWidgetCreator {

    private val TMP_VECTOR3 = Vector3()

    private const val BOX = "Box"
    private const val SPHERE = "Sphere"
    // TODO capsule
    private const val CYLINDER = "Cylinder"
    // TODO mesh

    fun setup(component: Ode4jPhysicsComponent, rootWidget: RootWidget) {
        if (GameObjectUtils.isModelGameObject(component.gameObject)) {
            setupModelComponentWidget(component, rootWidget)
        }
    }

    private fun setupModelComponentWidget(component: Ode4jPhysicsComponent, rootWidget: RootWidget) {
        val modelComponent = component.gameObject.findComponentByType<ModelComponent>(Component.Type.MODEL)
        var innerWidgetCell: RootWidgetCell? = null

        val types = Array<String>()
        types.add(BOX)
        types.add(SPHERE)
        types.add(CYLINDER)
        rootWidget.addSelectBox(types, getSelectBoxType(component)) {
            innerWidgetCell!!.rootWidget!!.clearWidgets()

            destroyBody(component)
            destroyGeom(component)
            destroyDebugInstance(component)

            if (it.equals(BOX)) {
                changeToBox(component, modelComponent, innerWidgetCell!!)
            }

            if (it.equals(SPHERE)) {
                changeToSphere(component, modelComponent, innerWidgetCell!!)
            }

            if (it.equals(CYLINDER)) {
                changeToCylinder(component, modelComponent, innerWidgetCell!!)
            }
        }
        rootWidget.addRow()

        innerWidgetCell = rootWidget.addEmptyWidget()

        if (ShapeType.BOX == component.shapeType) {
            addBoxWidgets(component, innerWidgetCell.rootWidget)
        } else if (ShapeType.SPHERE == component.shapeType) {
            addSphereWidgets(component, innerWidgetCell.rootWidget)
        } else if (ShapeType.CYLINDER == component.shapeType) {
            addCylinderWidgets(component, innerWidgetCell.rootWidget)
        }
    }

    private fun getSelectBoxType(component: Ode4jPhysicsComponent): String {
        return when(component.shapeType) {
            ShapeType.BOX -> return BOX
            ShapeType.CYLINDER -> return CYLINDER
            else -> throw RuntimeException("Not suppoted shape type!")
        }
    }

    private fun changeToBox(
        component: Ode4jPhysicsComponent,
        modelComponent: ModelComponent,
        innerWidgetCell: RootWidgetCell
    ) {
        val physicsWorld = MundusOde4jRuntimePlugin.getPhysicsWorld()

        component.shapeType = ShapeType.BOX

        // Create static box geom
        val goScale = modelComponent.gameObject.getScale(Vector3())
        val goPosition = modelComponent.gameObject.getPosition(Vector3())
        val boundingBox = modelComponent.orientedBoundingBox.bounds
        val geomWidth = boundingBox.width * goScale.x
        val geomHeight = boundingBox.height * goScale.y
        val geomDepth = boundingBox.depth * goScale.z
        component.geom = physicsWorld.createBox(geomWidth.toDouble(), geomHeight.toDouble(), geomDepth.toDouble())
        component.geom.setPosition(goPosition.x.toDouble(), goPosition.y.toDouble(), goPosition.z.toDouble())

        addBoxWidgets(component, innerWidgetCell.rootWidget)
    }

    private fun changeToSphere(
        component: Ode4jPhysicsComponent,
        modelComponent: ModelComponent,
        innerWidgetCell: RootWidgetCell
    ) {
        val physicsWorld = MundusOde4jRuntimePlugin.getPhysicsWorld()

        component.shapeType = ShapeType.SPHERE

        // Create static sphere box geom
        val goScale = modelComponent.gameObject.getScale(Vector3())
        val goPosition = modelComponent.gameObject.getPosition(Vector3())
        val boundingBox = modelComponent.orientedBoundingBox.bounds
        val geomRadius = Math.max(Math.max(boundingBox.width * goScale.x, boundingBox.depth * goScale.z), boundingBox.height * goScale.y) / 2.0
        component.geom = physicsWorld.createSphere(geomRadius)
        component.geom.setPosition(goPosition.x.toDouble(), goPosition.y.toDouble(), goPosition.z.toDouble())

        addSphereWidgets(component, innerWidgetCell.rootWidget)
    }

    private fun changeToCylinder(
        component: Ode4jPhysicsComponent,
        modelComponent: ModelComponent,
        innerWidgetCell: RootWidgetCell
    ) {
        val physicsWorld = MundusOde4jRuntimePlugin.getPhysicsWorld()

        component.shapeType = ShapeType.CYLINDER

        // Create static cylinder geom
        val goScale = modelComponent.gameObject.getScale(Vector3())
        val goPosition = modelComponent.gameObject.getPosition(Vector3())
        val boundingBox = modelComponent.orientedBoundingBox.bounds
        val radius = Math.max(boundingBox.width * goScale.x, boundingBox.depth * goScale.z) / 2.0
        val height = boundingBox.height * goScale.y
        component.geom = physicsWorld.createCylinder(radius, height.toDouble())
        component.geom.setPosition(goPosition.x.toDouble(), goPosition.y.toDouble(), goPosition.z.toDouble())

        addCylinderWidgets(component, innerWidgetCell.rootWidget)
    }

    private fun addBoxWidgets(component: Ode4jPhysicsComponent, rootWidget: RootWidget) {
        var boxGeom = component.geom as DBox
        val length = DVector3()
        boxGeom.getLengths(length)

        var static = component.geom.body == null

        rootWidget.addCheckbox("Static", static) {
            boxGeom.getLengths(length)
            destroyBody(component)
            destroyGeom(component)
            destroyDebugInstance(component)

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

    private fun addSphereWidgets(component: Ode4jPhysicsComponent, rootWidget: RootWidget) {
        var sphereGeom = component.geom as DSphere
        var statis = component.geom.body == null

        rootWidget.addCheckbox("Static", statis) {
            // TODO
        }
        rootWidget.addRow()
        rootWidget.addLabel("Size:")
        rootWidget.addRow()
        rootWidget.addSpinner("Radius", 0.1f, Float.MAX_VALUE, sphereGeom.radius.toFloat(), 0.1f) {
            sphereGeom.radius = it.toDouble()
            updateDebugInstanceIfNecessary(component, sphereGeom)
        }
    }

    private fun addCylinderWidgets(component: Ode4jPhysicsComponent, rootWidget: RootWidget) {
        var cylinderGeom = component.geom as DCylinder
        var static = component.geom.body == null

        rootWidget.addCheckbox("Static", static) {
            // TODO
        }
        rootWidget.addRow()
        rootWidget.addLabel("Size:")
        rootWidget.addRow()
        rootWidget.addSpinner("Radius", 0.1f, Float.MAX_VALUE, cylinderGeom.radius.toFloat(), 0.1f) {
            cylinderGeom.setParams(it.toDouble(), cylinderGeom.length)
            updateDebugInstanceIfNecessary(component, cylinderGeom)
        }
        rootWidget.addSpinner("Height", 0.1f, Float.MAX_VALUE, cylinderGeom.length.toFloat(), 0.1f) {
            cylinderGeom.setParams(cylinderGeom.radius, it.toDouble())
            updateDebugInstanceIfNecessary(component, cylinderGeom)
        }
    }

    private fun destroyBody(physicsComponent: Ode4jPhysicsComponent) {
        if (physicsComponent.body == null) return

        physicsComponent.body.destroy()
        physicsComponent.body = null
    }

    private fun destroyGeom(physicsComponent: Ode4jPhysicsComponent) {
        if (physicsComponent.geom == null) return

        physicsComponent.geom.destroy()
        physicsComponent.geom = null
    }

    private fun destroyDebugInstance(physicsComponent: Ode4jPhysicsComponent) {
        if (physicsComponent.debugInstance == null) return

        physicsComponent.debugInstance.model.dispose()
        physicsComponent.debugInstance = null
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

    private fun updateDebugInstanceIfNecessary(component: Ode4jPhysicsComponent, geom: DSphere) {
        if (component.debugInstance == null) return

        var debugInstance = component.debugInstance
        debugInstance.model.dispose()

        val radius = geom.radius
        debugInstance = DebugModelBuilder.createSphere(radius.toFloat())
        debugInstance.transform.setTranslation(component.gameObject.getPosition(TMP_VECTOR3))
        component.debugInstance = debugInstance
    }

    private fun updateDebugInstanceIfNecessary(component: Ode4jPhysicsComponent, geom: DCylinder) {
        if (component.debugInstance == null) return

        var debugInstance = component.debugInstance
        debugInstance.model.dispose()

        val radius = geom.radius
        val height = geom.length
        debugInstance = DebugModelBuilder.createCylinder(radius.toFloat(), height.toFloat())
        debugInstance.transform.setTranslation(component.gameObject.getPosition(TMP_VECTOR3))
        component.debugInstance = debugInstance
    }
}
