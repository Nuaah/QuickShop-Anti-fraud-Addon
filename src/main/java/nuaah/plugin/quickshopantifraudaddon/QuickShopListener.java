package nuaah.plugin.quickshopantifraudaddon;

import com.ghostchu.quickshop.api.event.management.ShopCreateEvent;
import nuaah.plugin.quickshopantifraudaddon.quickShopAntiFraudAddon.QuickShopAntiFraudAddon;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class QuickShopListener implements Listener {

    private final ItemNameChecker checker;
    private final QuickShopAntiFraudAddon plugin;

    public QuickShopListener(LangManager langManager, List<String> langOrder,QuickShopAntiFraudAddon plugin) {
        this.checker = new ItemNameChecker(langManager,langOrder);
        this.plugin = plugin;
    }

    @EventHandler
    public void onShopCreate(ShopCreateEvent event) {

        event.shop().ifPresent(shop -> {

            ItemStack item = shop.getItem();

            OfflinePlayer owner = Bukkit.getOfflinePlayer(shop.getOwner().getUniqueId());

            if (!owner.isOnline()) return;

            Player player = owner.getPlayer();

            if (checker.isConflict(item)) {
                event.setCancelled(true,plugin.getMsg(player,"existedItem"));
            }
        });
    }
}
