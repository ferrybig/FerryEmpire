/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.randomfirework.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;

/**
 *
 * @author Fernando
 */
public class FreezeGenerator extends ColorGenerator {

	Random random = new Random();
	Color[] freezeColors = new Color[]{
		Color.FUCHSIA, Color.WHITE, Color.BLUE, Color.PURPLE
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
	public Type getType() {
		return FireworkEffect.Type.BALL;
	}
}
