/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 *
 * @author Fernando
 */
public class RechargePowerup implements PowerupAction
{
	@Override
	public int doAction(Player player, ItemStack item1, RodData data, FerryEmpirePlugin plugin, me.ferry.bukkit.plugins.ferryempire.RodBase roBase)
	{
		if (data.getDurability() > 1000 || !roBase.hasDurability())
		{
			player.sendMessage(ChatColor.DARK_RED + roBase.getRodName() + ChatColor.DARK_GREEN + " Recharge failed!");
			return -2;
		}
		int diamonds = 0;
		final PlayerInventory inventory = player.getInventory();
		if (inventory.containsAtLeast(new ItemStack(Material.DIAMOND), 1))
		{
			for (ItemStack item : inventory.getContents())
			{
				if (item == null)
				{
					continue;
				}
				if (item.getType() == Material.DIAMOND)
				{
					diamonds += item.getAmount();
				}
			}
			if (diamonds > 10)
			{
				diamonds = 10;
			}
			int rechargeAmount = diamonds;
			data.setDurability(diamonds * 100 + data.getDurability());
			int size = inventory.getSize();
			for (int i = 0; i < size; i++)
			{
				ItemStack item = inventory.getItem(i);
				if (item == null)
				{
					continue;
				}
				if (item.getType() != Material.DIAMOND)
				{
					continue;
				}
				if (item.getAmount() > diamonds)
				{
					item.setAmount(item.getAmount() - diamonds);
				}
				else if (item.getAmount() == diamonds)
				{
					item = null;
					diamonds = 0;
				}
				else
				{
					diamonds -= item.getAmount();
					item = null;
				}
				inventory.setItem(i, item);
				if (diamonds <= 0)
				{
					break;
				}

			}
			player.sendMessage(
				ChatColor.DARK_RED + roBase.getRodName() + ChatColor.DARK_GREEN + " Recharging using " + ChatColor.DARK_RED + rechargeAmount + ChatColor.DARK_GREEN + " diamonds!");
			return rechargeAmount * 10;
		}
		else
		{
			player.sendMessage(ChatColor.DARK_RED + roBase.getRodName() + ChatColor.DARK_GREEN + " Recharge failed!");
			return -2;
		}
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
