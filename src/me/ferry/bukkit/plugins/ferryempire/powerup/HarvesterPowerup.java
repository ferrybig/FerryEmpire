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

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Fernando
 */
public class HarvesterPowerup extends SlowBlockAreaPowerup {

	public HarvesterPowerup() {
		this.radius = 10;
		this.circle = true;
	}
	
	@Override
	protected BlockAction createBlockAction(FerryEmpirePlugin plugin, Player shooter, Location loc, List<Block> b, Random random, RodBase rodbase) {
		return new BlockAction() {
			@Override
			public boolean doBlockActionsChunk(FerryEmpirePlugin plugin, Player shooter, Location loc, Iterator<Block> b, Random random, RodBase rodbase, int iteration) {
				int done = 0;
				ItemStack tool = new ItemStack(Material.SHEARS);
				while(b.hasNext()) {
					Block block = b.next(); 
					switch(block.getType()) {
						case LEAVES:
						case LEAVES_2:
						case LOG:
						case LOG_2:
						case CROPS:
						case PUMPKIN:
						case MELON:
						case NETHER_WARTS:
						case SUGAR_CANE:
						case DEAD_BUSH:
						case CACTUS:
						case VINE:
						case YELLOW_FLOWER:
						case RED_ROSE:
						case RED_MUSHROOM:
						case BROWN_MUSHROOM:
						case LONG_GRASS:
						case WEB:
						case HUGE_MUSHROOM_1:
						case HUGE_MUSHROOM_2:
						case WATER_LILY:
						case CARROT:
						case POTATO:
						case CHORUS_FLOWER:
						case CHORUS_PLANT:
						case BEETROOT_BLOCK:
						case DOUBLE_PLANT:
						{
							if(done < 2) {
								block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
							}
							block.breakNaturally(tool);
							block.setType(Material.AIR);
						}
						break;
						default: {
							continue;
						}
					}
					done++;
					if(done > 16) {
						return true;
					}
				}
				return true;
			}
		};
	}
	
}
