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

    private double angularDamping = 0.0;

    private boolean autoDisableFlag = false;

    private double autoDisableLinearThreshold = 0.01;

    private double autoDisableAngularThreshold = 0.01;

    private double autoDisableTime = 0.0;

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

    public double getAngularDamping() {
        return angularDamping;
    }

    /**
     * {@link com.github.antzGames.gdx.ode4j.ode.DWorld#setAngularDamping(double)}
     *
     * @param angularDamping Set the world's angular damping scale.
     */
    public void setAngularDamping(double angularDamping) {
        this.angularDamping = angularDamping;
    }

    public boolean isAutoDisableFlag() {
        return autoDisableFlag;
    }

    /**
     * {@link com.github.antzGames.gdx.ode4j.ode.DWorld#setAutoDisableFlag(boolean)}
     *
     * @param autoDisableFlag Set auto disable flag for newly created bodies.
     */
    public void setAutoDisableFlag(boolean autoDisableFlag) {
        this.autoDisableFlag = autoDisableFlag;
    }

    public double getAutoDisableLinearThreshold() {
        return autoDisableLinearThreshold;
    }

    /**
     * {@link com.github.antzGames.gdx.ode4j.ode.DWorld#setAutoDisableLinearThreshold(double)}
     *
     * @param autoDisableLinearThreshold Set auto disable linear threshold for newly created bodies.
     */
    public void setAutoDisableLinearThreshold(double autoDisableLinearThreshold) {
        this.autoDisableLinearThreshold = autoDisableLinearThreshold;
    }

    public double getAutoDisableAngularThreshold() {
        return autoDisableAngularThreshold;
    }

    /**
     * {@link com.github.antzGames.gdx.ode4j.ode.DWorld#setAutoDisableAngularThreshold(double)}
     *
     * @param autoDisableAngularThreshold Set auto disable angular threshold for newly created bodies.
     */
    public void setAutoDisableAngularThreshold(double autoDisableAngularThreshold) {
        this.autoDisableAngularThreshold = autoDisableAngularThreshold;
    }

    public double getAutoDisableTime() {
        return autoDisableTime;
    }

    /**
     * {@link com.github.antzGames.gdx.ode4j.ode.DWorld#setAutoDisableTime(double)}
     *
     * @param autoDisableTime Set auto disable time for newly created bodies.
     */
    public void setAutoDisableTime(double autoDisableTime) {
        this.autoDisableTime = autoDisableTime;
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
