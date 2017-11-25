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
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;

/**
 *
 * @author Fernando
 */
public class AntiGravityPowerup extends SecondEffectPowerup {
	
	
	public AntiGravityPowerup() {
		this.fuse = 120;
		this.yield = 10.0f;
	}

	@Override
	public void onRemoteExplode(FerryEmpirePlugin plugin, Location loc, Entity ent, final Random random, List<Block> block, RodBase rodbase) {
		Location second = loc.clone();
		List<Block> toConsume = new ArrayList<>(block);
		block.clear();
		{
			Collections.shuffle(toConsume, random);
			Function<Block, Double> keyExtractor = (l -> {
				return second.distanceSquared(l.getLocation());
			});
			Collections.sort(toConsume, Comparator.comparing(Block::getY).reversed().thenComparing(Comparator.comparing(keyExtractor)));
		}
		Iterator<Block> toKill = toConsume.iterator();
		List<FlyingBlock> flyingBlocks = new ArrayList<>();
		new BetterBukkitRunnable() {
			int j = 0;
			@Override
			public void run() {
				j++;
				for (int i = 0; i < 4;) {
					if (!toKill.hasNext()) {
						cancel();
						new BetterBukkitRunnable() {
							@Override
							public void run() {
								flyingBlocks.forEach(FlyingBlock::cancel);
							}
						}.runTaskLater(plugin, 20);
						break;
					}
					Block next = toKill.next();
					if (next.isEmpty()) {
						continue;
					}
					Location bLoc = next.getLocation();
					if(i == 0) {
						bLoc.getWorld().playSound(bLoc, Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 5, 0.5f);
					}
					bLoc.add(0, 0.1, 0);
					if(i < 2) {
						final FallingBlock fblock = bLoc.getWorld().spawnFallingBlock(bLoc, next.getType(), (byte) next.getData());
						Location targetLocation = bLoc.add(0, 20, 0);
						targetLocation.add(random.nextDouble() * 4 - 2, random.nextDouble() * 4 - 2, random.nextDouble() * 4 - 2);
						FlyingBlock f = new FlyingBlock(fblock, targetLocation, 200);
						f.start(plugin);
						flyingBlocks.add(f);
						next.setType(Material.AIR);
					} else if(random.nextInt(10) < 2) {
						next.breakNaturally();
					} else {
						next.setType(Material.AIR);
					}
					i++;
				}
			}
		}.runTaskTimer(plugin, 1, 1);
	}
}
