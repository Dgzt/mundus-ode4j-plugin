package com.github.dgzt.mundus.plugin.ode4j.component.updater;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import org.ode4j.Ode2GdxMathUtils;
import org.ode4j.math.DVector3C;
import org.ode4j.ode.DBody;
import org.ode4j.ode.DGeom;

/**
 * Default component updater for {@link Ode4jPhysicsComponent}.
 */
public class DefaultOde4jPhysicsComponentUpdater implements Ode4jPhysicsComponentUpdater {

    private final Ode4jPhysicsComponent component;

    public DefaultOde4jPhysicsComponentUpdater(final Ode4jPhysicsComponent component) {
        this.component = component;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        final GameObject gameObject = component.gameObject;
        final DBody body = component.getBody();
        final DGeom geom = component.getGeom();

        // Update model
        Quaternion quaternion = Ode2GdxMathUtils.getGdxQuaternion(body.getQuaternion());
        DVector3C position = body.getPosition();

        gameObject.setLocalRotation(quaternion.x, quaternion.y, quaternion.z, quaternion.w);
        gameObject.setLocalPosition((float) position.get0(), (float) position.get1(), (float) position.get2());

        // Update debug instance
        final ModelInstance debugInstance = component.getDebugInstance();
        if (debugInstance != null) {
            quaternion = Ode2GdxMathUtils.getGdxQuaternion(geom.getQuaternion());
            position = geom.getPosition();

            debugInstance.transform.set(quaternion);
            debugInstance.transform.setTranslation((float) position.get0(), (float) position.get1(), (float) position.get2());
        }
    }
}
