/*
 * Trident - A Multithreaded Server Alternative
 * Copyright 2014 The TridentSDK Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.tridentsdk.plugin;

import com.google.common.collect.Lists;
import net.tridentsdk.Trident;
import net.tridentsdk.concurrent.TaskExecutor;
import net.tridentsdk.docs.InternalUseOnly;
import net.tridentsdk.event.Listener;
import net.tridentsdk.factory.ExecutorFactory;
import net.tridentsdk.factory.Factories;
import net.tridentsdk.plugin.annotation.IgnoreRegistration;
import net.tridentsdk.plugin.annotation.PluginDescription;
import net.tridentsdk.plugin.cmd.Command;
import net.tridentsdk.util.TridentLogger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Handles server plugins, loading and unloading, class management, and lifecycle management for plugins
 *
 * @author The TridentSDK Team
 */
public class TridentPluginHandler {
    private static final ExecutorFactory<TridentPlugin> PLUGIN_EXECUTOR_FACTORY = Factories.threads()
            .executor(2, "Plugins");
    private final List<TridentPlugin> plugins = Lists.newArrayList();

    /**
     * Do not instantiate this without being Trident
     */
    public TridentPluginHandler() {
        if (!Trident.isTrident())
            TridentLogger.error(new IllegalAccessException("Can only be instantiated by Trident"));
    }

    @InternalUseOnly
    public void load(final File pluginFile) {
        final TaskExecutor executor = PLUGIN_EXECUTOR_FACTORY.scaledThread();

        executor.addTask(new Runnable() {
            @Override
            public void run() {
                TridentPlugin plugin = null;
                try(JarFile jarFile = new JarFile(pluginFile);
                    PluginClassLoader loader = new PluginClassLoader(pluginFile, getClass().getClassLoader())) {

                    Class<? extends TridentPlugin> pluginClass = null;
                    Enumeration<JarEntry> entries = jarFile.entries();

                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();

                        if(!(entry.isDirectory() || !entry.getName().endsWith(".class"))) {
                            String name = entry.getName().replace(".class", "").replace(File.separatorChar, '.');
                            Class<?> loadedClass = loader.loadClass(name);

                            loader.putClass(loadedClass);

                            if(TridentPlugin.class.isAssignableFrom(loadedClass)) {
                                if(pluginClass != null) {
                                    TridentLogger.error(new PluginLoadException("Plugin has more than one main class!"));
                                }

                                pluginClass = loadedClass.asSubclass(TridentPlugin.class);
                            }
                        }
                    }

                    // start initiating the plugin class and registering commands and listeners
                    if (pluginClass == null) {
                        TridentLogger.error(new PluginLoadException("Plugin does not have a main class"));
                        loader.unloadClasses();
                    } else {
                        PluginDescription description = pluginClass.getAnnotation(PluginDescription.class);

                        if (description == null) {
                            TridentLogger.error(new PluginLoadException("PluginDescription annotation does not exist!"));
                            loader.unloadClasses();
                        } else {
                            TridentLogger.log("Loading " + description.name() + " version " + description.version());

                            Constructor<? extends TridentPlugin> defaultConstructor = pluginClass.getSuperclass()
                                    .asSubclass(TridentPlugin.class)
                                    .getDeclaredConstructor(File.class, PluginDescription.class, PluginClassLoader.class);
                            defaultConstructor.setAccessible(true);
                            plugin = defaultConstructor.newInstance(pluginFile, description, loader);

                            plugins.add(plugin);
                            PLUGIN_EXECUTOR_FACTORY.set(executor, plugin);

                            plugin.startup(executor);
                            plugin.onLoad();

                            for (Class<?> cls : loader.locallyLoaded.values()) {
                                register(plugin, cls, executor);
                            }

                            plugin.onEnable();
                            TridentLogger.success("Loaded " + description.name() + " version " + description.version());
                        }
                    }

                } catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException
                        | InstantiationException | ClassNotFoundException ex) {
                    TridentLogger.error(new PluginLoadException(ex));
                    if (plugin != null) {
                        disable(plugin);
                    }
                }
            }
        });
    }

    private void register(TridentPlugin plugin, Class<?> cls, TaskExecutor executor) throws InstantiationException {
        if (!Modifier.isAbstract(cls.getModifiers())) {

            try {
                Object instance = null;
                Constructor<?> constr = null;
                if (Listener.class.isAssignableFrom(cls) && !cls.isAnnotationPresent(IgnoreRegistration.class)) {
                    constr = cls.getConstructor();
                    instance = constr.newInstance();
                    Trident.eventHandler().registerListener(plugin, executor, (Listener) instance);
                }

                if (Command.class.isAssignableFrom(cls)) {
                    if (constr == null) {
                        constr = cls.getConstructor();
                    }
                    Trident.commandHandler().addCommand(plugin, executor, (Command) ((instance == null) ? constr.newInstance() : instance));
                }
            } catch (NoSuchMethodException e) {
                TridentLogger.error(new PluginLoadException("A no-arg constructor for class " + cls.getName() + " does not exist"));
            } catch (IllegalAccessException e) {
                TridentLogger.error(new PluginLoadException("A no-arg constructor for class " + cls.getName() + " is not accessible"));
            } catch (InvocationTargetException e) {
                TridentLogger.error(e);
            }
        }
    }

    /**
     * Disables the plugin unloading the classes and removing the subprocess
     *
     * @param plugin the plugin to disable
     */
    public void disable(final TridentPlugin plugin) {
        plugin.executor().addTask(new Runnable() {
            @Override
            public void run() {
                // Perform disabling first, we don't want to unload everything
                // then disable it
                // State checking could be performed which breaks the class loader
                plugin.onDisable();

                PLUGIN_EXECUTOR_FACTORY.removeAssignment(plugin);
                plugins.remove(plugin);

                plugin.classLoader.unloadClasses();
                plugin.classLoader = null;
            }
        });
    }

    /**
     * Obtains a list of plugins that are currently <strong>loaded</strong>
     * (not the plugins that are inside the plugin directory)
     *
     * @return the collection of plugins that are loaded
     */
    public List<TridentPlugin> plugins() {
        return Collections.unmodifiableList(this.plugins);
    }
}