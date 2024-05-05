package com.github.dgzt.mundus.plugin.ode4j.config;

import com.badlogic.gdx.math.Vector3;
import com.github.dgzt.mundus.plugin.ode4j.physics.AbstractNearCallback;
import com.github.dgzt.mundus.plugin.ode4j.physics.DefaultNearCallback;
import com.github.dgzt.mundus.plugin.ode4j.physics.DefaultUpdateCallback;
import com.github.dgzt.mundus.plugin.ode4j.physics.UpdateCallback;

public class RuntimeConfig {

    private Vector3 gravity = new Vector3(0f, -9.81f, 0f);

    private double cfm = 1e-5;

    private double erp = 0.8;

    private int quickStepNumIterations = 20;

    private AbstractNearCallback nearCallback = new DefaultNearCallback();

    private double step = 1 / 30.0;

    private UpdateCallback updateCallback = new DefaultUpdateCallback();

    public Vector3 getGravity() {
        return gravity;
    }

    public void setGravity(Vector3 gravity) {
        this.gravity = gravity;
    }

    public double getCfm() {
        return cfm;
    }

    public void setCfm(double cfm) {
        this.cfm = cfm;
    }

    public double getErp() {
        return erp;
    }

    public void setErp(double erp) {
        this.erp = erp;
    }

    public int getQuickStepNumIterations() {
        return quickStepNumIterations;
    }

    public void setQuickStepNumIterations(int quickStepNumIterations) {
        this.quickStepNumIterations = quickStepNumIterations;
    }

    public AbstractNearCallback getNearCallback() {
        return nearCallback;
    }

    public void setNearCallback(AbstractNearCallback nearCallback) {
        this.nearCallback = nearCallback;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public UpdateCallback getUpdateCallback() {
        return updateCallback;
    }

    public void setUpdateCallback(UpdateCallback updateCallback) {
        this.updateCallback = updateCallback;
    }
}
