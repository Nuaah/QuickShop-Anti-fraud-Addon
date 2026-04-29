package nuaah.plugin.quickshopantifraudaddon;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class LangManager {

    private final Map<String, Map<String, String>> langs = new HashMap<>();

    public LangManager(JavaPlugin plugin) {
        load(plugin, "en_us");
        load(plugin, "ja_jp");
    }

    private void load(JavaPlugin plugin, String lang) {
        try {
            File file = new File(plugin.getDataFolder(), "lang/" + lang + ".json");

            if (!file.exists()) {
                plugin.getLogger().warning("Lang file not found: " + lang);
                return;
            }

            String json = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);

            Map<String, String> map = new Gson().fromJson(json,
                    new TypeToken<Map<String, String>>() {}.getType());

            if (map == null) {
                plugin.getLogger().severe("Lang file broken: " + lang);
                return;
            }

            langs.put(lang, map);

            plugin.getLogger().info("Loaded lang: " + lang + " (" + map.size() + ")");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ★ 複数言語対応版
    public String get(Iterable<String> langOrder, String key) {

        for (String lang : langOrder) {
            Map<String, String> map = langs.get(lang.toLowerCase());
            if (map == null) continue;

            String value = map.get(key);
            if (value != null) {
                return value;
            }
        }

        // fallback
        Map<String, String> en = langs.get("en_us");
        if (en != null) {
            return en.getOrDefault(key, key);
        }

        return key;
    }
}
