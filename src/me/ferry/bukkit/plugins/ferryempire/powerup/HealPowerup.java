/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author Fernando
 */
public class HealPowerup implements PowerupAction
{
	private final int power;

	public HealPowerup(int power)
	{
		this.power = power;
	}

	@Override
	public int doAction(Player player, ItemStack item, RodData data, FerryEmpirePlugin plugin, me.ferry.bukkit.plugins.ferryempire.RodBase roBase)
	{
		plugin.sendMessage(player,ChatColor.DARK_RED + "Empire Rod " + ChatColor.DARK_GREEN + "Healed!");
		player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, power));
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
