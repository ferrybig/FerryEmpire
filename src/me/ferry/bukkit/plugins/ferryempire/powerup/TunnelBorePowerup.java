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
import me.ferry.bukkit.plugins.BetterBukkitRunnable;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.SkillColors;
import me.ferry.bukkit.plugins.ferryempire.powerup.ligthingstorm.MultipleProjectileHandler;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

/**
 *
 * @author Fernando
 */
public class TunnelBorePowerup extends SecondEffectPowerup {

	private static final double SQUARE_ROOT_OF_TWO = Math.sqrt(2);
	
	private static final double UPWARD_VECTOR = 4.1;
	
	public TunnelBorePowerup() {
		this.fuse = 20;
	}

	@Override
	public void onRemoteExplode(FerryEmpirePlugin plugin, Location loc, Entity ent, Random random, List<Block> blocks, RodBase rodbase) {
		loc.add(0, 2, 0);
		Projectile[] fireballs = new Projectile[]{
			loc.getWorld().spawn(loc, EnderPearl.class),
			loc.getWorld().spawn(loc, EnderPearl.class),
			loc.getWorld().spawn(loc, EnderPearl.class),
			loc.getWorld().spawn(loc, EnderPearl.class),
			loc.getWorld().spawn(loc, EnderPearl.class),
			loc.getWorld().spawn(loc, EnderPearl.class),
			loc.getWorld().spawn(loc, EnderPearl.class),
			loc.getWorld().spawn(loc, EnderPearl.class),
		};
		fireballs[0].setVelocity(new Vector(1, UPWARD_VECTOR, 1).multiply(0.15));
		fireballs[1].setVelocity(new Vector(-1, UPWARD_VECTOR, 1).multiply(0.15));
		fireballs[2].setVelocity(new Vector(1, UPWARD_VECTOR, -1).multiply(0.15));
		fireballs[3].setVelocity(new Vector(-1, UPWARD_VECTOR, -1).multiply(0.15));
		
		fireballs[4].setVelocity(new Vector(-SQUARE_ROOT_OF_TWO, UPWARD_VECTOR, 0).multiply(0.15));
		fireballs[5].setVelocity(new Vector(SQUARE_ROOT_OF_TWO, UPWARD_VECTOR, 0).multiply(0.15));
		fireballs[6].setVelocity(new Vector(0, UPWARD_VECTOR, SQUARE_ROOT_OF_TWO).multiply(0.15));
		fireballs[7].setVelocity(new Vector(0, UPWARD_VECTOR, -SQUARE_ROOT_OF_TWO).multiply(0.15));
		new MultipleProjectileHandler(fireballs, plugin, rodbase, SkillColors.ENDER_COLOR_BURST.getEffect(null, rodbase)) {
			@Override
			protected void playEfect(Location[] locs) {
				if(this.timer % 2 == 0)
					super.playEfect(locs);
			}

			@Override
			protected void onFail() {
				super.onFail();
				for (Location loc : this.oldLocation) {
					if (loc == null) {
						continue;
					}
					loc.getWorld().createExplosion(loc, 2.5f);
				}
			}
			
			
			@Override
			protected void explode() {
				Location startingLocation = null;
				int total;
				List<Location> valid = new ArrayList<>(this.oldLocation.length);
				double length;
				{
					double circleLength;
					int totalLocations = 0;
					for (Location loc : this.oldLocation) {
						if (loc == null) {
							continue;
						}
						totalLocations++;
						valid.add(loc.clone());
						if (startingLocation == null) {
							startingLocation = loc.clone();
						} else {
							startingLocation.add(loc);
						}
					}
					if (startingLocation == null) {
						return; // no valid locations where found
					}
					if (totalLocations < 2) {
						return; // no valid locations where found
					}
					startingLocation.multiply(1.0f / totalLocations);
					circleLength = 0.0f;
					for (Location loc : this.oldLocation) {
						if (loc == null) {
							continue;
						}
						circleLength += loc.subtract(startingLocation).lengthSquared();
					}
					assert circleLength != 0;// should not happend under normal code execution
					circleLength /= totalLocations;
					circleLength = Math.sqrt(circleLength); // I need it realy, sorry!
					assert valid.size() == totalLocations;
					total = totalLocations;
					length = circleLength;
				}
				EnderCrystal[] crystal = new EnderCrystal[total];
				startingLocation.add(0, 20, 0);
				Location workingLocation = startingLocation;
				System.out.println(workingLocation);
				for (int i = 0; i < total; i++) {
					crystal[i] = valid.get(i).getWorld().spawn(valid.get(i), EnderCrystal.class);
					crystal[i].setBeamTarget(workingLocation);
					System.out.println(crystal[i] + ": " + crystal[i].getLocation());
				}
				new BetterBukkitRunnable() {
					int timer = 40;

					@Override
					public void run() {
						for (int i = 0; i < total; i++) {
							crystal[i].setBeamTarget(workingLocation);
							if(!crystal[i].isValid()) {
								timer--;
							}
						}
						workingLocation.getWorld().createExplosion(workingLocation, (float) Math.sqrt(Math.sqrt(length)) * 1.3f);
						workingLocation.add(0, -1, 0);
						if (timer-- < 0) {
							for (int i = 0; i < total; i++) {
								crystal[i].remove();
							}
							cancel();
						}

					}
				}.runTaskTimer(plugin, 10, 3);
			}

		}.start();
	}

	@Override
	public void loadFromConfig(ConfigurationSection from) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void writeToConfig(ConfigurationSection to) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
