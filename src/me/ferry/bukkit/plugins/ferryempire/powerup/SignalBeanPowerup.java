/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import me.ferry.bukkit.plugins.ferryempire.Skill;
import org.bukkit.Effect;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 *
 * @author Fernando
 */
public class SignalBeanPowerup extends ActiveSkillPowerup
{

	public SignalBeanPowerup()
	{
		super(2);
	}

	@Override
	protected Skill getSkill()
	{
		return Skill.SIGNAL_BEAM;
	}

	@Override
	public int doInteractiveAction(Player player, int remainingPower, FerryEmpirePlugin plugin, RodBase rodBase, RodData data)
	{
		player.getWorld().playEffect(player.getEyeLocation(), Effect.CLICK2, null);
		return 1;
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
