# Ode4j Mundus plugin 

This is an ode4j physics plugin for [Mundus Editor](https://github.com/JamesTKhan/Mundus).

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
        mundusVersion = 'master-SNAPSHOT'
        ode4jPluginVersion = 'master-SNAPSHOT'
        gltfVersion = '2.2.1' // Only needed if targeting HTML, version should match what Mundus uses
    }
}

...

project(":core") {
    ...
    dependencies {
        ...
        api "com.github.jamestkhan.mundus:gdx-runtime:$mundusVersion"
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
        api "com.github.jamestkhan.mundus:gdx-runtime:$mundusVersion:sources"
        api "com.github.jamestkhan.mundus:commons:$mundusVersion:sources"
        api "com.github.mgsx-dev.gdx-gltf:gltf:$gltfVersion:sources"
        
        api "com.github.Dgzt:mundus-ode4j-plugin:$ode4jPluginVersion:sources"
        api "com.github.antzGames:ode4j:0.5.4.GWT:sources"
        api "com.github.tommyettinger:formic:0.1.5:sources"
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
