package me.barnaby.shopaddon.commands;

import me.barnaby.shopaddon.ShopAddon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ShopAddonCommand implements CommandExecutor {

    private final ShopAddon shopAddon;
    public ShopAddonCommand(ShopAddon shopAddon) {
        this.shopAddon = shopAddon;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (s.equalsIgnoreCase("shopaddon")) {
            shopAddon.getConfigManager().reloadPricesConfig();
        }
        return false;
    }
}
