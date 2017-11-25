/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

/**
 *
 * @author Fernando
 */
public final class ConfigRecipe implements ConfigurationSerializable {

	public ShapedRecipe recipe;
	public String[] shape;
	public Map<Character, ItemStack> items;

	public ConfigRecipe(Map<String, Object> map) {
		List<String> sh = new ArrayList<String>();
		for (Object str : (List<?>) map.get("shape")) {
			sh.add(str.toString());
		}
		this.shape = sh.toArray(new String[0]);
		this.items = new HashMap<Character, ItemStack>();
		for (Object str : ((Map<?, ?>) map.get("items")).entrySet()) {
			Map.Entry<?, ?> next = (Map.Entry<?, ?>) str;
			char c = next.getKey().toString().charAt(0);
			int itemId;
			short damage;
			String id = next.getValue().toString();
			String[] split = id.split(":", 2);
			if (split.length == 1) {
				itemId = Integer.parseInt(split[0]);
				damage = 0;
			} else {
				itemId = Integer.parseInt(split[0]);
				damage = Short.parseShort(split[1]);
			}
			items.put(c, new ItemStack(itemId, damage));
		}
	}

	public ConfigRecipe(ShapedRecipe recipe) {
		this.recipe = recipe;
	}

	public void configure(ItemStack rod) {
		recipe = new ShapedRecipe(rod);
		recipe.shape(shape);
		for (Map.Entry<Character, ItemStack> next : this.items.entrySet()) {
			recipe.setIngredient(next.getKey(), next.getValue().getType(), next.getValue().getDurability());
		}
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> ingredients = new HashMap<String, String>();
		Map<Character, ItemStack> ingredientMap = recipe.getIngredientMap();
		for (Map.Entry<Character, ItemStack> next : ingredientMap.entrySet()) {
			ItemStack v = next.getValue();
			if (v == null) {
				continue;
			}
			String id = String.valueOf(v.getTypeId());
			if (next.getValue().getDurability() != 0) {
				id += ":" + next.getValue().getDurability();
			}
			ingredients.put(next.getKey().toString(), id);
		}
		map.put("items", ingredients);
		map.put("shape", Arrays.asList(recipe.getShape()));
		return map;

	}
}
