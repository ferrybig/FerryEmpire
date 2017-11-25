/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.randomfirework.generator;

import me.ferry.bukkit.plugins.ferryempire.RodBase;
import org.bukkit.FireworkEffect;

/**
 *
 * @author Fernando
 */
public abstract class EfectGenerator {

	/**
	 *
	 * @param rodbase the value of rodbase
	 */
	public abstract FireworkEffect getNewEffect(RodBase rodbase);
}
