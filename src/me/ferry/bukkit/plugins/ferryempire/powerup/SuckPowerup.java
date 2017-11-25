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
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import me.ferry.bukkit.plugins.BetterBukkitRunnable;
import me.ferry.bukkit.plugins.FlyingBlock;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

/**
 *
 * @author Fernando
 */
public class SuckPowerup extends SecondEffectPowerup {
	
	public SuckPowerup()
	{
		this.fuse = 60;
		this.yield = 5.0f;
	}

	@Override
	public void onRemoteExplode(FerryEmpirePlugin plugin, Location loc, Entity ent, final Random random, List<Block> block, RodBase rodbase) {
		Iterator<Block> blocks = block.iterator();
		int spawned = 0;
		loc.getWorld().playSound(loc, Sound.ENTITY_IRONGOLEM_ATTACK, 10, 10);
		final List<FlyingBlock> flyingBlocks = new ArrayList<>();
		final List<Location> locs = new ArrayList<>();
		final Location entityLoc = loc.clone();
		while (blocks.hasNext())
		{
			Block bl = blocks.next();
			spawned++;
			Location otherLocation = bl.getLocation();
			if (spawned <= 5)
			{
				loc.getWorld().playEffect(otherLocation, Effect.ZOMBIE_DESTROY_DOOR, bl.getTypeId());
			}
			if (spawned <= 50)
			{

				FallingBlock fall = loc.getWorld().spawnFallingBlock(bl.getLocation().add(0.5, 0.5, 0.5), bl.getType(), bl.getData());
				fall.setDropItem(false);
				Vector vector = new Vector(otherLocation.getX() - loc.getX(), 125, otherLocation.getZ() - loc.getZ());
				vector.normalize();
				vector.setY(2);
				vector.normalize();
				vector.multiply(3);
				fall.setVelocity(vector);
				Location newLoc = loc.clone();
				locs.add(newLoc);
				FlyingBlock flyingBlock = new FlyingBlock(fall, newLoc, 200);
				flyingBlocks.add(flyingBlock);
				flyingBlock.start(plugin);
			}
		}
		if(flyingBlocks.isEmpty()) {
			return;
		}
		new BetterBukkitRunnable() {
			int ticks = 100;
			@Override
			public void run() {
				ticks--;
				for(Location location : locs) {
					location.setX(entityLoc.getX() + random.nextDouble() * 2 - 1);
					location.setY(entityLoc.getY() + random.nextDouble() * 2 - 1);
					location.setZ(entityLoc.getZ() + random.nextDouble() * 2 - 1);
				}
				if(ticks < 2) {
					for(Location location : locs) {
						location.setX(entityLoc.getX());
						location.setY(entityLoc.getY() + 10);
						location.setZ(entityLoc.getZ());
					}
				}
				if(ticks < 0) {
					for(FlyingBlock b : flyingBlocks) {
						b.cancel();
					}
				}
			}
		}.runTaskTimer(plugin, 1, 1);
	}
	
}
