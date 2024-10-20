package com.github.dgzt.mundus.plugin.ode4j.physics;

import com.github.antzGames.gdx.ode4j.ode.DContact;
import com.github.antzGames.gdx.ode4j.ode.DContactBuffer;
import com.github.antzGames.gdx.ode4j.ode.DGeom;
import com.github.antzGames.gdx.ode4j.ode.DRay;
import com.github.antzGames.gdx.ode4j.ode.OdeHelper;

public class RaycastNearCallback implements DGeom.DNearCallback {

    private static final int CONTACT_BUFFER_SIZE = 4;

    @Override
    public void call(final Object data, final DGeom dGeom1, final DGeom dGeom2) {
        final RaycastResult raycastResult = (RaycastResult) data;

        final DContactBuffer contacts = new DContactBuffer(CONTACT_BUFFER_SIZE);
        int n = OdeHelper.collide(dGeom1, dGeom2, CONTACT_BUFFER_SIZE, contacts.getGeomBuffer());
        for (int i = 0; i < n; ++i) {
            final DContact contact = contacts.get(i);

            if (!raycastResult.isHit() || contact.geom.depth < raycastResult.getDepth()) {
                raycastResult.setHit(true);
                raycastResult.setContactPosition(contact.geom.pos);
                raycastResult.setDepth(contact.geom.depth);
                raycastResult.setGeom(dGeom1 instanceof DRay ? dGeom2 : dGeom1);
            }
        }
    }
}
