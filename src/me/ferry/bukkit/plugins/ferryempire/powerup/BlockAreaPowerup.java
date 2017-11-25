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
import java.util.List;
import java.util.Random;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Fernando
 */
public abstract class BlockAreaPowerup extends MetadataPowerup {

	int radius;
	boolean circle;

	public BlockAreaPowerup() {
		super(Snowball.class);
		radius = 2;
		circle = false;
	}

	protected abstract void doBlockActions(FerryEmpirePlugin plugin, Player shooter, Location loc, List<Block> b, Random random, RodBase rodbase);

	@Override
	public void onRemoteBlockHit(FerryEmpirePlugin plugin, Player shooter, Location loc, Projectile ent, Random random, RodBase rodbase) {
		int offSetX = loc.getBlockX();
		int offSetY = loc.getBlockY();
		int offSetZ = loc.getBlockZ();
		World world = loc.getWorld();

		int startX = offSetX - radius;
		int startY = offSetY - radius;
		int startZ = offSetZ - radius;

		int endX = offSetX + radius;
		int endY = offSetY + radius;
		int endZ = offSetZ + radius;
		
		List<Block> blocks = new ArrayList<>();
		for (int counterX = startX; counterX <= endX; counterX++) {
			final double radiusX;
			if(circle)
				radiusX = Math.pow(counterX - offSetX, 2);
			else
				radiusX = 0;
			if(radiusX > radius * radius)
				continue;
			for (int counterZ = startZ; counterZ <= endZ; counterZ++) {
				final double radiusXZ;
				if(circle)
					radiusXZ = radiusX + Math.pow(counterZ - offSetZ, 2);
				else
					radiusXZ = 0;
				if(radiusXZ > radius * radius)
					continue;
				for (int counterY = startY; counterY <= endY; counterY++) {
					final double radiusXZY;
					if(circle)
						radiusXZY = radiusX + Math.pow(counterY - offSetY, 2);
					else
						radiusXZY = 0;
					if(radiusXZY > radius * radius)
						continue;
					blocks.add(world.getBlockAt(counterX, counterY, counterZ));
				}
			}
		}
		doBlockActions(plugin, shooter, loc, blocks, random, rodbase);
	}
}
