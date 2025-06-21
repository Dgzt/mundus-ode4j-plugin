package com.github.dgzt.mundus.plugin.ode4j.physics;


import org.ode4j.ode.DContact;
import org.ode4j.ode.DContactJoint;
import org.ode4j.ode.DGeom;
import org.ode4j.ode.DJointGroup;
import org.ode4j.ode.DWorld;
import org.ode4j.ode.OdeHelper;

public abstract class AbstractNearCallback implements DGeom.DNearCallback {

    private DWorld world;
    private DJointGroup contactGroup;

    protected DContactJoint createJoint(final DContact contact, final DGeom geom1, final DGeom geom2) {
        final DContactJoint joint = OdeHelper.createContactJoint(world, contactGroup, contact);
        joint.attach(geom1.getBody(), geom2.getBody());

        return joint;
    }

    public DWorld getWorld() {
        return world;
    }

    public void setWorld(DWorld world) {
        this.world = world;
    }

    public DJointGroup getContactGroup() {
        return contactGroup;
    }

    public void setContactGroup(DJointGroup contactGroup) {
        this.contactGroup = contactGroup;
    }
}
