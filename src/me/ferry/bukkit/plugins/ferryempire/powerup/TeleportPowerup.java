/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import java.util.List;
import me.ferry.bukkit.plugins.SafeBlockIterator;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Fernando
 */
public class TeleportPowerup implements PowerupAction
{
	@Override
	public int doAction(Player player, ItemStack item, RodData data, FerryEmpirePlugin plugin, me.ferry.bukkit.plugins.ferryempire.RodBase roBase)
	{
		final List<Block> lastTwoTargetBlocks = SafeBlockIterator.getLastTwoTargetBlocks(plugin,player, 100);
		if (lastTwoTargetBlocks.size() < 2)
		{
			player.sendMessage(ChatColor.DARK_RED + "Empire Rod " + ChatColor.DARK_GREEN + "Teleportation failed!");
			return -2;
		}
		Location loc = lastTwoTargetBlocks.get(0).getLocation();
		Location loc1 = lastTwoTargetBlocks.get(1).getLocation();

		if (loc.getBlock().isEmpty() && loc1.getBlock().isEmpty())
		{
			player.sendMessage(ChatColor.DARK_RED + "Empire Rod " + ChatColor.DARK_GREEN + "Teleportation failed!");
			return -2;
		}
		loc.setPitch(player.getLocation().getPitch());
		loc.setYaw(player.getLocation().getYaw());
		loc.getWorld().playSound(loc.add(0.5, 0.5, 0.5), Sound.ENTITY_ENDERMEN_TELEPORT, 2, 2);

		player.teleport(loc);
		return -1;
	}

	@Override
	public void loadFromConfig(ConfigurationSection from)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void writeToConfig(ConfigurationSection to)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
