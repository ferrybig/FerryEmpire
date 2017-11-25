/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import me.ferry.bukkit.plugins.ferryempire.Skill;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author Fernando
 */
public class DoubleJumpPowerup extends ActiveSkillPowerup
{

	public DoubleJumpPowerup()
	{
		super(120);
	}

	@Override
	protected Skill getSkill()
	{
		return Skill.DOUBLE_JUMP;
	}

	@Override
	public int doInteractiveAction(Player player, int remainingPower, FerryEmpirePlugin plugin, RodBase rodBase, RodData data)
	{
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,40,2,true),true);
		return 1;
	}
	
}
