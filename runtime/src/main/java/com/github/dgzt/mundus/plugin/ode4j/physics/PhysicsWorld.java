package com.github.dgzt.mundus.plugin.ode4j.physics;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.github.antzGames.gdx.ode4j.ode.DBody;
import com.github.antzGames.gdx.ode4j.ode.DJointGroup;
import com.github.antzGames.gdx.ode4j.ode.DSapSpace;
import com.github.antzGames.gdx.ode4j.ode.DSpace;
import com.github.antzGames.gdx.ode4j.ode.DWorld;
import com.github.antzGames.gdx.ode4j.ode.OdeHelper;
import com.github.dgzt.mundus.plugin.ode4j.component.Ode4jPhysicsComponent;
import com.github.dgzt.mundus.plugin.ode4j.config.RuntimeConfig;

public class PhysicsWorld implements Disposable {

    private final DWorld world;
    private final DSpace space;
    private final DJointGroup contactGroup;
    private final AbstractNearCallback nearCallback;
    private final UpdateCallback updateCallback;
    private final double step;
    private final Array<Ode4jPhysicsComponent> physicsComponents;

    public PhysicsWorld(final RuntimeConfig config) {
        OdeHelper.initODE2(0);

        world = OdeHelper.createWorld();
        world.setGravity(config.getGravity().x, config.getGravity().y, config.getGravity().z);
        world.setCFM(config.getCfm());
        world.setERP(config.getErp());
        world.setQuickStepNumIterations(config.getQuickStepNumIterations());

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

    public DBody createBody() {
        return OdeHelper.createBody(world);
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
}
