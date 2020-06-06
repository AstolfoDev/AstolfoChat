package github.io.AstolfoDev.AstolfoChat;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main extends JavaPlugin implements Listener {

    private File ConfigFile;
    private FileConfiguration Config;

    @Override
    public void onEnable() {
        createConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("Plugin enabled!");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        String block = this.getConfig().getString("block");
        String blockMsg = this.getConfig().getString("block-message");
        String[] bannedWords = this.getConfig().getString("banned-text").split(", ");
        if (block.toLowerCase() == "true") {
            for (int i = 0; i < bannedWords.length; i++) {
                if (message.contains(bannedWords[i])) {
                    event.getPlayer().sendMessage(blockMsg);
                    event.setCancelled(true);
                    return;
                }
            }
        } else {
            String filter = event.getMessage();
            Boolean filterTrue = false;
            for (int i = 0; i < bannedWords.length; i++) {
                if (message.contains(bannedWords[i])) {
                    filter = message.replace(bannedWords[i], "*****");
                    filterTrue = true;
                }
            }
            if (filterTrue == true) {
                event.setMessage(filter);
            }
            return;
        }
    }

    private void createConfig() {
        ConfigFile = new File(getDataFolder(), "config.yml");
        if (!ConfigFile.exists()) {
            ConfigFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        Config = new YamlConfiguration();
        try {
            Config.load(ConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
