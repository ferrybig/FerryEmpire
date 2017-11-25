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

import org.bukkit.FireworkEffect;
import java.util.Random;
import java.util.function.Function;
import me.ferry.bukkit.plugins.BetterBukkitRunnable;
import me.ferry.bukkit.plugins.FlyingBlock;
import me.ferry.bukkit.plugins.LocationSpeedCalculator;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;

/**
 *
 * @author Fernando
 */
public class BlackHolePowerup extends SecondEffectPowerup {
	
	
	public BlackHolePowerup() {
		this.fuse = 120;
		this.yield = 14.0f;
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
			Collections.sort(toConsume, Comparator.comparing(keyExtractor));
		}
		Iterator<Block> toKill = toConsume.iterator();

		new BetterBukkitRunnable() {
			int j = 0;
			@Override
			public void run() {
				j++;
				if(j % 4 == 0) {
					plugin.getfPlayer().playFirework(second.getWorld(), second, FireworkEffect.builder()
						.withColor(Color.BLACK)
						.withFade(Color.BLACK)
						.with(FireworkEffect.Type.BALL)
						.build());
					second.getWorld().playEffect(second, Effect.ZOMBIE_DESTROY_DOOR, 49);
				}
				for (int i = 0; i < 4;) {
					if (!toKill.hasNext()) {
						cancel();
						break;
					}
					Block next = toKill.next();
					if (next.isEmpty()) {
						continue;
					}
					Location bLoc = next.getLocation();
					bLoc.add(0, 0.1, 0);
					if(i == 0) {
						final FallingBlock fblock = bLoc.getWorld().spawnFallingBlock(bLoc, next.getType(), (byte) next.getData());
						new FlyingBlock(fblock, second.clone(), 200, new FlyingBlock.Handler() {

							Location tmp = new Location(null, 0, 0, 0);
							@Override
							public void onTick(FallingBlock ent, Location target) {
								ent.getLocation(tmp);
								if (target.distanceSquared(tmp) < 0.5 * 0.5)
								{
									ent.getLocation(target);
									if(random.nextInt(10) < 2) {
										ent.getWorld().createExplosion(target, 1.0f, false);
									}
									ent.remove();
								}
							}
						}).start(plugin);
					}
					if(random.nextInt(10) < 2) {
						next.breakNaturally();
					}
					next.setType(Material.AIR);
					i++;
				}
			}
		}.runTaskTimer(plugin, 1, 1);
	}
}
