package me.barnaby.shopaddon.config;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ConfigManager {

    private final JavaPlugin plugin;
    @Getter
    private FileConfiguration pricesConfig;
    private File pricesFile;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void init() {
        // Create the plugin data folder if it doesn't exist
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        // Initialize points.yml
        pricesFile = new File(plugin.getDataFolder(), "prices.yml");
        pricesConfig = YamlConfiguration.loadConfiguration(pricesFile);

        // If points.yml doesn't exist, create it by copying it from the resources
        if (!pricesFile.exists()) {
            copyDefaultConfig("prices.yml");
        }
    }

    public void savePricesConfig() {
        try {
            pricesConfig.save(pricesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadPricesConfig() {
        pricesConfig = YamlConfiguration.loadConfiguration(pricesFile);
    }

    private void copyDefaultConfig(String fileName) {
        try (InputStream inputStream = plugin.getResource(fileName)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + fileName);
            }

            Files.copy(inputStream, pricesFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}