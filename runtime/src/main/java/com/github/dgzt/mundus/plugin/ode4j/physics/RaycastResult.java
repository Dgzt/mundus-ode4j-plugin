package com.github.dgzt.mundus.plugin.ode4j.physics;


import org.ode4j.math.DVector3;
import org.ode4j.ode.DGeom;

public class RaycastResult {

    private boolean hit = false;
    private DVector3 contactPosition = null;
    private double depth = Double.MAX_VALUE;
    private DGeom geom = null;

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public DVector3 getContactPosition() {
        return contactPosition;
    }

    public void setContactPosition(DVector3 contactPosition) {
        this.contactPosition = contactPosition;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public DGeom getGeom() {
        return geom;
    }

    public void setGeom(DGeom geom) {
        this.geom = geom;
    }
}
