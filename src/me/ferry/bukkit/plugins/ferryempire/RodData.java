/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;

/**
 *
 * @author Fernando
 */
public class RodData {

	private Skill skill;
	private int power;
	private int durability;
	private int cooldown;
	private Skill activeSkill;
	private int activeSkillTime;

	public RodData(Skill skill, int power, int durability, int cooldown, Skill activeSkill, int activeSkillTime) {
		this.skill = skill;
		this.power = power;
		this.durability = durability;
		this.cooldown = cooldown;
		this.activeSkill = activeSkill;
		this.activeSkillTime = activeSkillTime;
	}

	public Skill getSkill() {
		return skill;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getDurability() {
		return durability;
	}

	public void setDurability(int durability) {
		this.durability = durability;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public List<String> toLore(RodBase base) {
		List<String> lore = new ArrayList<String>(15);
		lore.add(ChatColor.DARK_BLUE + "/------" + ChatColor.DARK_PURPLE + "--------" + ChatColor.DARK_BLUE + "-------\\");
		lore.add(ChatColor.DARK_BLUE + "| ----- " + ChatColor.DARK_PURPLE + "Spell info " + ChatColor.DARK_BLUE + "------  |");
		lore.add(ChatColor.DARK_RED + "Action " + ChatColor.DARK_GREEN + skill.name().replace("_", " ").toLowerCase());
		if (base.hasDurability() || base.hasPower()) {
			lore.add(ChatColor.DARK_RED + "Cost " + ChatColor.DARK_GREEN + skill.getCost());
		}
		if (base.hasDurability() || base.hasPower() || base.hasCooldown()) {
			lore.add(ChatColor.DARK_BLUE + "| ----- " + ChatColor.DARK_PURPLE + "Rod info " + ChatColor.DARK_BLUE + "-------  |");
			if (base.hasPower()) {
				lore.add(ChatColor.DARK_RED + "Power " + ChatColor.DARK_GREEN + this.getPower());
			}
			if (base.hasDurability()) {
				lore.add(ChatColor.DARK_RED + "Durability " + ChatColor.DARK_GREEN + this.getDurability());
			}
			if (base.hasCooldown()) {
				lore.add(ChatColor.DARK_RED + "Cooldown " + ChatColor.DARK_GREEN + this.getCooldown());
			}
		}
		lore.add(ChatColor.DARK_BLUE + "| -- " + ChatColor.DARK_PURPLE + "Spell  description " + ChatColor.DARK_BLUE + "--  |");
		for (String line : getSkill().getDescription().split("\n")) {
			lore.add(ChatColor.DARK_PURPLE + line);
		}
		if (this.activeSkill != null) {
			lore.add(ChatColor.DARK_BLUE + "| ----- " + ChatColor.DARK_PURPLE + "Active Spell " + ChatColor.DARK_BLUE + "-----  |");
			lore.add(ChatColor.DARK_RED + "ActiveSkill " + ChatColor.DARK_GREEN + this.getActiveSkill().name().replace("_", " ").toLowerCase());
			lore.add(ChatColor.DARK_RED + "ActiveSkillTime " + ChatColor.DARK_GREEN + this.getActiveSkillTime());
		}
		lore.add(ChatColor.DARK_BLUE + "\\------" + ChatColor.DARK_PURPLE + "--------" + ChatColor.DARK_BLUE + "------/");
		return lore;
	}

	public Skill getActiveSkill() {
		return activeSkill;
	}

	public void setActiveSkill(Skill activeSkill) {
		this.activeSkill = activeSkill;
	}

	public int getActiveSkillTime() {
		return activeSkillTime;
	}

	public void setActiveSkillTime(int activeSkillTime) {
		this.activeSkillTime = activeSkillTime;
	}
}
