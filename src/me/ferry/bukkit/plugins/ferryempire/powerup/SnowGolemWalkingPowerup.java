/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import me.ferry.bukkit.plugins.ferryempire.Skill;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 *
 * @author Fernando
 */
public class SnowGolemWalkingPowerup extends ActiveSkillPowerup
{

	public SnowGolemWalkingPowerup()
	{
		super(20);
	}

	@Override
	protected Skill getSkill()
	{
		return Skill.SNOW_GOLEM;
	}

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
	
	@Override
	public int doInteractiveAction(Player player, int remainingPower, FerryEmpirePlugin plugin, RodBase rodBase, RodData data)
	{
		Block b = player.getLocation().getBlock();
		if(b.isEmpty())
		{
			b.setType(Material.SNOW);
			return remainingPower;
		}
		return 0;
	}
	public static class Snower
	{
		
	}
	
}