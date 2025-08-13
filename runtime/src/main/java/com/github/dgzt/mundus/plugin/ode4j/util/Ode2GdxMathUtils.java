package com.github.dgzt.mundus.plugin.ode4j.util;

import com.badlogic.gdx.math.Quaternion;
import org.ode4j.math.DQuaternion;

public class Ode2GdxMathUtils {

    private static DQuaternion dq = new DQuaternion();

    public static DQuaternion getOde4jQuaternion(Quaternion quaternion) {
        dq.set(quaternion.w, quaternion.x, quaternion.y, quaternion.z);
        return dq;
    }
}
