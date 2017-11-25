/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.FireWorkProjectilePlayer;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Fernando
 */
public class TeleportEnderPowerup implements PowerupAction
{
	@Override
	public int doAction(Player player, ItemStack item, RodData data, FerryEmpirePlugin plugin, me.ferry.bukkit.plugins.ferryempire.RodBase roBase)
	{
		EnderPearl ender = player.launchProjectile(EnderPearl.class);
		ender.setVelocity(ender.getVelocity().multiply(3));
		new FireWorkProjectilePlayer(ender, data, plugin, roBase).start();
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
