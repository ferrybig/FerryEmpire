/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.randomfirework.generator;

import java.util.Arrays;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;

/**
 *
 * @author Fernando
 */
public class StaticColorGenerator extends ColorGenerator {

	int loop = 0;
	Color[][][] colors;
	private final FireworkEffect.Type type;

	public StaticColorGenerator(Color[][][] colors, FireworkEffect.Type type) {
		if (colors.length == 0) {
			throw new IllegalArgumentException("excepted: color[length > 0][2][length >= 0 ]; got: color[0][?][?]");
		}
		this.colors = colors;
		this.type = type;
	}

	@Override
	public Iterable<Color> getFirstColor() {
		if (loop >= colors.length) {
			loop = 0;
		}
		return Arrays.asList(colors[loop][0]);
	}

	@Override
	public Iterable<Color> getSecondColor() {
		loop++;
		return Arrays.asList(colors[loop - 1][1]);
	}

	@Override
	public Type getType() {
		return type;
	}
}
