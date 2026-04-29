package nuaah.plugin.quickshopantifraudaddon.quickShopAntiFraudAddon;

import nuaah.plugin.quickshopantifraudaddon.LangManager;
import nuaah.plugin.quickshopantifraudaddon.QuickShopListener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public final class QuickShopAntiFraudAddon extends JavaPlugin {

    private static QuickShopAntiFraudAddon instance;
    private LangManager langManager;
    private FileConfiguration langConfig; //翻訳ファイル
    private List<String> langOrder; //判定対象ファイル

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        saveResource("lang/en_us.json", false);
        saveResource("lang/ja_jp.json", false);

        langManager = new LangManager(this);

        langOrder = getConfig().getStringList("detectlangs");

        if (langOrder.isEmpty()) {
            langOrder = List.of("en_us");
        }

        Bukkit.getPluginManager().registerEvents(new QuickShopListener(langManager,langOrder,this), this);

        File file = new File(getDataFolder(), "lang.yml");

        if (!file.exists()) {
            saveResource("lang.yml", false);
        }

        loadLang();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static QuickShopAntiFraudAddon getInstance() {
        return instance;
    }

    public void loadLang() {
        File file = new File(getDataFolder(), "lang.yml");

        if (!file.exists()) {
            saveResource("lang.yml", false);
        }

        langConfig = YamlConfiguration.loadConfiguration(file);
    }

    public String getMsg(CommandSender sender, String key) {

        String lang = "en"; // デフォルト

        if (sender instanceof Player player) {
            String locale = player.locale().toString();

            if (locale.startsWith("ja")) lang = "ja";
            else lang = "en";
        }

        return langConfig.getString(lang + "." + key);
    }
}
