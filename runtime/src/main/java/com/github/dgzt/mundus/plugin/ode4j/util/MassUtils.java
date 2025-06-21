package com.github.dgzt.mundus.plugin.ode4j.util;


import org.ode4j.math.DVector3C;
import org.ode4j.ode.DMass;
import org.ode4j.ode.DTriMesh;
import org.ode4j.ode.OdeHelper;

public class MassUtils {

    private static final double DENSITY = 1.0;
    private static final int CYLINDER_DIRECTION = 2; // 2 = Y
    private static final int CAPSULE_DIRECTION = 2; // 2 = Y

    private static DMass MASS_INSTANCE = null;

    public static DMass createBoxMass(
            final DVector3C boxLength,
            final double mass
    ) {
        return createBoxMass(boxLength.get0(), boxLength.get1(), boxLength.get2(), mass);
    }

    public static DMass createBoxMass(
            final double width,
            final double height,
            final double depth,
            final double mass
    ) {
        final DMass massInfo = createOrGetMassInfo();
        massInfo.setBox(DENSITY, width, height, depth);
        massInfo.adjust(mass);
        return massInfo;
    }

    public static DMass createSphereMass(
        final double radius,
        final double mass
    ) {
        final DMass massInfo = createOrGetMassInfo();
        massInfo.setSphere(DENSITY, radius);
        massInfo.adjust(mass);
        return massInfo;
    }

    public static DMass createCylinderMass(
        final double radius,
        final double height,
        final double mass
    ) {
        final DMass massInfo = createOrGetMassInfo();
        massInfo.setCylinder(DENSITY, CYLINDER_DIRECTION, radius, height);
        massInfo.adjust(mass);
        return massInfo;
    }

    public static DMass createCapsuleMass(
        final double radius,
        final double length,
        final double mass
    ) {
        final DMass massInfo = createOrGetMassInfo();
        massInfo.setCapsule(DENSITY, CAPSULE_DIRECTION, radius, length);
        massInfo.adjust(mass);
        return massInfo;
    }

    public static DMass createArrayMass(
        final DTriMesh mesh,
        final double mass
    ) {
        final DMass massInfo = createOrGetMassInfo();
        massInfo.setTrimesh(DENSITY, mesh);
        massInfo.adjust(mass);
        return massInfo;
    }

    private static DMass createOrGetMassInfo() {
        if (MASS_INSTANCE == null) {
            MASS_INSTANCE = OdeHelper.createMass();
        }

        return MASS_INSTANCE;
    }
}
