package com.github.dgzt.mundus.plugin.ode4j.physics;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.utils.Array;
import com.github.antzGames.gdx.ode4j.Ode2GdxMathUtils;
import com.github.antzGames.gdx.ode4j.math.DVector3C;
import com.github.antzGames.gdx.ode4j.ode.DBody;
import com.github.antzGames.gdx.ode4j.ode.DGeom;
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent;
import com.mbrlabs.mundus.commons.scene3d.GameObject;

public class DefaultUpdateCallback implements UpdateCallback {

    @Override
    public void update(final Array<Ode4jPhysicsComponent> physicsComponents) {
        for (int i = 0; i < physicsComponents.size; ++i) {
            final Ode4jPhysicsComponent component = physicsComponents.get(i);
            final DBody body = component.getBody();

            if (body != null) {
                final GameObject gameObject = component.gameObject;
                final DGeom geom = component.getGeom();
                final DVector3C position = geom.getPosition();

                final float x = (float) position.get0();
                final float y = (float) position.get1();
                final float z = (float) position.get2();

                final Quaternion quaternion = Ode2GdxMathUtils.getGdxQuaternion(geom.getQuaternion());

                gameObject.setLocalRotation(quaternion.x, quaternion.y, quaternion.z, quaternion.w);
                gameObject.setLocalPosition(x, y, z);
            }
        }
    }

}
