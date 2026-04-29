package nuaah.plugin.quickshopantifraudaddon;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemNameChecker {

    private final LangManager langManager;
    private final List<String> langOrder;

    public ItemNameChecker(LangManager langManager, List<String> langOrder) {
        this.langManager = langManager;
        this.langOrder = langOrder;
    }

    public boolean isConflict(ItemStack item) {

        if (item == null || !item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName()) return false;

        String target = normalize(meta.getDisplayName());

        for (Material material : Material.values()) {
            if (!material.isItem()) continue;

            String key = material.translationKey();
            String name = langManager.get(langOrder, key);

            String normalizedName = normalize(name);

            //自分のアイテムと同じMaterialならスキップ
            if (material == item.getType() && normalizedName.equals(target)) {
                continue;
            }

            if (normalize(name).equals(target)) {
                return true;
            }
        }

        return false;
    }

    private String normalize(String s) {
        return ChatColor.stripColor(s).trim();
    }
}
