# Ode4j Mundus plugin 

This is an ode4j physics plugin for [Mundus Editor](https://github.com/JamesTKhan/Mundus).

**Until the [PR](https://github.com/JamesTKhan/Mundus/pull/289) will not be merged you need to use
[my fork](https://github.com/Dgzt/Mundus/tree/plugin-new-component) with `plugin-new-component` branch.**

## Setup for Editor

You need to build the plugin from source code:

```shell
mvn clean install
```

Then you need to copy `plugin/build/libs/ode4j-plugin.jar` jar file into your `.mundus/plugins/` directory.

## Usage in Editor

TODO

## Setup for Runtime

Add the dependency in your core project:

```groovy
allprojects {
    ext {
        ...
        mundusVersion = 'plugin-new-component-SNAPSHOT'
        ode4jPluginVersion = 'master-SNAPSHOT'
        gltfVersion = '2.1.0' // Only needed if targeting HTML, version should match what Mundus uses
    }
}

...

project(":core") {
    ...
    dependencies {
        ...
        api "com.github.Dgzt.Mundus:gdx-runtime:$mundusVersion"
        api "com.github.Dgzt:mundus-ode4j-plugin:$ode4jPluginVersion"
    }
}
```

If you are targeting HTML (GWT) you will also need the following:

```groovy
project(":html") {
    ...
    dependencies {
        ...
        api "com.github.Dgzt.Mundus:gdx-runtime:$mundusVersion:sources"
        api "com.github.Dgzt.Mundus:commons:$mundusVersion:sources"
        api "com.github.mgsx-dev.gdx-gltf:gltf:$gltfVersion:sources"
        
        api "com.github.Dgzt:mundus-ode4j-plugin:$ode4jPluginVersion:sources"
        api "com.github.antzGames:gdx-ode4j:master-SNAPSHOT:sources"
        api "com.github.tommyettinger:formic:0.1.4:sources"
    }
}
```

and lastly add this to your GdxDefinition.gwt.xml file:

```xml
<module>
    ...
    <inherits name="ode4j_plugin" />
    <inherits name="gdx_ode4j" />
    <inherits name="formic" />
</module>
```

## Usage in Runtime

You need to initialize the plugin and pass the converter to Mundus:

```java
    @Override
	public void create () {
    MundusOde4jRuntimePlugin.init();
    ...

    mundus = new Mundus(Gdx.files.internal("MundusExampleProject"), config, new Ode4jPhysicsComponentConverter());
    ...
	}
```

You need to update plugin in render method:

```java
    @Override
    public void render () {
    MundusOde4jRuntimePlugin.update();
    ...
    }
```
