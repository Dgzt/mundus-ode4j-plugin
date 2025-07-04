package com.github.dgzt.mundus.plugin.ode4j.converter;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.dgzt.mundus.plugin.ode4j.MundusOde4jRuntimePlugin;
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent;
import com.github.dgzt.mundus.plugin.ode4j.constant.SaveConstants;
import com.github.dgzt.mundus.plugin.ode4j.physics.ArrayGeomData;
import com.github.dgzt.mundus.plugin.ode4j.type.ShapeType;
import com.github.dgzt.mundus.plugin.ode4j.util.Ode4jPhysicsComponentUtils;
import com.mbrlabs.mundus.commons.assets.Asset;
import com.mbrlabs.mundus.commons.mapper.CustomComponentConverter;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import org.ode4j.math.DVector3C;
import org.ode4j.ode.DBox;
import org.ode4j.ode.DCapsule;
import org.ode4j.ode.DCylinder;
import org.ode4j.ode.DGeom;
import org.ode4j.ode.DSphere;

public class Ode4jPhysicsComponentConverter implements CustomComponentConverter {

    @Override
    public Component.Type getComponentType() {
        return Component.Type.PHYSICS;
    }

    @Override
    public OrderedMap<String, String> convert(final Component component) {
        if (!(component instanceof Ode4jPhysicsComponent)) {
            return null;
        }

        final OrderedMap<String, String> map = new OrderedMap<>();
        final Ode4jPhysicsComponent ode4jComponent = (Ode4jPhysicsComponent) component;
        map.put(SaveConstants.SHAPE, ode4jComponent.getShapeType().name());

        if (ShapeType.BOX == ode4jComponent.getShapeType()) {
            final DBox boxGeom = (DBox) ode4jComponent.getGeom();
            final DVector3C boxLength = boxGeom.getLengths();

            map.put(SaveConstants.BOX_WIDTH, String.valueOf(boxLength.get0()));
            map.put(SaveConstants.BOX_HEIGHT, String.valueOf(boxLength.get1()));
            map.put(SaveConstants.BOX_DEPTH, String.valueOf(boxLength.get2()));
            if (boxGeom.getBody() != null) {
                final double mass = boxGeom.getBody().getMass().getMass();
                map.put(SaveConstants.BOX_MASS, String.valueOf(mass));
            }
        } else if (ShapeType.SPHERE == ode4jComponent.getShapeType()) {
            final DSphere sphereGeom = (DSphere) ode4jComponent.getGeom();
            final double radius = sphereGeom.getRadius();

            map.put(SaveConstants.SPHERE_RADIUS, String.valueOf(radius));
            if (sphereGeom.getBody() != null) {
                final double mass = sphereGeom.getBody().getMass().getMass();
                map.put(SaveConstants.SPHERE_MASS, String.valueOf(mass));
            }
        } else if (ShapeType.CYLINDER == ode4jComponent.getShapeType()) {
            final DCylinder cylinderGeom = (DCylinder) ode4jComponent.getGeom();
            final double radius = cylinderGeom.getRadius();
            final double height = cylinderGeom.getLength();

            map.put(SaveConstants.CYLINDER_RADIUS, String.valueOf(radius));
            map.put(SaveConstants.CYLINDER_HEIGHT, String.valueOf(height));
            if (cylinderGeom.getBody() != null) {
                final double mass = cylinderGeom.getBody().getMass().getMass();
                map.put(SaveConstants.CYLINDER_MASS, String.valueOf(mass));
            }
        } else if (ShapeType.CAPSULE == ode4jComponent.getShapeType()) {
            final DCapsule capsuleGeom = (DCapsule) ode4jComponent.getGeom();
            final double radius = capsuleGeom.getRadius();
            final double height = capsuleGeom.getLength();

            map.put(SaveConstants.CAPSULE_RADIUS, String.valueOf(radius));
            map.put(SaveConstants.CAPSULE_HEIGHT, String.valueOf(height));
            if (capsuleGeom.getBody() != null) {
                final double mass = capsuleGeom.getBody().getMass().getMass();
                map.put(SaveConstants.CAPSULE_MASS, String.valueOf(mass));
            }
        } else if (ShapeType.MESH == ode4jComponent.getShapeType()) {
            // NOOP
        } else if (ShapeType.ARRAY == ode4jComponent.getShapeType()) {
            final DGeom arrayGeom = ode4jComponent.getGeom();
            final ArrayGeomData arrayGeomData = (ArrayGeomData) arrayGeom.getData();

            Json json = new Json(JsonWriter.OutputType.json);
            final String verticesJson = json.toJson(arrayGeomData.getVertices(), Array.class, Vector3.class);
            final String indicesJson = json.toJson(arrayGeomData.getIndices(), IntArray.class);

            map.put(SaveConstants.ARRAY_VERTICES, verticesJson);
            map.put(SaveConstants.ARRAY_INDICES, indicesJson);
            if (arrayGeom.getBody() != null) {
                final double mass = arrayGeom.getBody().getMass().getMass();
                map.put(SaveConstants.ARRAY_MASS, String.valueOf(mass));
            }
        }

        return map;
    }

    @Override
    public Component convert(final GameObject gameObject, final OrderedMap<String, String> orderedMap, final ObjectMap<String, Asset> objectMap) {
        final ShapeType shapeType = ShapeType.valueOf(orderedMap.get(SaveConstants.SHAPE));

        final Ode4jPhysicsComponent physicsComponent;
        switch (shapeType) {
            case TERRAIN:
                if (gameObject.findComponentByType(Component.Type.TERRAIN) == null) {
                    physicsComponent = Ode4jPhysicsComponentUtils.createTerrainSystemPhysicsComponent(gameObject);
                } else {
                    physicsComponent = Ode4jPhysicsComponentUtils.createTerrainPhysicsComponent(gameObject);
                }
                break;
            case BOX:
                final double boxWidth = Double.parseDouble(orderedMap.get(SaveConstants.BOX_WIDTH));
                final double boxHeight = Double.parseDouble(orderedMap.get(SaveConstants.BOX_HEIGHT));
                final double boxDepth = Double.parseDouble(orderedMap.get(SaveConstants.BOX_DEPTH));
                if (orderedMap.containsKey(SaveConstants.BOX_MASS)) {
                    final double boxMass = Double.parseDouble(orderedMap.get(SaveConstants.BOX_MASS));
                    physicsComponent = Ode4jPhysicsComponentUtils.createBoxPhysicsComponent(gameObject, boxWidth, boxHeight, boxDepth, boxMass);
                } else {
                    physicsComponent = Ode4jPhysicsComponentUtils.createBoxPhysicsComponent(gameObject, boxWidth, boxHeight, boxDepth);
                }
                break;
            case SPHERE:
                final double sphereRadius = Double.parseDouble(orderedMap.get(SaveConstants.SPHERE_RADIUS));
                if (orderedMap.containsKey(SaveConstants.SPHERE_MASS)) {
                    final double sphereMass = Double.parseDouble(orderedMap.get(SaveConstants.SPHERE_MASS));
                    physicsComponent = Ode4jPhysicsComponentUtils.createSpherePhysicsComponent(gameObject, sphereRadius, sphereMass);
                } else {
                    physicsComponent = Ode4jPhysicsComponentUtils.createSpherePhysicsComponent(gameObject, sphereRadius);
                }
                break;
            case CYLINDER:
                final double cylinderRadius = Double.parseDouble(orderedMap.get(SaveConstants.CYLINDER_RADIUS));
                final double cylinderHeight = Double.parseDouble(orderedMap.get(SaveConstants.CYLINDER_HEIGHT));
                if (orderedMap.containsKey(SaveConstants.CYLINDER_MASS)) {
                    final double cylinderMass = Double.parseDouble(orderedMap.get(SaveConstants.CYLINDER_MASS));
                    physicsComponent = Ode4jPhysicsComponentUtils.createCylinderPhysicsComponent(gameObject, cylinderRadius, cylinderHeight, cylinderMass);
                } else {
                    physicsComponent = Ode4jPhysicsComponentUtils.createCylinderPhysicsComponent(gameObject, cylinderRadius, cylinderHeight);
                }
                break;
            case CAPSULE:
                final double capsuleRadius = Double.parseDouble(orderedMap.get(SaveConstants.CAPSULE_RADIUS));
                final double capsuleLength = Double.parseDouble(orderedMap.get(SaveConstants.CAPSULE_HEIGHT));
                if (orderedMap.containsKey(SaveConstants.CAPSULE_MASS)) {
                    final double capsuleMass = Double.parseDouble(orderedMap.get(SaveConstants.CAPSULE_MASS));
                    physicsComponent = Ode4jPhysicsComponentUtils.createCapsulePhysicsComponent(gameObject, capsuleRadius, capsuleLength, capsuleMass);
                } else {
                    physicsComponent = Ode4jPhysicsComponentUtils.createCapsulePhysicsComponent(gameObject, capsuleRadius, capsuleRadius);
                }
                break;
            case MESH:
                physicsComponent = Ode4jPhysicsComponentUtils.createMeshComponent(gameObject);
                break;
            case ARRAY:
                final String verticesJson = orderedMap.get(SaveConstants.ARRAY_VERTICES);
                final String indicesJson = orderedMap.get(SaveConstants.ARRAY_INDICES);

                final Json json = new Json(JsonWriter.OutputType.json);
                final Array<Vector3> vertices = json.fromJson(Array.class, Vector3.class, verticesJson);
                final IntArray indices = json.fromJson(IntArray.class, indicesJson);

                if (orderedMap.containsKey(SaveConstants.ARRAY_MASS)) {
                    final double arrayMass = Double.parseDouble(orderedMap.get(SaveConstants.ARRAY_MASS));
                    physicsComponent = Ode4jPhysicsComponentUtils.createArrayComponent(gameObject, vertices, indices, arrayMass);
                } else {
                    physicsComponent = Ode4jPhysicsComponentUtils.createArrayComponent(gameObject, vertices, indices);
                }

                break;
            default: throw new RuntimeException("Not supported shape type");
        }

        MundusOde4jRuntimePlugin.getPhysicsWorld().getPhysicsComponents().add(physicsComponent);
        return physicsComponent;
    }
}
