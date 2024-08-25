package com.github.dgzt.mundus.plugin.ode4j.event

import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.github.antzGames.gdx.ode4j.Ode2GdxMathUtils
import com.github.antzGames.gdx.ode4j.ode.DGeom
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent
import com.github.dgzt.mundus.plugin.ode4j.util.GameObjectUtils
import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.editorcommons.events.GameObjectModifiedEvent
import com.mbrlabs.mundus.editorcommons.events.GameObjectModifiedEvent.GameObjectModifiedListener

class GameObjectModifiedEventListener : GameObjectModifiedListener {

    companion object {
        val TMP_VECTOR3 = Vector3()
        val TMP_QUATERNION = Quaternion()
    }

    override fun onGameObjectModified(event: GameObjectModifiedEvent) {
        val gameObject = event.gameObject
        if (!GameObjectUtils.hasPhysicsComponent(gameObject)) {
            return
        }

        val physicsComponent = GameObjectUtils.getPhysicsComponent(gameObject)
        val geom = physicsComponent.geom

        updatePosition(gameObject, physicsComponent, geom)
    }

    private fun updatePosition(gameObject: GameObject, physicsComponent: Ode4jPhysicsComponent, geom: DGeom) {
        gameObject.getPosition(TMP_VECTOR3)
        gameObject.getRotation(TMP_QUATERNION)

        geom.setPosition(TMP_VECTOR3.x.toDouble(), TMP_VECTOR3.y.toDouble(), TMP_VECTOR3.z.toDouble())

        geom.quaternion = Ode2GdxMathUtils.getOde4jQuaternion(TMP_QUATERNION)

        if (physicsComponent.debugInstance != null) {
            if (GameObjectUtils.isTerrainGameObject(gameObject)) {
                val terrainComponent = GameObjectUtils.getTerrainComponent(gameObject)
                val terrain = terrainComponent.terrainAsset.terrain
                val terrainWidth = terrain.terrainWidth
                val terrainDepth = terrain.terrainDepth

                TMP_VECTOR3.add(terrainWidth / 2.0f, 0f, terrainDepth / 2.0f)
            }

            physicsComponent.debugInstance.transform.set(TMP_QUATERNION)
            physicsComponent.debugInstance.transform.setTranslation(TMP_VECTOR3)
        }
    }
}
