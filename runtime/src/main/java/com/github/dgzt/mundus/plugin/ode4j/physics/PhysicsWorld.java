package com.github.dgzt.mundus.plugin.ode4j.physics;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.github.antzGames.gdx.ode4j.ode.DBody;
import com.github.antzGames.gdx.ode4j.ode.DBox;
import com.github.antzGames.gdx.ode4j.ode.DCylinder;
import com.github.antzGames.gdx.ode4j.ode.DHeightfieldData;
import com.github.antzGames.gdx.ode4j.ode.DHinge2Joint;
import com.github.antzGames.gdx.ode4j.ode.DJointGroup;
import com.github.antzGames.gdx.ode4j.ode.DPlane;
import com.github.antzGames.gdx.ode4j.ode.DSapSpace;
import com.github.antzGames.gdx.ode4j.ode.DSpace;
import com.github.antzGames.gdx.ode4j.ode.DSphere;
import com.github.antzGames.gdx.ode4j.ode.DTriMesh;
import com.github.antzGames.gdx.ode4j.ode.DTriMeshData;
import com.github.antzGames.gdx.ode4j.ode.DWorld;
import com.github.antzGames.gdx.ode4j.ode.OdeHelper;
import com.github.antzGames.gdx.ode4j.ode.internal.DxTrimeshHeightfield;
import com.github.dgzt.mundus.plugin.ode4j.component.AbstractOde4jPhysicsComponent;
import com.github.dgzt.mundus.plugin.ode4j.config.RuntimeConfig;

public class PhysicsWorld implements Disposable {

    private final DWorld world;
    private final DSpace space;
    private final DJointGroup contactGroup;
    private final AbstractNearCallback nearCallback;
    private final UpdateCallback updateCallback;
    private final double step;
    private final Array<AbstractOde4jPhysicsComponent> physicsComponents;

    public PhysicsWorld(final RuntimeConfig config) {
        OdeHelper.initODE2(0);

        world = OdeHelper.createWorld();
        world.setGravity(config.getGravity().x, config.getGravity().y, config.getGravity().z);
        world.setCFM(config.getCfm());
        world.setERP(config.getErp());
        world.setQuickStepNumIterations(config.getQuickStepNumIterations());
        world.setAngularDamping(config.getAngularDamping());
        world.setAutoDisableFlag(config.isAutoDisableFlag());
        world.setAutoDisableLinearThreshold(config.getAutoDisableLinearThreshold());
        world.setAutoDisableAngularThreshold(config.getAutoDisableAngularThreshold());
        world.setAutoDisableTime(config.getAutoDisableTime());

        // TODO support other spaces
        space = OdeHelper.createSapSpace(null, DSapSpace.AXES.XZY);

        contactGroup = OdeHelper.createJointGroup();

        nearCallback = config.getNearCallback();
        nearCallback.setWorld(world);
        nearCallback.setContactGroup(contactGroup);

        step = config.getStep();

        updateCallback = config.getUpdateCallback();

        physicsComponents = new Array<>();
    }

    public DTriMeshData createTriMeshData() {
        return OdeHelper.createTriMeshData();
    }

    public DPlane createPlane() {
        return OdeHelper.createPlane(space, 0.0, 1.0, 0.0, 0.0);
    }

    public DxTrimeshHeightfield createTrimeshHeightfield(final DHeightfieldData data) {
        return new DxTrimeshHeightfield(space, data, true);
    }

    public DTriMesh createTriMesh(final DTriMeshData data) {
        return OdeHelper.createTriMesh(space, data, null, null, null);
    }

    public DBox createBox(final double width, final double height, final double depth) {
        return OdeHelper.createBox(space, width, height, depth);
    }

    public DSphere createSphere(final double radius) {
        return OdeHelper.createSphere(space, radius);
    }

    public DCylinder createCylinder(final double radius, final double height) {
        return OdeHelper.createCylinder(space, radius, height);
    }

    public DBody createBody() {
        return OdeHelper.createBody(world);
    }

    public DHinge2Joint createHinge2Joint() {
        return OdeHelper.createHinge2Joint(world);
    }

    public void update() {
        space.collide(null, nearCallback);

        world.quickStep(step);

        contactGroup.empty();

        updateCallback.update(physicsComponents);
    }

    @Override
    public void dispose() {
        contactGroup.destroy();
        space.destroy();
        world.destroy();
        OdeHelper.closeODE();
    }

    public Array<AbstractOde4jPhysicsComponent> getPhysicsComponents() {
        return physicsComponents;
    }
}
