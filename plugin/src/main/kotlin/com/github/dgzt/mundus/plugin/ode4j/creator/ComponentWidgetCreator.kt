package com.github.dgzt.mundus.plugin.ode4j.creator

import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.github.dgzt.mundus.plugin.ode4j.MundusOde4jRuntimePlugin
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent
import com.github.dgzt.mundus.plugin.ode4j.debug.DebugModelBuilder
import com.github.dgzt.mundus.plugin.ode4j.physics.ArrayGeomData
import com.github.dgzt.mundus.plugin.ode4j.type.ShapeType
import com.github.dgzt.mundus.plugin.ode4j.util.GameObjectUtils
import com.github.dgzt.mundus.plugin.ode4j.util.MassUtils
import com.github.dgzt.mundus.plugin.ode4j.util.MeshUtils
import com.github.dgzt.mundus.plugin.ode4j.util.OdePhysicsUtils
import com.github.dgzt.mundus.plugin.ode4j.util.Utils3D
import com.mbrlabs.mundus.commons.scene3d.components.Component
import com.mbrlabs.mundus.commons.scene3d.components.ModelComponent
import com.mbrlabs.mundus.pluginapi.ui.Cell
import com.mbrlabs.mundus.pluginapi.ui.RootWidget
import com.mbrlabs.mundus.pluginapi.ui.RootWidgetCell
import com.mbrlabs.mundus.pluginapi.ui.WidgetAlign
import org.ode4j.math.DVector3
import org.ode4j.ode.DBox
import org.ode4j.ode.DCapsule
import org.ode4j.ode.DCylinder
import org.ode4j.ode.DSphere
import org.ode4j.ode.DTriMesh

object ComponentWidgetCreator {

    private const val STATIC_BOTTOM_PAD = 3.0f
    private const val SIZE_RIGHT_PAD = 10.0f
    private const val VERTEX_WIDGET_TOP_BOTTOM_PAD = 1.0f
    private const val VERTEX_WIDGET_DELETE_BUTTON_LEFT_PAD = 2.0f

    private const val DEFAULT_MASS = 10.0

    private val TMP_VECTOR3 = Vector3()

    private const val BOX = "Box"
    private const val SPHERE = "Sphere"
    private const val CYLINDER = "Cylinder"
    private const val CAPSULE = "Capsule"
    private const val MESH = "Mesh"
    private const val ARRAY = "Array"

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
        types.add(CAPSULE)
        // TODO The rotation of mesh is not good, so fix this first
//        types.add(MESH)
        types.add(ARRAY)
        rootWidget.addSelectBox(types, getSelectBoxType(component)) {
            innerWidgetCell!!.rootWidget!!.clearWidgets()

            destroyBody(component)
            destroyGeom(component)
            destroyDebugInstance(component)

            when (it) {
                BOX -> changeToBox(component, modelComponent, innerWidgetCell!!)
                SPHERE -> changeToSphere(component, modelComponent, innerWidgetCell!!)
                CYLINDER -> changeToCylinder(component, modelComponent, innerWidgetCell!!)
                CAPSULE -> changeToCapsule(component, modelComponent, innerWidgetCell!!)
                MESH -> changeToMesh(component, modelComponent, innerWidgetCell!!)
                ARRAY -> changeToArray(component, innerWidgetCell!!)
                else -> throw RuntimeException("Unsupported model type!")
            }
        }
        rootWidget.addRow()

        innerWidgetCell = rootWidget.addEmptyWidget()
        innerWidgetCell.grow()

        when (component.shapeType) {
            ShapeType.BOX -> addBoxWidgets(component, innerWidgetCell.rootWidget)
            ShapeType.SPHERE -> addSphereWidgets(component, innerWidgetCell.rootWidget)
            ShapeType.CYLINDER -> addCylinderWidgets(component, innerWidgetCell.rootWidget)
            ShapeType.CAPSULE -> addCapsuleWidgets(component, innerWidgetCell.rootWidget)
            ShapeType.MESH -> addMeshWidgets(component, innerWidgetCell.rootWidget)
            ShapeType.ARRAY -> addArrayWidgets(component, innerWidgetCell.rootWidget)
            else -> throw RuntimeException("Unsupported model type!")
        }
    }

    private fun getSelectBoxType(component: Ode4jPhysicsComponent): String {
        return when(component.shapeType) {
            ShapeType.BOX -> BOX
            ShapeType.SPHERE -> SPHERE
            ShapeType.CYLINDER -> CYLINDER
            ShapeType.CAPSULE -> CAPSULE
            ShapeType.MESH -> MESH
            ShapeType.ARRAY -> ARRAY
            else -> throw RuntimeException("Unsupported model type!")
        }
    }

    private fun changeToBox(
        component: Ode4jPhysicsComponent,
        modelComponent: ModelComponent,
        innerWidgetCell: RootWidgetCell
    ) {
        component.shapeType = ShapeType.BOX

        // Create static box geom
        val goScale = modelComponent.gameObject.getScale(Vector3())
        val goPosition = modelComponent.gameObject.getPosition(Vector3())
        val goQuaternion = modelComponent.gameObject.getRotation(Quaternion())
        val boundingBox = modelComponent.orientedBoundingBox.bounds
        val geomWidth = boundingBox.width * goScale.x
        val geomHeight = boundingBox.height * goScale.y
        val geomDepth = boundingBox.depth * goScale.z
        component.geom = OdePhysicsUtils.createBox(goPosition, goQuaternion, geomWidth.toDouble(), geomHeight.toDouble(), geomDepth.toDouble())

        addBoxWidgets(component, innerWidgetCell.rootWidget)
    }

    private fun changeToSphere(
        component: Ode4jPhysicsComponent,
        modelComponent: ModelComponent,
        innerWidgetCell: RootWidgetCell
    ) {
        component.shapeType = ShapeType.SPHERE

        // Create static sphere box geom
        val goScale = modelComponent.gameObject.getScale(Vector3())
        val goPosition = modelComponent.gameObject.getPosition(Vector3())
        val goRotation = modelComponent.gameObject.getRotation(Quaternion())
        val boundingBox = modelComponent.orientedBoundingBox.bounds
        val geomRadius = Math.max(Math.max(boundingBox.width * goScale.x, boundingBox.depth * goScale.z), boundingBox.height * goScale.y) / 2.0
        component.geom = OdePhysicsUtils.createSphere(goPosition, goRotation, geomRadius)

        addSphereWidgets(component, innerWidgetCell.rootWidget)
    }

    private fun changeToCylinder(
        component: Ode4jPhysicsComponent,
        modelComponent: ModelComponent,
        innerWidgetCell: RootWidgetCell
    ) {
        component.shapeType = ShapeType.CYLINDER

        // Create static cylinder geom
        val goScale = modelComponent.gameObject.getScale(Vector3())
        val goPosition = modelComponent.gameObject.getPosition(Vector3())
        val goRotation = modelComponent.gameObject.getRotation(Quaternion())
        val boundingBox = modelComponent.orientedBoundingBox.bounds
        val radius = Math.max(boundingBox.width * goScale.x, boundingBox.depth * goScale.z) / 2.0
        val height = boundingBox.height * goScale.y
        component.geom = OdePhysicsUtils.createCylinder(goPosition, goRotation, radius, height.toDouble())

        addCylinderWidgets(component, innerWidgetCell.rootWidget)
    }

    private fun changeToCapsule(
        component: Ode4jPhysicsComponent,
        modelComponent: ModelComponent,
        innerWidgetCell: RootWidgetCell
    ) {
        component.shapeType = ShapeType.CAPSULE

        // Create static capsule geom
        val goScale = modelComponent.gameObject.getScale(Vector3())
        val goPosition = modelComponent.gameObject.getPosition(Vector3())
        val goRotation = modelComponent.gameObject.getRotation(Quaternion())
        val boundingBox = modelComponent.orientedBoundingBox.bounds
        val radius = Math.max(boundingBox.width * goScale.x, boundingBox.depth * goScale.z) / 2.0
        var height = boundingBox.height.toDouble() * goScale.y
        if (height < radius * 2.0) {
            height = radius * 2.0
        }

        component.geom = OdePhysicsUtils.createCapsule(goPosition, goRotation, radius, height)

        addCapsuleWidgets(component, innerWidgetCell.rootWidget)
    }

    private fun changeToMesh(
        component: Ode4jPhysicsComponent,
        modelComponent: ModelComponent,
        innerWidgetCell: RootWidgetCell
    ) {
        val physicsWorld = MundusOde4jRuntimePlugin.getPhysicsWorld()

        component.shapeType = ShapeType.MESH

        // Create static mesh geom
        val triMeshData = physicsWorld.createTriMeshData()
        Utils3D.fillTriMeshData(component.gameObject, modelComponent.modelInstance, triMeshData)
        component.geom = physicsWorld.createTriMesh(triMeshData)

        addMeshWidgets(component, innerWidgetCell.rootWidget)
    }

    private fun changeToArray(
        component: Ode4jPhysicsComponent,
        innerWidgetCell: RootWidgetCell
    ) {
        component.shapeType = ShapeType.ARRAY

        // Init vertices
        val geomData = ArrayGeomData()
        geomData.vertices.add(Vector3(-10.0f,  0.0f, -10.0f))
        geomData.vertices.add(Vector3(-10.0f,  0.0f,  10.0f))
        geomData.vertices.add(Vector3( 10.0f,  0.0f, -10.0f))
        geomData.vertices.add(Vector3( 10.0f,  0.0f,  10.0f))
        geomData.vertices.add(Vector3(  0.0f, 10.0f,   0.0f))

        MeshUtils.generateIndices(geomData.vertices, geomData.indices)

        val goPosition = component.gameObject.getPosition(Vector3())
        val goRotation = component.gameObject.getRotation(Quaternion())
        component.geom = OdePhysicsUtils.createTriMesh(goPosition, goRotation, geomData)

        addArrayWidgets(component, innerWidgetCell.rootWidget)
    }

    private fun addBoxWidgets(component: Ode4jPhysicsComponent, rootWidget: RootWidget) {
        var boxGeom = component.geom as DBox
        val length = DVector3()
        boxGeom.getLengths(length)

        var static = component.geom.body == null

        var massParentWidgetCell: RootWidgetCell? = null
        var mass = if (static) DEFAULT_MASS else boxGeom.body.mass.mass

        rootWidget.addCheckbox("Static", static) {
            boxGeom.getLengths(length)
            destroyBody(component)
            destroyGeom(component)
            destroyDebugInstance(component)

            static = it
            val goPosition = component.gameObject.getPosition(Vector3())
            val goRotation = component.gameObject.getRotation(Quaternion())

            if (static) {
                boxGeom = OdePhysicsUtils.createBox(goPosition, goRotation, length.get0(), length.get1(), length.get2())
            } else {
                boxGeom = OdePhysicsUtils.createBox(goPosition, goRotation, length.get0(), length.get1(), length.get2(), mass)
                component.body = boxGeom.body
            }
            component.geom = boxGeom

            if (static) {
                massParentWidgetCell?.rootWidget?.clearWidgets()
            } else {
                createMassSpinner(massParentWidgetCell!!.rootWidget, mass) { newMass -> run {
                    mass = newMass
                    boxGeom.body.mass = MassUtils.createBoxMass(length, mass)
                }}
            }
        }.setAlign(WidgetAlign.LEFT).setPad(0.0f, 0.0f, STATIC_BOTTOM_PAD, 0.0f)
        rootWidget.addRow()
        rootWidget.addLabel("Size:").grow().setAlign(WidgetAlign.LEFT)
        rootWidget.addRow()
        rootWidget.addSpinner("Width", 0.1f, Float.MAX_VALUE, length.get0().toFloat(), 0.1f) {
            length.set0(it.toDouble())
            boxGeom.lengths = length
            updateDebugInstanceIfNecessary(component, boxGeom)
        }.grow().setPad(0.0f, SIZE_RIGHT_PAD, 0.0f, 0.0f)
        rootWidget.addSpinner("Height", 0.1f, Float.MAX_VALUE, length.get1().toFloat(), 0.1f) {
            length.set1(it.toDouble())
            boxGeom.lengths = length
            updateDebugInstanceIfNecessary(component, boxGeom)
        }.grow().setPad(0.0f, SIZE_RIGHT_PAD, 0.0f, 0.0f)
        rootWidget.addSpinner("Depth", 0.1f, Float.MAX_VALUE, length.get2().toFloat(), 0.1f) {
            length.set2(it.toDouble())
            boxGeom.lengths = length
            updateDebugInstanceIfNecessary(component, boxGeom)
        }.grow()
        rootWidget.addRow()
        massParentWidgetCell = rootWidget.addEmptyWidget()
        massParentWidgetCell.grow().setAlign(WidgetAlign.LEFT)
        if (!static) {
            createMassSpinner(massParentWidgetCell.rootWidget, mass) { newMass -> run {
                mass = newMass
                boxGeom.body.mass = MassUtils.createBoxMass(length, mass)
            }}
        }
    }

    private fun addSphereWidgets(component: Ode4jPhysicsComponent, rootWidget: RootWidget) {
        var sphereGeom = component.geom as DSphere
        var radius = sphereGeom.radius

        var static = component.geom.body == null

        var massParentWidgetCell: RootWidgetCell? = null
        var mass = if (static) DEFAULT_MASS else sphereGeom.body.mass.mass

        rootWidget.addCheckbox("Static", static) {
            radius = sphereGeom.radius
            destroyBody(component)
            destroyGeom(component)
            destroyDebugInstance(component)

            static = it
            val goPosition = component.gameObject.getPosition(Vector3())
            val goRotation = component.gameObject.getRotation(Quaternion())

            if (static) {
                sphereGeom = OdePhysicsUtils.createSphere(goPosition, goRotation, radius)
            } else {
                sphereGeom = OdePhysicsUtils.createSphere(goPosition, goRotation, radius, mass)
                component.body = sphereGeom.body
            }
            component.geom = sphereGeom

            if (static) {
                massParentWidgetCell?.rootWidget?.clearWidgets()
            } else {
                createMassSpinner(massParentWidgetCell!!.rootWidget, mass) { newMass -> run {
                    mass = newMass
                    sphereGeom.body.mass = MassUtils.createSphereMass(radius, mass)
                }}
            }
        }.setAlign(WidgetAlign.LEFT).setPad(0.0f, 0.0f, STATIC_BOTTOM_PAD, 0.0f)
        rootWidget.addRow()
        rootWidget.addLabel("Size:").grow().setAlign(WidgetAlign.LEFT)
        rootWidget.addRow()
        rootWidget.addSpinner("Radius", 0.1f, Float.MAX_VALUE, sphereGeom.radius.toFloat(), 0.1f) {
            sphereGeom.radius = it.toDouble()
            updateDebugInstanceIfNecessary(component, sphereGeom)
        }.setAlign(WidgetAlign.LEFT)
        rootWidget.addRow()
        massParentWidgetCell = rootWidget.addEmptyWidget()
        massParentWidgetCell.setAlign(WidgetAlign.LEFT)
        if (!static) {
            createMassSpinner(massParentWidgetCell.rootWidget, mass) { newMass -> run {
                mass = newMass
                sphereGeom.body.mass = MassUtils.createSphereMass(radius, mass)
            }}
        }
    }

    private fun addCylinderWidgets(component: Ode4jPhysicsComponent, rootWidget: RootWidget) {
        var cylinderGeom = component.geom as DCylinder
        var radius = cylinderGeom.radius
        var length = cylinderGeom.length

        var static = component.geom.body == null

        var massParentWidgetCell: RootWidgetCell? = null
        var mass = if (static) DEFAULT_MASS else cylinderGeom.body.mass.mass

        rootWidget.addCheckbox("Static", static) {
            radius = cylinderGeom.radius
            length = cylinderGeom.length
            destroyBody(component)
            destroyGeom(component)
            destroyDebugInstance(component)

            static = it
            val goPosition = component.gameObject.getPosition(Vector3())
            val goRotation = component.gameObject.getRotation(Quaternion())

            if (static) {
                cylinderGeom = OdePhysicsUtils.createCylinder(goPosition, goRotation, radius, length)
            } else {
                cylinderGeom = OdePhysicsUtils.createCylinder(goPosition, goRotation, radius, length, mass)
                component.body = cylinderGeom.body
            }
            component.geom = cylinderGeom

            if (static) {
                massParentWidgetCell?.rootWidget?.clearWidgets()
            } else {
                createMassSpinner(massParentWidgetCell!!.rootWidget, mass) { newMass -> run {
                    mass = newMass
                    cylinderGeom.body.mass = MassUtils.createCylinderMass(radius, length, mass)
                }}
            }
        }.setAlign(WidgetAlign.LEFT).setPad(0.0f, 0.0f, STATIC_BOTTOM_PAD, 0.0f)
        rootWidget.addRow()
        rootWidget.addLabel("Size:").setAlign(WidgetAlign.LEFT)
        rootWidget.addRow()
        rootWidget.addSpinner("Radius", 0.1f, Float.MAX_VALUE, cylinderGeom.radius.toFloat(), 0.1f) {
            radius = it.toDouble()
            cylinderGeom.setParams(radius, length)
            updateDebugInstanceIfNecessary(component, cylinderGeom)
        }.setAlign(WidgetAlign.LEFT).setPad(0.0f, SIZE_RIGHT_PAD, 0.0f, 0.0f)
        rootWidget.addSpinner("Height", 0.1f, Float.MAX_VALUE, cylinderGeom.length.toFloat(), 0.1f) {
            length = it.toDouble()
            cylinderGeom.setParams(radius, length)
            updateDebugInstanceIfNecessary(component, cylinderGeom)
        }.setAlign(WidgetAlign.LEFT)
        rootWidget.addEmptyWidget().grow()
        rootWidget.addRow()
        massParentWidgetCell = rootWidget.addEmptyWidget()
        massParentWidgetCell.setAlign(WidgetAlign.LEFT)
        if (!static) {
            createMassSpinner(massParentWidgetCell.rootWidget, mass) { newMass -> run {
                mass = newMass
                cylinderGeom.body.mass = MassUtils.createCylinderMass(radius, length, mass)
            }}
        }
    }

    private fun addCapsuleWidgets(component: Ode4jPhysicsComponent, rootWidget: RootWidget) {
        var capsuleGeom = component.geom as DCapsule
        var radius = capsuleGeom.radius
        var length = capsuleGeom.length

        var static = component.geom.body == null

        var massParentWidgetCell: RootWidgetCell? = null
        var mass = if (static) DEFAULT_MASS else capsuleGeom.body.mass.mass

        var errorMessageWidgetCell: RootWidgetCell? = null

        rootWidget.addCheckbox("Static", static) {
            radius = capsuleGeom.radius
            length = capsuleGeom.length
            destroyBody(component)
            destroyGeom(component)
            destroyDebugInstance(component)

            static = it
            val goPosition = component.gameObject.getPosition(Vector3())
            val goRotation = component.gameObject.getRotation(Quaternion())

            if (static) {
                capsuleGeom = OdePhysicsUtils.createCapsule(goPosition, goRotation, radius, length)
            } else {
                capsuleGeom = OdePhysicsUtils.createCapsule(goPosition, goRotation, radius, length, mass)
                component.body = capsuleGeom.body
            }
            component.geom = capsuleGeom

            if (static) {
                massParentWidgetCell?.rootWidget?.clearWidgets()
            } else {
                createMassSpinner(massParentWidgetCell!!.rootWidget, mass) { newMass -> run {
                    mass = newMass
                    capsuleGeom.body.mass = MassUtils.createCapsuleMass(radius, length, mass)
                }}
            }
        }.setAlign(WidgetAlign.LEFT).setPad(0.0f, 0.0f, STATIC_BOTTOM_PAD, 0.0f)
        rootWidget.addRow()
        rootWidget.addLabel("Size:").setAlign(WidgetAlign.LEFT)
        rootWidget.addRow()
        val radiusAndHeightWidget = rootWidget.addEmptyWidget()
        radiusAndHeightWidget.grow().setAlign(WidgetAlign.LEFT)

        radiusAndHeightWidget.rootWidget.addSpinner("Radius", 0.1f, Float.MAX_VALUE, capsuleGeom.radius.toFloat(), 0.1f) {
            errorMessageWidgetCell?.rootWidget?.clearWidgets()
            if (length < 2 * it) {
                errorMessageWidgetCell?.rootWidget?.addLabel("The height must be at least twice the radius!")
            } else {
                radius = it.toDouble()
                capsuleGeom.setParams(radius, length)
                updateDebugInstanceIfNecessary(component, capsuleGeom)
            }
        }.setAlign(WidgetAlign.LEFT).setPad(0.0f, SIZE_RIGHT_PAD, 0.0f, 0.0f)
        radiusAndHeightWidget.rootWidget.addSpinner("Height", 0.1f, Float.MAX_VALUE, capsuleGeom.length.toFloat(), 0.1f) {
            errorMessageWidgetCell?.rootWidget?.clearWidgets()
            if (it.toDouble() < 2 * radius) {
                errorMessageWidgetCell?.rootWidget?.addLabel("The height must be at least twice the radius!")
            } else {
                length = it.toDouble()
                capsuleGeom.setParams(radius, length)
                updateDebugInstanceIfNecessary(component, capsuleGeom)
            }
        }.setAlign(WidgetAlign.LEFT)
        radiusAndHeightWidget.rootWidget.addEmptyWidget().grow()
        rootWidget.addRow()
        errorMessageWidgetCell = rootWidget.addEmptyWidget()
        rootWidget.addRow()
        massParentWidgetCell = rootWidget.addEmptyWidget()
        massParentWidgetCell.setAlign(WidgetAlign.LEFT)
        if (!static) {
            createMassSpinner(massParentWidgetCell.rootWidget, mass) { newMass -> run {
                mass = newMass
                capsuleGeom.body.mass = MassUtils.createCylinderMass(radius, length, mass)
            }}
        }
    }

    private fun addMeshWidgets(component: Ode4jPhysicsComponent, rootWidget: RootWidget) {
        var static = component.geom.body == null

        rootWidget.addCheckbox("Static", static) {
            // TODO
        }.setAlign(WidgetAlign.LEFT)
        rootWidget.addEmptyWidget().grow()
    }

    private fun addArrayWidgets(component: Ode4jPhysicsComponent, rootWidget: RootWidget) {
        var arrayGeom = component.geom as DTriMesh
        var static = component.geom.body == null

        val geomData = component.geom.data as ArrayGeomData

        var massParentWidgetCell: RootWidgetCell? = null
        var mass = if (static) DEFAULT_MASS else arrayGeom.body.mass.mass

        rootWidget.addCheckbox("Static", static) {
            destroyBody(component)
            destroyGeom(component)
            destroyDebugInstance(component)

            static = it

            val goPosition = component.gameObject.getPosition(Vector3())
            val goRotation = component.gameObject.getRotation(Quaternion())

            if (static) {
                arrayGeom = OdePhysicsUtils.createTriMesh(goPosition, goRotation, geomData)
            } else {
                arrayGeom = OdePhysicsUtils.createTriMesh(goPosition, goRotation, geomData, mass)
            }
            component.geom = arrayGeom

            if (static) {
                massParentWidgetCell?.rootWidget?.clearWidgets()
            } else {
                createMassSpinner(massParentWidgetCell!!.rootWidget, mass) { newMass -> run {
                    mass = newMass
                    arrayGeom.body.mass = MassUtils.createArrayMass(arrayGeom, mass)
                }}
            }
        }.setAlign(WidgetAlign.LEFT).setPad(0.0f, 0.0f, STATIC_BOTTOM_PAD, 0.0f)
        rootWidget.addRow()
        val arrayWidgetCell = rootWidget.addEmptyWidget()
        arrayWidgetCell.grow()
        val arrayWidget = arrayWidgetCell.rootWidget
        for (i in 0 until geomData.vertices.size) {
            val vertexData = geomData.vertices.get(i)
            val vertexWidgetCell = arrayWidget.addEmptyWidget()
            vertexWidgetCell.grow()
            val vertexWidget = vertexWidgetCell.rootWidget

            setupVertexWidget(component, vertexWidget, geomData, vertexData)
            arrayWidget.addRow()
        }
        rootWidget.addRow()
        rootWidget.addTextButton("Add") {
            geomData.vertices.add(Vector3())
            setupVertexWidget(component, arrayWidget, geomData, geomData.vertices.last())
            arrayWidget.addRow()

            regenerateArrayGeom(component, geomData)
        }
        rootWidget.addRow()
        massParentWidgetCell = rootWidget.addEmptyWidget()
        massParentWidgetCell.setAlign(WidgetAlign.LEFT)
        if (!static) {
            createMassSpinner(massParentWidgetCell.rootWidget, mass) { newMass -> run {
                mass = newMass
                arrayGeom.body.mass = MassUtils.createArrayMass(arrayGeom, mass)
            }}
        }

    }

    private fun setupVertexWidget(
        component: Ode4jPhysicsComponent,
        rootWidget: RootWidget,
        geomData: ArrayGeomData,
        vertexData: Vector3)
    {
        val vertexWidgetCell = rootWidget.addEmptyWidget()
        vertexWidgetCell.grow()
        val vertexWidget = vertexWidgetCell.rootWidget

        vertexWidget.addSpinner("x:", -1000f, 1000f, vertexData.x) {
            if (!containsVertex(geomData.vertices, it, vertexData.y, vertexData.z)) {
                vertexData.x = it
                regenerateArrayGeom(component, geomData)
            }
        }.grow().setPad(VERTEX_WIDGET_TOP_BOTTOM_PAD, 0.0f, VERTEX_WIDGET_TOP_BOTTOM_PAD, 0.0f)
        vertexWidget.addSpinner("y:", -1000f, 1000f, vertexData.y) {
            if (!containsVertex(geomData.vertices, vertexData.x, it, vertexData.z)) {
                vertexData.y = it
                regenerateArrayGeom(component, geomData)
            }
        }.grow().setPad(VERTEX_WIDGET_TOP_BOTTOM_PAD, 0.0f, VERTEX_WIDGET_TOP_BOTTOM_PAD, 0.0f)
        vertexWidget.addSpinner("z:", -1000f, 1000f, vertexData.z) {
            if (!containsVertex(geomData.vertices, vertexData.x, vertexData.y, it)) {
                vertexData.z = it
                regenerateArrayGeom(component, geomData)
            }
        }.grow().setPad(VERTEX_WIDGET_TOP_BOTTOM_PAD, 0.0f, VERTEX_WIDGET_TOP_BOTTOM_PAD, 0.0f)
        vertexWidget.addTextButton("X") {
            if (3 < geomData.vertices.size) {
                vertexWidgetCell.delete()
                geomData.vertices.removeValue(vertexData, true)
                regenerateArrayGeom(component, geomData)
            }
        }.setPad(0.0f, 0.0f, 0.0f, VERTEX_WIDGET_DELETE_BUTTON_LEFT_PAD)
    }

    private fun containsVertex(vertices: Array<Vector3>, x: Float, y: Float, z: Float): Boolean {
        for (vertex in vertices) {
            if (vertex.x == x && vertex.y == y && vertex.z == z) {
                return true
            }
        }

        return false
    }

    private fun regenerateArrayGeom(component: Ode4jPhysicsComponent, geomData: ArrayGeomData) {
        var mass = OdePhysicsUtils.INVALID_MASS
        if (component.body != null) {
            mass = component.body.mass.mass
        }

        destroyBody(component)
        destroyGeom(component)
        destroyDebugInstance(component)

        MeshUtils.generateIndices(geomData.vertices, geomData.indices)

        val goPosition = component.gameObject.getPosition(Vector3())
        val goRotation = component.gameObject.getRotation(Quaternion())
        component.geom = OdePhysicsUtils.createTriMesh(goPosition, goRotation, geomData, mass)
        if (component.geom.body != null) {
            component.body = component.geom.body
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

    private fun updateDebugInstanceIfNecessary(component: Ode4jPhysicsComponent, geom: DCapsule) {
        if (component.debugInstance == null) return

        var debugInstance = component.debugInstance
        debugInstance.model.dispose()

        val radius = geom.radius
        val height = geom.length
        debugInstance = DebugModelBuilder.createCapsule(radius.toFloat(), height.toFloat())
        debugInstance.transform.setTranslation(component.gameObject.getPosition(TMP_VECTOR3))
        component.debugInstance = debugInstance
    }

    private fun createMassSpinner(widget: RootWidget, value: Double, f: (Double) -> Unit): Cell {
        val result = widget.addSpinner("Mass:", 0.1f, Float.MAX_VALUE, value.toFloat()) { f.invoke(it.toDouble()) }
        result.grow().setAlign(WidgetAlign.LEFT)

        return result
    }
}
