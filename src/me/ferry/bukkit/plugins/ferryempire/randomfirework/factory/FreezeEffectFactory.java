/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.randomfirework.factory;

import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.Skill;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.EfectGenerator;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.FreezeGenerator;

/**
 *
 * @author Fernando
 */
public class FreezeEffectFactory implements EffectFactory {

	public FreezeEffectFactory() {
	}

	/**
	 *
	 * @param skill the value of skill
	 * @param rodbase the value of rodbase
	 */
	@Override
	public EfectGenerator getEffect(Skill skill, RodBase rodbase) {
		if (rodbase != null) {
			EfectGenerator g = rodbase.getFireworkOverride(skill);
			if (g != null) {
				return g;
			}
		}
		return new FreezeGenerator();
	}

}
