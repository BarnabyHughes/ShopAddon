package me.barnaby.shopaddon;

import lombok.Getter;
import me.barnaby.shopaddon.commands.ShopAddonCommand;
import me.barnaby.shopaddon.config.ConfigManager;
import me.barnaby.shopaddon.hook.ShopGUIPlusHook;
import me.barnaby.shopaddon.placeholders.ShopAddonExtension;
import me.barnaby.shopaddon.runnables.FluctuationRunnable;
import me.barnaby.shopaddon.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class ShopAddon extends JavaPlugin {

    @Getter
    private final Map<String, Double> previousPrices = new HashMap<>();

    @Getter
    private final Map<String, Integer> boughtRecently = new HashMap<>();

    @Getter
    private final Map<String, Integer> soldRecently = new HashMap<>();


    @Getter
    private final ConfigManager configManager = new ConfigManager(this);

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new ShopGUIPlusHook(this), this);
        configManager.init();

        this.getCommand("test123").setExecutor(new CommandTest());
        this.getCommand("shopaddon").setExecutor(new ShopAddonCommand(this));


        String time = configManager.getPricesConfig().getString("config.time-period");
        assert time != null;
        long seconds = TimeUtil.parseInterval(time);
        new FluctuationRunnable(this).runTaskTimer(this, 100, seconds * 20);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ShopAddonExtension(this).register();
        }
    }

    @Override
    public void onDisable() {



    }
}