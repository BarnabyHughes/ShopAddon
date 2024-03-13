package me.barnaby.shopaddon.runnables;

import me.barnaby.shopaddon.ShopAddon;
import me.barnaby.shopaddon.hook.ShopGUIPlusHook;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.shop.item.ShopItem;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class FluctuationRunnable extends BukkitRunnable {

    Random random = new Random();
    private final ShopAddon shopAddon;
    public FluctuationRunnable(ShopAddon shopAddon) {
        this.shopAddon = shopAddon;
    }


    @Override
    public void run() {
        FileConfiguration config = shopAddon.getConfigManager().getPricesConfig();
        List<ShopItem> shopItemList = new ArrayList<>();
        shopItemList.addAll(ShopGuiPlusApi.getShop("farming").getShopItems());
        shopItemList.addAll(ShopGuiPlusApi.getShop("ores").getShopItems());
        shopItemList.addAll(ShopGuiPlusApi.getShop("drops").getShopItems());



        shopItemList.forEach(shopItem -> {
            double maxPrice = config.getDouble("prices."
                    + shopItem.getShop().getId() + "." + shopItem.getId() + ".maxPrice");

            double minPrice = config.getDouble("prices."
                    + shopItem.getShop().getId() + "." + shopItem.getId() + ".minPrice");
            double newPrice = getRandomDoubleBetween(minPrice, maxPrice);
            shopAddon.getPreviousPrices().put(shopItem.getShop().getId() + "." + shopItem.getId(), shopItem.getSellPrice());
            System.out.println(shopItem.getItem().getType() + " updated to " + newPrice + " from " + shopItem.getSellPrice());
            //shopItem.setBuyPrice(newPrice);
            shopItem.setSellPrice(newPrice);
        });
    }

    private double getRandomDoubleBetween(double min, double max) {
        double range = max - min;
        double scaled = Math.random() * range;
        double shifted = scaled + min;
        // Keeping 1 decimal place
        return Math.round(shifted * 10.0) / 10.0;
    }



}
