/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.EfectGenerator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Fernando
 */
public final class ConfigureableRod extends RodBase implements ConfigurationSerializable, Comparable<ConfigureableRod> {

	private final ItemStack empireStick;
	private final String itemMagic;
	private final int itemMagicDecoded;
	private final Skill[] skills;
	private final String rodName;
	private final boolean usesPower;
	private final boolean usesDurability;
	private final boolean usesCooldown;
	private final int maxPower;
	private final int maxDurability;
	private final Material material;
	private final ShapedRecipe[] recipes;
	private final String color;
	private final boolean keepEnchantsOnBreak;
	private final boolean maySpawnWithCommand;
	private final short damageValue;
	private final FireworkOverride override;

	@SuppressWarnings("unchecked")
	public ConfigureableRod(Map<String, Object> map) {
		this.rodName = map.get("RodName").toString();
		this.color = ChatColor.translateAlternateColorCodes('&', map.get("RodColor").toString());
		final String[] item = map.get("RodMaterial").toString().split(":", 2);

		this.material = Material.matchMaterial(item[0]);
		if (this.material == null) {
			throw new IllegalArgumentException("No material found!");
		}
		if (item.length > 1) {
			damageValue = Short.parseShort(item[1]);
		} else if (map.containsKey("RodData")) {
			damageValue = Short.parseShort(map.get("RodData").toString());
		} else {
			damageValue = 0;
		}
		this.itemMagicDecoded = Integer.parseInt(map.get("Id").toString());
		StringBuilder temp = new StringBuilder();
		temp.append(ChatColor.MAGIC);
		for (char ch : Integer.toHexString(this.itemMagicDecoded).toCharArray()) {
			temp.append(ChatColor.COLOR_CHAR).append(ch);
		}
		temp.append(ChatColor.RESET);
		this.itemMagic = temp.toString();
		this.usesCooldown = Boolean.parseBoolean(map.get("UseCooldown").toString());
		this.usesDurability = Boolean.parseBoolean(map.get("UseDurability").toString());
		this.usesPower = Boolean.parseBoolean(map.get("UsePower").toString());
		this.maxDurability = Integer.parseInt(map.get("MaxDurability").toString());
		this.maxPower = Integer.parseInt(map.get("MaxPower").toString());
		if (map.containsKey("keepEnchantsOnBreak")) {
			keepEnchantsOnBreak = Boolean.valueOf(map.get("keepEnchantsOnBreak").toString());
		} else {
			keepEnchantsOnBreak = true;
		}

		List<?> skillList = (List<?>) map.get("Skills");
		List<Skill> skills1 = new ArrayList<Skill>();
		for (Object obj : skillList) {
			try {
				skills1.add(Skill.valueOf(obj.toString().toUpperCase()));
			} catch (IllegalArgumentException e) {
				System.out.println("Ferry empire error: " + e.getMessage() + " at rod " + this.rodName);
			}
		}
		this.skills = skills1.toArray(new Skill[skills1.size()]);
		this.empireStick = new ItemStack(material);
		RodData data = new RodData(this.skills[0], this.maxPower(), this.maxDurability(), this.usesCooldown ? 3 : 0, null, 0);
		this.empireStick.setDurability(damageValue);
		ItemMeta meta = this.empireStick.getItemMeta();
		meta.setLore(data.toLore(this));
		meta.setDisplayName(this.getRodNameItem(data));
		this.empireStick.setItemMeta(meta);

		List<?> recipeList = (List<?>) map.get("Recipes");
		List<ShapedRecipe> recipes1 = new ArrayList<ShapedRecipe>();
		for (Object obj : recipeList) {
			@SuppressWarnings("unchecked")
			ConfigRecipe recipe = new ConfigRecipe((Map<String, Object>) obj);
			recipe.configure(this.empireStick);
			recipes1.add(recipe.recipe);
		}
		this.recipes = recipes1.toArray(new ShapedRecipe[recipes1.size()]);
		this.maySpawnWithCommand = !"false".equals(map.get("maySpawnWithCommand"));
		if (map.containsKey("FireworkOverride")) {
			FireworkOverride overridea = new FireworkOverride((Map<String, Object>) map.get("FireworkOverride"));
			override = overridea;
		} else {
			override = new FireworkOverride(Collections.<String, Object>singletonMap("enabled", false));
		}

	}

	public ConfigureableRod() {
		boolean useUnlimited = "true".equals(System.getProperty("ferrydebug"));
		this.material = Material.BLAZE_ROD;
		this.itemMagicDecoded = 27;
		StringBuilder temp = new StringBuilder();
		temp.append(ChatColor.MAGIC);
		for (char ch : Integer.toHexString(this.itemMagicDecoded).toCharArray()) {
			temp.append(ChatColor.COLOR_CHAR).append(ch);
		}
		temp.append(ChatColor.RESET);
		this.skills = Skill.values();
		this.rodName = "EmpireRod";
		this.damageValue = 0;
		this.itemMagic = temp.toString();
		this.usesCooldown = !useUnlimited; // should be true under normal behavior
		this.usesDurability = !useUnlimited;
		this.usesPower = !useUnlimited;
		this.maxDurability = 1000;
		this.maxPower = 100;
		this.empireStick = new ItemStack(material);
		RodData data = new RodData(this.skills[0], this.maxPower(), this.maxDurability(), this.usesCooldown ? 3 : 0, null, 0);
		ItemMeta meta = this.empireStick.getItemMeta();
		meta.setLore(data.toLore(this));
		meta.setDisplayName(this.getRodNameItem(data));
		this.empireStick.setItemMeta(meta);
		this.recipes = new ShapedRecipe[]{
			new ShapedRecipe(this.empireStick), new ShapedRecipe(this.empireStick)
		};
		this.recipes[0].shape(new String[]{
			"rgr", "sbs", "rgr"
		});
		this.recipes[0].setIngredient('b', this.material);
		this.recipes[0].setIngredient('r', Material.REDSTONE);
		this.recipes[0].setIngredient('g', Material.GLOWSTONE_DUST);
		this.recipes[0].setIngredient('s', Material.SULPHUR);

		this.recipes[1].shape(new String[]{
			"rsr", "gbg", "rsr"
		});
		this.recipes[1].setIngredient('b', this.material);
		this.recipes[1].setIngredient('r', Material.REDSTONE);
		this.recipes[1].setIngredient('g', Material.GLOWSTONE_DUST);
		this.recipes[1].setIngredient('s', Material.SULPHUR);
		this.color = ChatColor.GOLD.toString();
		keepEnchantsOnBreak = false;
		this.maySpawnWithCommand = true;
		override = new FireworkOverride(Collections.<String, Object>singletonMap("enabled", false));
	}

	@Override
	public Recipe[] getRecipes() {
		return this.recipes;
	}

	@Override
	public ItemStack getRod() {
		return this.empireStick;
	}

	@Override
	public String getRodName() {
		return rodName;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("RodName", this.rodName);
		map.put("RodColor", this.color.replace(ChatColor.COLOR_CHAR, '&'));
		map.put("RodMaterial", this.material.getId());
		map.put("RodData", damageValue);
		map.put("Id", this.itemMagicDecoded);
		map.put("UsePower", this.usesPower);
		map.put("UseDurability", this.usesDurability);
		map.put("UseCooldown", this.usesCooldown);
		map.put("MaxDurability", this.maxDurability);
		map.put("MaxPower", this.maxPower);
		map.put("maySpawnWithCommand", this.maySpawnWithCommand);
		List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
		for (ShapedRecipe recipe : this.recipes) {
			temp.add(new ConfigRecipe(recipe).serialize());
		}
		map.put("Recipes", temp);
		List<String> temp1 = new ArrayList<String>();
		for (Skill skill : this.getSkills()) {
			temp1.add(skill.name().toLowerCase());
		}
		map.put("Skills", temp1);
		map.put("FireworkOverride", this.override.save());
		return map;
	}

	@Override
	public Skill[] getSkills() {
		return this.skills;
	}

	@Override
	public String getRodMagic() {
		return this.itemMagic;
	}

	@Override
	public boolean hasCooldown() {
		return this.usesCooldown;
	}

	@Override
	public boolean hasPower() {
		return this.usesPower;
	}

	@Override
	public boolean hasDurability() {
		return this.usesDurability;
	}

	@Override
	public int maxPower() {
		return this.maxPower;
	}

	@Override
	public int maxDurability() {
		return this.maxDurability;
	}

	@Override
	public String getRodColor() {
		return color;
	}

	@Override
	public int getRodMagicId() {
		return this.itemMagicDecoded;
	}

	@Override
	public boolean maySpawnWithCommand() {
		return this.maySpawnWithCommand;
	}

	@Override
	public int compareTo(ConfigureableRod o) {
		if (this.itemMagicDecoded == o.itemMagicDecoded) {
			return 0;
		} else if (this.itemMagicDecoded > o.itemMagicDecoded) {
			return 1;
		} else {
			return -1;
		}
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 79 * hash + this.itemMagicDecoded;
		hash = 79 * hash + Arrays.deepHashCode(this.skills);
		hash = 79 * hash + (this.rodName != null ? this.rodName.hashCode() : 0);
		hash = 79 * hash + (this.usesPower ? 1 : 0);
		hash = 79 * hash + (this.usesDurability ? 1 : 0);
		hash = 79 * hash + (this.usesCooldown ? 1 : 0);
		hash = 79 * hash + this.maxPower;
		hash = 79 * hash + this.maxDurability;
		hash = 79 * hash + (this.material != null ? this.material.hashCode() : 0);
		hash = 79 * hash + (this.color != null ? this.color.hashCode() : 0);
		hash = 79 * hash + (this.keepEnchantsOnBreak ? 1 : 0);
		hash = 79 * hash + (this.maySpawnWithCommand ? 1 : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ConfigureableRod other = (ConfigureableRod) obj;
		if (this.itemMagicDecoded != other.itemMagicDecoded) {
			return false;
		}
		if (!Arrays.deepEquals(this.skills, other.skills)) {
			return false;
		}
		if ((this.rodName == null) ? (other.rodName != null) : !this.rodName.equals(other.rodName)) {
			return false;
		}
		if (this.usesPower != other.usesPower) {
			return false;
		}
		if (this.usesDurability != other.usesDurability) {
			return false;
		}
		if (this.usesCooldown != other.usesCooldown) {
			return false;
		}
		if (this.maxPower != other.maxPower) {
			return false;
		}
		if (this.maxDurability != other.maxDurability) {
			return false;
		}
		if (this.material != other.material) {
			return false;
		}
		if ((this.color == null) ? (other.color != null) : !this.color.equals(other.color)) {
			return false;
		}
		if (this.keepEnchantsOnBreak != other.keepEnchantsOnBreak) {
			return false;
		}
		if (this.maySpawnWithCommand != other.maySpawnWithCommand) {
			return false;
		}
		return true;
	}

	@Override
	public EfectGenerator getFireworkOverride(Skill skill) {
		return this.override.gen;
	}
}
