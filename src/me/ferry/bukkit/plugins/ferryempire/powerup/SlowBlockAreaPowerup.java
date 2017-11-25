/*
 * Copyright (C) 2016 Fernando
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import me.ferry.bukkit.plugins.BetterBukkitRunnable;
import me.ferry.bukkit.plugins.FlyingBlock;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

/**
 *
 * @author Fernando
 */
public abstract class SlowBlockAreaPowerup extends BlockAreaPowerup {

	int tickDelay = 1;
	
	protected abstract BlockAction createBlockAction(FerryEmpirePlugin plugin, Player shooter, Location loc, List<Block> b, Random random, RodBase rodbase);
	
	protected Comparator<Block> getSortListComparator(Location loc) {
		Function<Block, Double> keyExtractor = (l -> {
			return loc.distanceSquared(l.getLocation());
		});
		return Comparator.comparing(keyExtractor);
	}
	
	protected void sortList(List<Block> blocks, Location loc) {
		blocks.sort(getSortListComparator(loc).thenComparing(Object::hashCode));
	}
	
	@Override
	protected void doBlockActions(FerryEmpirePlugin plugin, Player shooter, Location loc, List<Block> b, Random random, RodBase rodbase) {
		sortList(b, loc);
		BlockAction action = this.createBlockAction(plugin, shooter, loc, b, random, rodbase);
		Iterator<Block> toKill = b.iterator();
		new BetterBukkitRunnable() {
			int iteration = 0;
			@Override
			public void run() {
				iteration++;
				if(!toKill.hasNext() || !action.doBlockActionsChunk(plugin, shooter, loc, toKill, random, rodbase, iteration)) {
					cancel();
					action.finish(plugin, shooter, loc, b, random, rodbase);
				}
			}
		}.runTaskTimer(plugin, 1, tickDelay);
	}
	
	public interface BlockAction {
		
		public abstract boolean doBlockActionsChunk(
				FerryEmpirePlugin plugin, Player shooter, Location loc, 
				Iterator<Block> b, Random random, RodBase rodbase, 
				int iteration);
		
		public default void finish(
				FerryEmpirePlugin plugin, Player shooter,  Location loc, 
				List<Block> b, Random random, RodBase rodbase) {
		}
	}
	
}
