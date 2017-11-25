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

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import me.ferry.bukkit.plugins.BetterBukkitRunnable;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import me.ferry.bukkit.plugins.ferryempire.Skill;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

/**
 *
 * @author Fernando
 */
public class ClusterPowerup extends SecondEffectPowerup {

	protected int forkCount = 7;
	
	public ClusterPowerup() {
		this.fuse = 100;
		this.yield = 3.5f;
	}

	@Override
	protected Entity createProjectile(FerryEmpirePlugin plugin, Player player) {
		Location target = player.getLastTwoTargetBlocks(EnumSet.of(Material.AIR), 100).iterator().next().getLocation();
		double y = Math.max(target.getY(), player.getLocation().getY());
		y -= player.getLocation().getY();
		y += 10;
		Vector speed = calculateVelocity(player.getLocation().toVector(), target.toVector(), (int) y);
		Entity e = player.launchProjectile(org.bukkit.entity.Snowball.class);
		e.setVelocity(speed);
		return e;
	}

	@Override
	protected void spawnNewProjectile(Entity orginal, Location loc, FerryEmpirePlugin plugin, RodData data) {
		Vector inital = orginal.getVelocity();
		Random random = new Random();
		for(int i = 0; i < forkCount; i++) {
			Vector modifed = inital.clone();
			modifed.add(new Vector(random.nextDouble() - .5, random.nextDouble() - .5, random.nextDouble() - .5).multiply(1));
			orginal.setVelocity(modifed);
			TNTPrimed block = loc.getWorld().spawn(loc, TNTPrimed.class);
			block.setVelocity(orginal.getVelocity());
			block.setMetadata(Skill.class.getName(), new FixedMetadataValue(plugin, data.getSkill().name()));
			block.setFuseTicks(fuse + random.nextInt(10));
			block.setYield(yield);
			block.setFireTicks(fuse);
			
			new BetterBukkitRunnable() {
				Location last = new Location(null, 0, 0, 0);
				Location now = new Location(null, 0, 0, 0);
				
				@Override
				public void run() {
					block.getLocation(now);
					if(last.getY() == now.getY()) {
						block.setFuseTicks(1);
						now.getWorld().strikeLightning(now);
						plugin.getfPlayer().playFirework(now.getWorld(), now, FireworkEffect.builder()
							.withColor(Color.RED)
							.withFade(Color.SILVER)
							.with(FireworkEffect.Type.BALL_LARGE)
							.build());
						cancel();
					}
					block.getLocation(last);
				}
			}.runTaskTimer(plugin, 3, 3);
		}
	}

	@Override
	public void onRemoteExplode(FerryEmpirePlugin plugin, Location loc, Entity ent, Random random, List<Block> block, RodBase rodbase) {
		Iterator<Block> blocks = block.iterator();
		int spawned = 0;
		while (blocks.hasNext()) {
			Block bl = blocks.next();
			spawned++;
			Location otherLocation = bl.getLocation();
			if (spawned <= 1) {
				loc.getWorld().playEffect(otherLocation, Effect.ZOMBIE_DESTROY_DOOR, bl.getTypeId());
			}
			if (spawned <= 9) {
				FallingBlock fall = loc.getWorld().spawnFallingBlock(bl.getLocation().add(0.5, 0.5, 0.5), bl.getType(), bl.getData());
				fall.setDropItem(false);
				Vector vector = new Vector(otherLocation.getX() - loc.getX(), 125, otherLocation.getZ() - loc.getZ());
				vector.normalize();
				vector.setY(2);
				vector.normalize();
				vector.multiply(1);
				fall.setVelocity(vector);
			}
		}
	}

	public static Vector calculateVelocity(Vector from, Vector to, int heightGain) {
		// Gravity of a potion
		double gravity = 0.080; // Snowball 0.075 // tnt unknown

		// Block locations
		int endGain = to.getBlockY() - from.getBlockY();
		double horizDist = Math.sqrt(distanceSquared(from, to));

		// Height gain
		int gain = heightGain;

		double maxGain = gain > (endGain + gain) ? gain : (endGain + gain);

		// Solve quadratic equation for velocity
		double a = -horizDist * horizDist / (4 * maxGain);
		double b = horizDist;
		double c = -endGain;

		double slope = -b / (2 * a) - Math.sqrt(b * b - 4 * a * c) / (2 * a);

		// Vertical velocity
		double vy = Math.sqrt(maxGain * gravity);

		// Horizontal velocity
		double vh = vy / slope;

		// Calculate horizontal direction
		int dx = to.getBlockX() - from.getBlockX();
		int dz = to.getBlockZ() - from.getBlockZ();
		double mag = Math.sqrt(dx * dx + dz * dz);
		double dirx = dx / mag;
		double dirz = dz / mag;

		// Horizontal velocity components
		double vx = vh * dirx;
		double vz = vh * dirz;

		return new Vector(vx, vy, vz);
	}

	private static double distanceSquared(Vector from, Vector to) {
		double dx = to.getBlockX() - from.getBlockX();
		double dz = to.getBlockZ() - from.getBlockZ();

		return dx * dx + dz * dz;
	}
}
