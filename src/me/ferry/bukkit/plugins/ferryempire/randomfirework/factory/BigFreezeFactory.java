/*
 * Copyright (C) 2014 Fernando
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ferry.bukkit.plugins.ferryempire.randomfirework.factory;

import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.Skill;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.BigFreezeGenerator;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.EfectGenerator;

/**
 *
 * @author ferrybig
 */
public class BigFreezeFactory implements EffectFactory {

	@Override
	public EfectGenerator getEffect(Skill skill, RodBase rodbase) {
		if (rodbase != null) {
			EfectGenerator g = rodbase.getFireworkOverride(skill);
			if (g != null) {
				return g;
			}
		}
		return new BigFreezeGenerator();
	}
}
