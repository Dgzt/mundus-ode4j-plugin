package com.github.dgzt.mundus.plugin.ode4j.physics;


import org.ode4j.ode.DBody;
import org.ode4j.ode.DContact;
import org.ode4j.ode.DContactBuffer;
import org.ode4j.ode.DGeom;
import org.ode4j.ode.OdeHelper;

public class DefaultNearCallback extends AbstractNearCallback {

    private static final int CONTACT_BUFFER_SIZE = 4;

    @Override
    public void call(final Object o, final DGeom dGeom1, final DGeom dGeom2) {
        final DBody body1 = dGeom1.getBody();
        final DBody body2 = dGeom2.getBody();

        if (body1 != null && body2 != null && OdeHelper.areConnected(body1, body2)) {
            return;
        }

        final DContactBuffer contacts = new DContactBuffer(CONTACT_BUFFER_SIZE);
        int n = OdeHelper.collide(dGeom1, dGeom2, CONTACT_BUFFER_SIZE, contacts.getGeomBuffer());
        if (n > 0) {
            for (int i = 0; i < n; ++i) {
                final DContact contact = contacts.get(i);
                contact.surface.mu = 0.5;

                createJoint(contact, dGeom1, dGeom2);
            }
        }
    }
}
