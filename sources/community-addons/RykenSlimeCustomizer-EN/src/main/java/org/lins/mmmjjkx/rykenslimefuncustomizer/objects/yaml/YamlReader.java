package org.lins.mmmjjkx.rykenslimefuncustomizer.objects.yaml;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import io.papermc.lib.PaperLib;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.rykenslimefuncustomizer.objects.ProjectAddon;
import org.lins.mmmjjkx.rykenslimefuncustomizer.objects.customs.CustomAddonConfig;
import org.lins.mmmjjkx.rykenslimefuncustomizer.utils.ExceptionHandler;

public abstract class YamlReader<T> {
    public static final int MAJOR_VERSION = PaperLib.getMinecraftVersion();
    public static final int MINOR_VERSION = PaperLib.getMinecraftPatchVersion();
    private final List<String> lateInits;
    protected final ProjectAddon addon;
    protected final YamlConfiguration configuration;

    public YamlReader(YamlConfiguration config, ProjectAddon addon) {
        this.configuration = config;
        this.lateInits = new ArrayList<>();
        this.addon = addon;
    }

    public abstract T readEach(String section);

    public final void preload() {
        for (String key : configuration.getKeys(false)) {
            ConfigurationSection section = configuration.getConfigurationSection(key);
            if (section == null) continue;
            ConfigurationSection register = section.getConfigurationSection("register");
            if (!checkForRegistration(key, register)) continue;

            List<SlimefunItemStack> items = preloadItems(key);

            if (items == null || items.isEmpty()) continue;

            for (SlimefunItemStack item : items) {
                addon.getPreloadItems().put(item.getItemId(), item);
                ExceptionHandler.debugLog("&aPreloaded item: " + item.getItemId());
            }
        }
    }

    @Nullable protected final SlimefunItemStack getPreloadItem(String itemId) {
        return addon.getPreloadItems().get(itemId);
    }

    public final List<T> readAll() {
        List<T> objects = new ArrayList<>();
        for (String key : configuration.getKeys(false)) {
            ConfigurationSection section = configuration.getConfigurationSection(key);
            if (section == null) continue;

            ExceptionHandler.debugLog("Starting to read item: " + key);

            ConfigurationSection register = section.getConfigurationSection("register");
            if (!checkForRegistration(key, register)) continue;

            ExceptionHandler.debugLog("Checking for late initialization...");

            if (section.getBoolean("lateInit", false)) {
                putLateInit(key);
                ExceptionHandler.debugLog("Check result: no late initialization");
                continue;
            }

            ExceptionHandler.debugLog("Starting to read...");

            var object = readEach(key);
            if (object != null) {
                objects.add(object);
                ExceptionHandler.debugLog("&aSUCCESS | Item " + key + " read successfully!");
            } else {
                ExceptionHandler.debugLog("&cFAILURE | Item " + key + " read failed!");
            }
        }
        return objects;
    }

    protected void putLateInit(String key) {
        lateInits.add(key);
    }

    public List<T> loadLateInits() {
        List<T> objects = new ArrayList<>();
        lateInits.forEach(key -> {
            ExceptionHandler.debugLog("Starting to read late initialization item: " + key);
            var object = readEach(key);
            if (object != null) {
                objects.add(object);
                ExceptionHandler.debugLog("&aSUCCESS | Item " + key + " read successfully!");
            } else {
                ExceptionHandler.debugLog("&cFAILURE | Item " + key + " read failed!");
            }
        });

        lateInits.clear();

        return objects;
    }

    public abstract List<SlimefunItemStack> preloadItems(String s);

    private boolean checkForRegistration(String key, ConfigurationSection section) {
        if (section == null) return true;

        List<String> conditions = section.getStringList("conditions");
        boolean warn = section.getBoolean("warn", false);
        boolean unfinished = section.getBoolean("unfinished", false);

        if (unfinished) return false;

        for (String condition : conditions) {
            String[] splits = condition.split(" ");
            String head = splits[0];
            if (head.equalsIgnoreCase("hasplugin")) {
                if (splits.length != 2) {
                    ExceptionHandler.handleError("Issue found while reading registration condition for " + key + ": hasplugin requires only one parameter");
                    continue;
                }
                boolean b = Bukkit.getPluginManager().isPluginEnabled(splits[1]);
                if (!b) {
                    if (warn) {
                        ExceptionHandler.handleError(key + " requires server plugin " + splits[1] + " to be registered");
                    }
                    return false;
                }
            } else if (head.equalsIgnoreCase("!hasplugin")) {
                if (splits.length != 2) {
                    ExceptionHandler.handleError("Issue found while reading registration condition for " + key + ": !hasplugin requires only one parameter");
                    continue;
                }
                boolean b = Bukkit.getPluginManager().isPluginEnabled(splits[1]);
                if (b) {
                    if (warn) {
                        ExceptionHandler.handleError(key + " requires server plugin " + splits[1] + " to be uninstalled to be registered (possible conflict?)");
                    }
                    return false;
                }
            } else if (head.equalsIgnoreCase("version")) {
                if (splits.length != 3) {
                    ExceptionHandler.handleError("Issue found while reading registration condition for " + key + ": version requires two parameters");
                    continue;
                }

                int targetMajor = 0;
                int targetMinor = 0;
                String[] versionSplit = splits[2].split("\\.");
                if (versionSplit.length == 2) {
                    try {
                        targetMajor = Integer.parseInt(versionSplit[1]);
                    } catch (NumberFormatException e) {
                        ExceptionHandler.handleError("Issue found while reading registration condition for " + key + ": version number " + splits[2] + " is not a valid version number!");
                        continue;
                    }
                } else if (versionSplit.length == 3) {
                    try {
                        targetMajor = Integer.parseInt(versionSplit[1]);
                        targetMinor = Integer.parseInt(versionSplit[2]);
                    } catch (NumberFormatException e) {
                        ExceptionHandler.handleError("Issue found while reading registration condition for " + key + ": version number " + splits[2] + " is not a valid version number!");
                        continue;
                    }
                } else {
                    ExceptionHandler.handleError("Issue found while reading registration condition for " + key + ": version number " + splits[2] + " is not a valid version number!");
                }

                // ExceptionHandler.info("key: " + key + " condition: " + condition + " major: " + targetMajor + "
                // minor: " + targetMinor);
                boolean pass = false;
                switch (splits[1]) {
                    case ">" -> {
                        if (MAJOR_VERSION > targetMajor
                                || (MAJOR_VERSION == targetMajor && MINOR_VERSION > targetMinor)) {
                            pass = true;
                        }
                    }
                    case "<" -> {
                        if (MAJOR_VERSION < targetMajor
                                || (MAJOR_VERSION == targetMajor && MINOR_VERSION < targetMinor)) {
                            pass = true;
                        }
                    }
                    case ">=" -> {
                        if (MAJOR_VERSION > targetMajor
                                || (MAJOR_VERSION == targetMajor && MINOR_VERSION >= targetMinor)) {
                            pass = true;
                        }
                    }
                    case "<=" -> {
                        if (MAJOR_VERSION < targetMajor
                                || (MAJOR_VERSION == targetMajor && MINOR_VERSION <= targetMinor)) {
                            pass = true;
                        }
                    }
                    case "==" -> {
                        if (MAJOR_VERSION == targetMajor && MINOR_VERSION == targetMinor) {
                            pass = true;
                        }
                    }
                    case "!=" -> {
                        if (MAJOR_VERSION != targetMajor || MINOR_VERSION != targetMinor) {
                            pass = true;
                        }
                    }
                    default -> {
                        ExceptionHandler.handleError("Issue found while reading registration condition for " + key + ": version requires a valid comparison operator!");
                        pass = true;
                    }
                }
                if (!pass) {
                    if (warn) {
                        ExceptionHandler.handleError(key + " requires server version " + splits[1] + " " + splits[2] + " to be registered");
                    }
                    return false;
                }
            } else if (head.contains("config")) {
                CustomAddonConfig config = addon.getConfig();
                if (config == null) {
                    ExceptionHandler.handleError("Issue found while reading registration condition for " + key + ": unable to get configuration");
                    continue;
                }

                switch (head) {
                    case "config.boolean" -> {
                        if (splits.length != 2) {
                            ExceptionHandler.handleError("Issue found while reading registration condition for " + key + ": config.boolean requires one parameter");
                            continue;
                        }

                        if (!config.config().getBoolean(splits[1])) {
                            if (warn) {
                                ExceptionHandler.handleError(key + " requires configuration option " + splits[1] + " to be true to be registered");
                            }
                            return false;
                        }
                    }
                    case "config.string" -> {
                        if (splits.length != 3) {
                            ExceptionHandler.handleError("Issue found while reading registration condition for " + key + ": config.string requires two parameters");
                            continue;
                        }

                        if (!Objects.equals(config.config().getString(splits[1]), splits[2])) {
                            if (warn) {
                                ExceptionHandler.handleError(key + " requires configuration option " + splits[1] + " to be " + splits[2] + " to be registered");
                            }
                            return false;
                        }
                    }
                    case "config.int" -> {
                        if (splits.length != 4) {
                            ExceptionHandler.handleError("Issue found while reading registration condition for " + key + ": config.int requires three parameters");
                            continue;
                        }

                        String configKey = splits[1];
                        int current = config.config().getInt(splits[2]);
                        int destination = Integer.parseInt(splits[3]);

                        if (!intCheck(
                                splits[1],
                                key,
                                "config.int",
                                current,
                                destination,
                                (op) -> "Needs the config value of " + configKey + op + splits[3] + ", so that it can be registered",
                                warn)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean intCheck(
            String operator,
            String key,
            String regParam,
            int current,
            int destination,
            Function<String, String> msg,
            boolean warn) {
        String operation = "";
      
        boolean b = 
          switch (operator) {
            case ">" -> {
                operation = "greater than";
                yield current > destination;
            }
            case "<" -> {
                operation = "less than";
                yield current < destination;
            }
            case ">=" -> {
                operation = "greater than or equal to";
                yield current >= destination;
            }
            case "<=" -> {
                operation = "less than or equal to";
                yield current <= destination;
            }
            case "==" -> {
                operation = "equal to";
                yield current == destination;
            }
            case "!=" -> {
                operation = "not equal to";
                yield current != destination;
            }
            default -> {
                ExceptionHandler.handleError("Issue found while reading registration condition for " + key + ": " + regParam + " requires a valid comparison operator!");
                yield true;
            }
        };

        if (!b) {
            if (warn) {
                ExceptionHandler.handleError(key + msg.apply(operation));
            }
        }

        return b;
    }
}
