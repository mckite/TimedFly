package com.timedfly.NMS.v_1_8;

import com.timedfly.NMS.NMS;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class v_1_8_R1 implements NMS {

    @Override
    public void sendActionbar(Player p, String message) {
        IChatBaseComponent chatBaseComponent = ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(chatBaseComponent, (byte) 2);

        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(bar);
    }

    @Override
    public void sendTitle(Player player, String text, int fadeIn, int stay, int fadeOut) {
        IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + text + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

        PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn, stay, fadeOut);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
    }

    @Override
    public void sendSubtitle(Player player, String text, int fadeIn, int stay, int fadeOut) {
        IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + text + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

        PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, null);
        PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn, stay, fadeOut);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitle);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
    }
}