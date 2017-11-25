/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.SafeBlockIterator;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.EfectGenerator;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Fernando
 */
public abstract class InstantSpellPowerup implements PowerupAction
{
	final Class<? extends Projectile> clazz;
	final boolean showFirework;
	public int maxDistance = 100;
	public int fireworkDistance = 10;

	public InstantSpellPowerup(Class<? extends Projectile> clazz, boolean showFirework)
	{
		this.clazz = clazz;
		this.showFirework = showFirework;
	}

	@Override
	public int doAction(Player player, ItemStack item, RodData data, FerryEmpirePlugin plugin, RodBase rodBase)
	{
		SafeBlockIterator bl = new SafeBlockIterator(player, maxDistance);

		Projectile ball;
		if (this.clazz != null)
		{
			ball = player.launchProjectile(clazz);
		}
		else
		{
			ball = null;
		}
		try
		{
			EfectGenerator effect = data.getSkill().getEffect(rodBase);
			int i = 0;
			while (bl.hasNext())
			{
				Block next = bl.next();
				if (!plugin.getBlockUtil().isSolid(next.getTypeId()))
				{

					i++;
					if (i % fireworkDistance == fireworkDistance - 1)
					{
						if (ball != null)
						{
							ball.teleport(next.getLocation().add(0.5, 0.5, 0.5));
						}
						if (this.showFirework)
						{
							try
							{
								plugin.getfPlayer().playFirework(next.getLocation().getWorld(), next.getLocation().add(0.5, 0.5, 0.5), effect.getNewEffect(rodBase));
							}
							catch (Exception ex)
							{
								ex.printStackTrace();
							}
						}
					}
					if (i >= maxDistance)
					{

						this.onExplode(next.getLocation().add(0.5, 0.5, 0.5), player, plugin, data, rodBase);
						return -1;
					}
				}
				else
				{
					this.onExplode(next.getLocation().add(0.5, 0.5, 0.5), player, plugin, data, rodBase);
					return -1;
				}
			}
		}
		finally
		{
			if (ball != null)
			{
				ball.remove();
			}
		}
		return -1;
	}

	/**
	 *
	 *
	 * @param loc the value of loc
	 * @param player the value of player
	 * @param plugin the value of plugin
	 * @param rodData the value of rodData
	 * @param rodbase the value of rodbase
	 */

	public abstract void onExplode(Location loc, Player player, FerryEmpirePlugin plugin, RodData rodData, RodBase rodbase);


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
