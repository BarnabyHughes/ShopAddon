package me.barnaby.shopaddon.placeholders;

import me.barnaby.shopaddon.ShopAddon;
import me.barnaby.shopaddon.config.ConfigManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.shop.Shop;
import net.brcdev.shopgui.shop.item.ShopItem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShopAddonExtension extends PlaceholderExpansion {

    private ShopAddon shopAddon;
    public ShopAddonExtension(ShopAddon shopAddon) {
        this.shopAddon = shopAddon;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "shopaddon";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Barnaby";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return "";
        String[] parameters = params.split("_");
        if (parameters.length!=3) return "IncorrectUsage";
        String shopId = parameters[1];
        String itemId = parameters[2];

        Shop shop = ShopGuiPlusApi.getShop(shopId);
        if (shop == null) return "ShopNotFound";

        ShopItem shopItem = shop.getShopItem(itemId);
        if (shopItem == null) return "ItemNotFound";

        if (parameters[0].equals("fluctuations")) {
            double prevPrice = shopAddon.getPreviousPrices().get(shopId + "." + itemId);
            double currentPrice = shopItem.getSellPrice();

            if (prevPrice == 0) return "Error";
            int percentageDifference = (int) Math.round(((currentPrice - prevPrice) / prevPrice) * 100);
            String returnMessage = "";
            if (percentageDifference >= 0)
                returnMessage = shopAddon.getConfigManager().getPricesConfig().getString("config.fluctuation-gone-up");
            else
                returnMessage = shopAddon.getConfigManager().getPricesConfig().getString("config.fluctuation-gone-down");

            System.out.println(prevPrice + "," + currentPrice + ", " + percentageDifference);

            return ChatColor.translateAlternateColorCodes('&', returnMessage.replace("{percent}",
                    String.valueOf(Math.abs(percentageDifference))));
        }
        if (parameters[0].equals("soldrecently")) {
            return String.valueOf(shopAddon.getSoldRecently().getOrDefault(shopId + "." + itemId, 0));
        }
        if (parameters[0].equals("boughtrecently")) {
            return String.valueOf(shopAddon.getBoughtRecently().getOrDefault(shopId + "." + itemId, 0));
        }
        if (parameters[0].equals("buyprice")) {
            return String.valueOf(ShopGuiPlusApi.getShop(shopId).getShopItem(itemId).getBuyPrice());
        }
        if (parameters[0].equals("sellprice")) {
            return String.valueOf(ShopGuiPlusApi.getShop(shopId).getShopItem(itemId).getSellPrice());
        }
        if (parameters[0].equals("baseprice")) {
            return String.valueOf(shopAddon.getConfigManager().getPricesConfig().getDouble("prices." + shopId + "." + itemId + ".minPrice"));
        }
        return null;
    }
}
