/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.randomfirework.factory;

import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.Skill;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.EfectGenerator;

/**
 *
 * @author Fernando
 */
public interface EffectFactory {

	/**
	 *
	 * @param skill the value of skill
	 * @param rodbase the value of rodbase
	 */
	public abstract EfectGenerator getEffect(Skill skill, RodBase rodbase);
}
