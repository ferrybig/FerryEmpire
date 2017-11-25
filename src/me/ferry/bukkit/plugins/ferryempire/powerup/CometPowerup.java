/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;

/**
 *
 * @author Fernando
 */
public class CometPowerup extends InstantSpellPowerup
{

	public CometPowerup()
	{
		super(SmallFireball.class, true);
	}

	/**
	 *
	 *
	 * @param loc the value of loc
	 * @param player the value of player
	 * @param plugin the value of plugin
	 * @param rodData the value of rodData
	 */
	
	
	@Override
	public void onExplode(Location loc, Player player, FerryEmpirePlugin plugin, RodData rodData, RodBase rodbase)
	{
		loc.getWorld().createExplosion(loc, 4.6f, true);
	}

	
	
}
