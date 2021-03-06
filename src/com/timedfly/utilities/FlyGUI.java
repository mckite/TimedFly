package com.timedfly.utilities;

import com.timedfly.configurations.ConfigCache;
import com.timedfly.configurations.ItemsConfig;
import com.timedfly.configurations.Languages;
import com.timedfly.hooks.PlayerPointsHook;
import com.timedfly.hooks.TokenManager;
import com.timedfly.hooks.Vault;
import com.timedfly.managers.HooksManager;
import com.timedfly.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class FlyGUI {

    private ItemsConfig items;
    private Languages languages;
    private TokenManager tokensManager;
    private PlayerPointsHook playerPointsHook;
    private Utilities utility;
    private Inventory inventory;
    private Vault vault;

    public FlyGUI(ItemsConfig items, Languages languages, TokenManager tokensManager, PlayerPointsHook playerPointsHook,
                  Utilities utility, Vault vault) {
        this.items = items;
        this.languages = languages;
        this.tokensManager = tokensManager;
        this.playerPointsHook = playerPointsHook;
        this.utility = utility;
        this.vault = vault;
    }

    public void openGui(Player player) {
        FileConfiguration itemsConfig = items.getItemsConfig();
        FileConfiguration languageConfig = languages.getLanguageFile();

        Inventory inv = Bukkit.createInventory(player, ConfigCache.getGuiSlots(), Message.color(ConfigCache.getGuiDisplayName()));
        ConfigurationSection section = itemsConfig.getConfigurationSection("Items");

        PlayerManager playerManager = utility.getPlayerManager(player.getUniqueId());

        for (String string : section.getKeys(false)) {
            String price = Integer.toString(itemsConfig.getInt("Items." + string + ".Price"));
            String time = Integer.toString(itemsConfig.getInt("Items." + string + ".Time"));
            List<String> lore = new ArrayList<>();
            List<String> stringList = itemsConfig.getStringList("Items." + string + ".Lore");
            ItemStack item = new ItemStack(Material.matchMaterial(itemsConfig.getString("Items." + string + ".Material")),
                    itemsConfig.getInt("Items." + string + ".Amount"), (short) itemsConfig.getInt("Items." + string + ".Data"));
            ItemMeta meta = item.getItemMeta();
            String pformat = NumberFormat.getIntegerInstance().format(Double.valueOf(price));

            for (String lines : stringList) {
                lore.add(HooksManager.setPlaceHolders(lines, player).replace("%time%", time)
                        .replace("%price%", pformat).replace("%timeleft%", playerManager.isTimeEnded() && !playerManager.isTimePaused() ?
                                languageConfig.getString("Format.NoTimeLeft") : TimeFormat.formatLong(playerManager.getTimeLeft()))
                        .replace("%balance%", vault.getMoney(player)).replace("%tokens%", tokensManager.tokens(player))
                        .replace("%points%", playerPointsHook.getPoints(player.getUniqueId())));
            }

            meta.setDisplayName(HooksManager.setPlaceHolders(itemsConfig.getString("Items." + string + ".Name")
                    .replace("%time%", time).replace("%price%", pformat), player));
            meta.setLore(lore);

            if (itemsConfig.getBoolean("Items." + string + ".Hide_Attributes")) {
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            }
            if (itemsConfig.getBoolean("Items." + string + ".Hide_Enchants")) {
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if (itemsConfig.getBoolean("Items." + string + ".Hide_Place_On")) {
                meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
            }
            if (itemsConfig.getBoolean("Items." + string + ".Hide_Potion_Effects")) {
                meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            }
            if (itemsConfig.getBoolean("Items." + string + ".Hide_Unbreakable")) {
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            }
            if (itemsConfig.getBoolean("Items." + string + ".Glow")) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
            }
            item.setItemMeta(meta);
            inv.setItem(itemsConfig.getInt("Items." + string + ".Slot"), item);
        }
        inventory = inv;
        player.openInventory(inv);
    }

    public Inventory getInventory() {
        return inventory;
    }
}
