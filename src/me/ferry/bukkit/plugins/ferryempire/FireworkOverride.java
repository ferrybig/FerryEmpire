/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.EfectGenerator;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Fernando
 */
public final class FireworkOverride {

	boolean usingOverride;
	EfectGenerator gen;
	List<FireworkEffect> effects;

	public FireworkOverride(Map<String, Object> map) {
		usingOverride = (Boolean) map.get("enabled");
		if (usingOverride) {
			FireworkMeta meta = (FireworkMeta) map.get("firework");
			final Random random = new Random();
			if (meta != null) {
				effects = meta.getEffects();
				gen = new EfectGenerator() {
					@Override
					public FireworkEffect getNewEffect(RodBase rodbase) {
						return effects.get(random.nextInt(effects.size()));
					}
				};
			} else {
				effects = new ArrayList<FireworkEffect>();
				effects.add(FireworkEffect.builder().with(FireworkEffect.Type.BALL).withColor(Color.YELLOW).withFade(Color.WHITE).withFlicker().build());
				effects.add(FireworkEffect.builder().with(FireworkEffect.Type.BALL).withColor(Color.GREEN).withFade(Color.WHITE).withTrail().build());
				gen = new EfectGenerator() {
					@Override
					public FireworkEffect getNewEffect(RodBase rodbase) {
						return effects.get(random.nextInt(effects.size()));
					}
				};
			}
		}
	}

	public Map<String, Object> save() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("enabled", usingOverride);
		if (effects != null) {
			ItemStack s = new ItemStack(Material.FIREWORK);
			FireworkMeta meta = (FireworkMeta) s.getItemMeta();
			meta.addEffects(effects);
			m.put("firework", meta);
		}
		return m;
	}
}
