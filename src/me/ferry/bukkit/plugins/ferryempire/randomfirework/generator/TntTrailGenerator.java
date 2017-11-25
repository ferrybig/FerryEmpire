/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.randomfirework.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.EfectGenerator;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;

/**
 *
 * @author Fernando
 */
public class TntTrailGenerator extends ColorGenerator {

	public TntTrailGenerator() {
	}
	Random random = new Random();
	Color[] freezeColors = new Color[]{
		Color.RED, Color.WHITE, Color.BLACK, Color.RED
	};
	List<Color> temp = new ArrayList<Color>();

	@Override
	public Iterable<Color> getFirstColor() {
		temp.clear();
		for (int i = 0; i < 2; i++) {
			temp.add(freezeColors[random.nextInt(freezeColors.length)]);
		}
		return temp;
	}

	@Override
	public Iterable<Color> getSecondColor() {
		temp.clear();
		for (int i = 0; i < 2; i++) {
			temp.add(freezeColors[random.nextInt(freezeColors.length)]);
		}
		return temp;
	}

	@Override
	public FireworkEffect.Type getType() {
		return FireworkEffect.Type.BURST;
	}
}
