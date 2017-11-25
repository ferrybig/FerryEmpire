/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire;

import java.util.regex.Pattern;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.EfectGenerator;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Fernando
 */
public abstract class RodBase implements Listener {

	public abstract Recipe[] getRecipes();

	public abstract Skill[] getSkills();

	public abstract ItemStack getRod();

	public abstract int getRodMagicId();

	public abstract boolean maySpawnWithCommand();

	public abstract EfectGenerator getFireworkOverride(Skill skill);

	public boolean isRod(ItemStack item) {
		if (item == null) {
			return false;
		}
		ItemMeta meta = item.getItemMeta();
		if (meta == null) {
			return false;
		}
		if (meta.hasDisplayName()) {
			if (meta.getDisplayName().startsWith(this.getRodMagic())) {
				return true;
			}
		}
		return false;
	}

	public abstract String getRodName();

	public abstract String getRodColor();

	public RodData getRodData(ItemMeta meta) {
		Skill skill = Skill.LIGHTNING;
		int power = 100;
		int cooldown = 20;
		int durability = 500;
		int activeSkillTime = 0;
		Skill activeSkill = null;
		for (String str : meta.getLore()) {
			str = ChatColor.stripColor(str);
			String split[] = str.split(Pattern.quote(" "), 2);
			if (split.length == 2) {
				if (split[0].equals("Power")) {
					try {
						int power1 = Integer.parseInt(split[1]);
						if (power1 < 0) {
							power = 0;
						} else {
							power = power1;
						}
					} catch (NumberFormatException ex) {
					}
				}
				if (split[0].equals("Cooldown")) {
					try {
						int cooldown1 = Integer.parseInt(split[1]);
						if (cooldown1 < 0) {
							cooldown = 0;
						} else {
							cooldown = cooldown1;
						}
					} catch (NumberFormatException ex) {
					}
				} else if (split[0].equals("Action")) {
					try {
						Skill skill1 = Skill.valueOf(split[1].toUpperCase().replace(" ", "_"));
						if (skill1 != null) {
							skill = skill1;
						}
					} catch (IllegalArgumentException ex) {
					}
				} else if (split[0].equals("ActiveSkill")) {
					try {
						Skill activeSkill1 = Skill.valueOf(split[1].toUpperCase().replace(" ", "_"));
						if (activeSkill1 != null) {
							activeSkill = activeSkill1;
						}
					} catch (IllegalArgumentException ex) {
					}
				} else if (split[0].equals("Durability")) {
					try {
						int durability1 = Integer.parseInt(split[1]);
						if (durability1 < 0) {
							durability = 0;
						} else {
							durability = durability1;
						}
					} catch (NumberFormatException ex) {
					}
				} else if (split[0].equals("ActiveSkillTime")) {
					try {
						int skillTime1 = Integer.parseInt(split[1]);
						if (skillTime1 > 2000) {
							activeSkillTime = 2000;
						} else if (skillTime1 < 0) {
							activeSkillTime = 0;
						} else {
							activeSkillTime = skillTime1;
						}
					} catch (NumberFormatException ex) {
					}
				}
			}
		}
		return new RodData(skill, power, durability, cooldown, activeSkill, activeSkillTime);
	}

	public abstract String getRodMagic();

	public abstract boolean hasCooldown();

	public abstract boolean hasPower();

	public abstract boolean hasDurability();

	public abstract int maxPower();

	public abstract int maxDurability();

	public String getRodNameItem(RodData rodData) {
		StringBuilder build = new StringBuilder();
		build.append(this.getRodMagic()).append(this.getRodColor()).append(this.getRodName());
		build.append(": ");
		if (this.getSkills().length > 2) {
			build.append(ChatColor.DARK_RED).append("Skill: ").append(ChatColor.DARK_GREEN).append(rodData.getSkill().name().replace("_", " ").toLowerCase()).append(' ');
		}
		if (this.hasPower()) {
			build.append(ChatColor.DARK_RED).append("Power: ").append(ChatColor.DARK_GREEN).append(rodData.getPower() >= 100 ? "Max" : rodData.getPower()).append(' ');
		}
		return build.toString();
	}
}
