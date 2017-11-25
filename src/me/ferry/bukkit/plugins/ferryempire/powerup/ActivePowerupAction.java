/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import org.bukkit.entity.Player;

/**
 *
 * @author Fernando
 */
public interface ActivePowerupAction extends PowerupAction
{
	/**
	 *
	 *
	 * @param player the value of player
	 * @param remainingPower the value of remainingPower
	 * @param plugin the value of plugin
	 * @param rodBase the value of rodBase
	 * @param data the value of data
	 * @return the int
	 */
	
	public abstract int doInteractiveAction(Player player, int remainingPower, FerryEmpirePlugin plugin, RodBase rodBase, RodData data);
}
