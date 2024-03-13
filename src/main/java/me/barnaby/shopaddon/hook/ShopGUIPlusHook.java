package me.barnaby.shopaddon.hook;

import com.bgsoftware.wildchests.api.WildChestsAPI;
import com.bgsoftware.wildchests.api.events.SellChestTaskEvent;
import me.barnaby.shopaddon.ShopAddon;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.economy.EconomyType;
import net.brcdev.shopgui.event.ShopGUIPlusPostEnableEvent;
import net.brcdev.shopgui.event.ShopPostTransactionEvent;
import net.brcdev.shopgui.shop.Shop;
import net.brcdev.shopgui.shop.ShopManager;
import net.brcdev.shopgui.shop.ShopTransactionResult;
import net.brcdev.shopgui.shop.item.ShopItem;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ShopGUIPlusHook implements Listener {

    private final ShopAddon shopAddon;

    public ShopGUIPlusHook(ShopAddon shopAddon) {
        this.shopAddon = shopAddon;
    }

    @EventHandler
    public void onShopGUIPlusPostEnable(ShopGUIPlusPostEnableEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                FileConfiguration config = shopAddon.getConfigManager().getPricesConfig();

                List<ShopItem> shopItemList = new ArrayList<>();
                shopItemList.addAll(ShopGuiPlusApi.getShop("farming").getShopItems());
                shopItemList.addAll(ShopGuiPlusApi.getShop("ores").getShopItems());
                shopItemList.addAll(ShopGuiPlusApi.getShop("drops").getShopItems());

                shopItemList.forEach(shopItem -> {
                    String shopId = shopItem.getShop().getId();
                    String itemId = shopItem.getId();
                    if (!config.contains("prices." + shopId + "." + itemId + ".minPrice"))
                        config.set("prices." + shopId + "." + itemId + ".minPrice", shopItem.getSellPrice() * 0.5);

                    if (!config.contains("prices." + shopId + "." + itemId + ".maxPrice"))
                        config.set("prices." + shopId + "." + itemId + ".maxPrice", shopItem.getSellPrice() * 2);
                });
                shopAddon.getConfigManager().savePricesConfig();
            }

        }.runTaskLater(shopAddon, 20);
    }

    @EventHandler
    public void buyItem(ShopPostTransactionEvent event) {
        if (event.getResult().getResult() == ShopTransactionResult.ShopTransactionResultType.SUCCESS) {
            ShopItem item = event.getResult().getShopItem();
            String key = item.getShop().getId() + "." + item.getId();
            if (event.getResult().getShopAction() == ShopManager.ShopAction.BUY) {
                int i = shopAddon.getBoughtRecently().getOrDefault(key, 0);
                shopAddon.getBoughtRecently().put(key, i + event.getResult().getAmount());
            }
            if (event.getResult().getShopAction() == ShopManager.ShopAction.SELL) {
                int i = shopAddon.getSoldRecently().getOrDefault(key, 0);
                shopAddon.getSoldRecently().put(key, i + event.getResult().getAmount());
            }
        }

    }

    @EventHandler
    public void onSellChest(SellChestTaskEvent event) {
        ItemStack itemStack = event.getItem();
        ShopItem item = ShopGuiPlusApi.getItemStackShopItem(itemStack);

        String key = item.getShop().getId() + "." + item.getId();

        int i = shopAddon.getSoldRecently().getOrDefault(key, 0);
        shopAddon.getSoldRecently().put(key, i+itemStack.getAmount());
    }
}