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
import java.util.Arrays;
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
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 *
 * @author Fernando
 */
public class DecayPowerup extends SlowBlockAreaPowerup {
	
	public DecayPowerup() {
		this.radius = 15;
		this.circle = true;
	}
	
	private final BlockFace[] faces = new BlockFace[] {
		BlockFace.SELF, 
		BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, 
		BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST
	};

	@Override
	protected BlockAction createBlockAction(FerryEmpirePlugin plugin, Player shooter, Location loc, List<Block> b, Random random, RodBase rodbase) {
		return new BlockAction() {
			@Override
			public boolean doBlockActionsChunk(FerryEmpirePlugin plugin, Player shooter, Location loc, Iterator<Block> b, Random random, RodBase rodbase, int iteration) {
				for (int i = 0; i < 100;) {
					if (!b.hasNext()) {
						return false;
					}
					Block next = b.next();
					if (next.isEmpty()) {
						continue;
					}
					if(iteration % 4 == 0 && i % 64 == 0) {
						plugin.getfPlayer().playFirework(next.getWorld(), next.getLocation().add(0.5, 0.5, 0.5), FireworkEffect.builder()
							.withColor(Color.OLIVE)
							.withFade(Color.GRAY)
							.with(FireworkEffect.Type.BURST)
							.build());
					}
					switch(next.getType()) {
						case GRASS_PATH:
						case GRASS: {
							next.setType(Material.DIRT);
						}
						break;
						case YELLOW_FLOWER:
						case RED_MUSHROOM:
						case LEAVES:
						case BROWN_MUSHROOM:
						case CROPS: {
							next.setType(Material.AIR);
						}
						break;
						case COBBLESTONE: {
							if(random.nextInt(5) == 1)
								next.setType(Material.MOSSY_COBBLESTONE);
						}
						break;
						case SMOOTH_BRICK: {
							if(random.nextInt(5) == 1)
								next.setData((byte) 1);
							else if(random.nextInt(5) == 1)
								next.setData((byte) 2);
						}
						break;
						case WOOD_BUTTON:
						case WOOD_DOOR:
						case STONE_BUTTON:
						case TORCH: {
							if(random.nextInt(5) == 1)
								next.setType(Material.AIR);
						}
						break;
					}
					
					if(next.getClass().getInterfaces().length < 3 && random.nextBoolean()) {
						List<BlockFace> vulnable = new ArrayList<>(); 
						Block below = next.getRelative(BlockFace.DOWN);
						int connections = 0;
						for(BlockFace f : faces) {
							if(below.getRelative(f).getType() != Material.AIR) {
								connections += (Math.abs(f.getModX()) + Math.abs(f.getModY()) + Math.abs(f.getModZ()));
							} else {
								vulnable.add(f);
							}
						}
						if(5 > connections && random.nextInt(5) > connections) {
							Block newBlock = below.getRelative(vulnable.get(random.nextInt(vulnable.size())));
							newBlock.setType(next.getType());
							newBlock.setData(next.getData());
							next.setType(Material.AIR);
							next.setData((byte) 0);
						}
					}
					i++;
				}
				return true;
			}
		};
	}

}
