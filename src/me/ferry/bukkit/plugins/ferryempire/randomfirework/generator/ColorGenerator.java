/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.randomfirework.generator;

import me.ferry.bukkit.plugins.ferryempire.RodBase;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;

/**
 *
 * @author Fernando
 */
public abstract class ColorGenerator extends EfectGenerator {

	public abstract Iterable<Color> getFirstColor();

	public abstract Iterable<Color> getSecondColor();

	public abstract FireworkEffect.Type getType();

	/**
	 *
	 * @param rodbase the value of rodbase
	 */
	@Override
	public FireworkEffect getNewEffect(RodBase rodbase) {
		FireworkEffect.Builder effect = FireworkEffect.builder();
		effect = effect.withColor(this.getFirstColor());
		effect = effect.withFade(this.getSecondColor());
		effect = effect.with(this.getType());
		return effect.build();
	}
}
