/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.EfectGenerator;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author Fernando
 */
public class TntTrailPowerup extends TrailPowerup
{
	private Location tmp = new Location(null, 0, 0, 0);

	/**
	 *
	 *
	 * @param loc the value of loc
	 * @param player the value of player
	 * @param plugin the value of plugin
	 * @param effect the value of effect
	 * @param rodbase the value of rodbase
	 */
	
	@Override
	public void runTrail(Location loc, Player player, FerryEmpirePlugin plugin, EfectGenerator effect, RodBase rodbase)
	{
		if (player.getWorld() != loc.getWorld())
		{
			return;
		}
		if (player.getLocation(tmp).distanceSquared(loc) > 5 * 5)
		{
			loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 2.5f, false, true);
			loc.getWorld().playEffect(loc, Effect.EXTINGUISH, 1);
		}
		try
		{
			plugin.getfPlayer().playFirework(loc.getWorld(), loc, effect.getNewEffect(rodbase));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
