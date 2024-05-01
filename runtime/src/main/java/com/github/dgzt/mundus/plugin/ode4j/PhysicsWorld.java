package com.github.dgzt.mundus.plugin.ode4j;

import com.badlogic.gdx.utils.Disposable;
import com.github.antzGames.gdx.ode4j.ode.DBody;
import com.github.antzGames.gdx.ode4j.ode.DWorld;
import com.github.antzGames.gdx.ode4j.ode.OdeHelper;

public class PhysicsWorld implements Disposable {

    private final DWorld world;

    public PhysicsWorld() {
        OdeHelper.initODE2(0);

        world = OdeHelper.createWorld();
        world.setGravity(0.0, -9.81, 0.0);
        world.setCFM(1e-5);
        world.setERP(0.8);
        world.setQuickStepNumIterations(20);
    }

    public DBody createBody() {
        return OdeHelper.createBody(world);
    }

    @Override
    public void dispose() {
        world.destroy();
        OdeHelper.closeODE();
    }
}
