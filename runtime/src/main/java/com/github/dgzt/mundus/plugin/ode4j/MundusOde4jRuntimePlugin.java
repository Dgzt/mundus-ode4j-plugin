package com.github.dgzt.mundus.plugin.ode4j;

import com.github.dgzt.mundus.plugin.ode4j.config.RuntimeConfig;
import com.github.dgzt.mundus.plugin.ode4j.physics.PhysicsWorld;

public class MundusOde4jRuntimePlugin {

    private static MundusOde4jRuntimePlugin instance;

    private final PhysicsWorld physicsWorld;

    private MundusOde4jRuntimePlugin(final RuntimeConfig config) {
        physicsWorld = new PhysicsWorld(config);
    }

    public static void init() {
        init(new RuntimeConfig());
    }

    public static void init(final RuntimeConfig config) {
        if (instance != null) {
            dispose();
        }
        instance = new MundusOde4jRuntimePlugin(config);
    }

    public static void update() {
        checkInstance();
        instance.physicsWorld.update();
    }

    public static void dispose() {
        checkInstance();
        instance.physicsWorld.dispose();
        instance = null;
    }

    private static void checkInstance() {
        if (instance == null) {
            throw new IllegalStateException("The plugin has not initialized!");
        }
    }

}
