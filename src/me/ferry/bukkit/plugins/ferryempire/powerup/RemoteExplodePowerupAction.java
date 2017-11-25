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
package me.ferry.bukkit.plugins.ferryempire.powerup;

import java.util.List;
import java.util.Random;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

/**
 *
 * @author Fernando
 */
public interface RemoteExplodePowerupAction extends PowerupAction
{
	/**
	 * called when the entity explodes
	 * <p>
	 * @param plugin the value of plugin
	 * @param loc
	 * @param ent
	 * @param random the value of random
	 * @param blocks the value of blocks
	 */

	public abstract void onRemoteExplode(FerryEmpirePlugin plugin, Location loc, Entity ent, Random random, List<Block> blocks, RodBase rodbase);
}
