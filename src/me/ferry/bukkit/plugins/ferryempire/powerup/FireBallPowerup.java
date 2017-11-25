/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.ferryempire.FireWorkProjectilePlayer;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Fernando
 */
public class FireBallPowerup implements PowerupAction
{
	boolean bigFireBall;

	public FireBallPowerup(boolean bigFireBall)
	{
		this.bigFireBall = bigFireBall;
	}

	/**
	 *
	 *
	 * @param player the value of player
	 * @param item the value of item
	 * @param data the value of data
	 * @param plugin the value of plugin
	 * @param roBase the value of roBase
	 * @return the int
	 */

	@Override
	public int doAction(Player player, ItemStack item, RodData data, me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin plugin, me.ferry.bukkit.plugins.ferryempire.RodBase roBase)
	{
		if (bigFireBall)
		{
			LargeFireball fireball = player.launchProjectile(LargeFireball.class);
			fireball.setVelocity(fireball.getVelocity().multiply(2));
			fireball.setYield(3.5f);
			new FireWorkProjectilePlayer(fireball,data,plugin, roBase).start();
		}
		else
		{
			new FireWorkProjectilePlayer(player.launchProjectile(SmallFireball.class),data,plugin, roBase).start();
		}
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
