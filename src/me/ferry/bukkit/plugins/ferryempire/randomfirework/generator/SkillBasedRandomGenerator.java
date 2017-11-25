/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.randomfirework.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.ferry.bukkit.plugins.ferryempire.Skill;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;

/**
 *
 * @author Fernando
 */
public class SkillBasedRandomGenerator extends ColorGenerator {

	public SkillBasedRandomGenerator(Skill skill) {
		random = new Random(skill.ordinal() ^ 0x58934523);
	}
	final List<Color> colors = new ArrayList<Color>();
	final Random random;

	@Override
	public Iterable<Color> getFirstColor() {
		colors.clear();
		int max = random.nextInt(4) + 1;
		for (int i = 0; i < max; i++) {
			colors.add(Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
		}
		return colors;
	}

	@Override
	public Iterable<Color> getSecondColor() {
		colors.clear();
		int max = random.nextInt(4) + 1;
		for (int i = 0; i < max; i++) {
			colors.add(Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
		}
		return colors;
	}

	@Override
	public Type getType() {
		return FireworkEffect.Type.STAR;
	}
}
