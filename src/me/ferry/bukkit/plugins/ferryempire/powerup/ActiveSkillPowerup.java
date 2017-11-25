/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import me.ferry.bukkit.plugins.ferryempire.Skill;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Fernando
 */
public abstract class ActiveSkillPowerup implements ActivePowerupAction
{
	private int time;

	public ActiveSkillPowerup(int time)
	{
		this.time = time;
	}

	@Override
	public int doAction(Player player, ItemStack item, RodData data, FerryEmpirePlugin plugin, me.ferry.bukkit.plugins.ferryempire.RodBase rodBase)
	{
		if(data.getActiveSkill() != null)
		{
			player.sendMessage(ChatColor.DARK_RED + rodBase.getRodName() + ChatColor.DARK_GREEN + " wait until your active skill runs out!");
			return -2;
		}
		if(player.hasMetadata("ActiveSkill"))
		{
			player.sendMessage(ChatColor.DARK_RED + rodBase.getRodName() + ChatColor.DARK_GREEN + " wait until your active skill runs out!");
			return -2;
		}
		data.setActiveSkill(this.getSkill());
		data.setActiveSkillTime(time);
		return 30;
	}

	protected abstract Skill getSkill();


	@Override
	public void loadFromConfig(ConfigurationSection from)
	{
		time = from.getInt("Time",time);
	}

	@Override
	public void writeToConfig(ConfigurationSection to)
	{
		to.set("Time", time);
	}
}
