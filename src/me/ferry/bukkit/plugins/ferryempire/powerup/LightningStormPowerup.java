/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import java.util.List;
import java.util.Random;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.powerup.ligthingstorm.MultipleProjectileHandler;
import me.ferry.bukkit.plugins.ferryempire.powerup.ligthingstorm.LigthingStormStart;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

/**
 *
 * @author Fernando
 */
public class LightningStormPowerup extends SecondEffectPowerup {

	public LightningStormPowerup() {
		this.fuse = 20;
	}

	@Override
	public void onRemoteExplode(FerryEmpirePlugin plugin, Location loc, Entity ent, Random random, List<Block> blocks, RodBase rodbase) {
		loc.add(0, 2, 0);
		Projectile[] fireballs = new Projectile[]{
			loc.getWorld().spawn(loc, Snowball.class),
			loc.getWorld().spawn(loc, Snowball.class),
			loc.getWorld().spawn(loc, Snowball.class),
			loc.getWorld().spawn(loc, Snowball.class),};
		fireballs[0].setVelocity(new Vector(1, 6, 1).multiply(0.2));
		fireballs[1].setVelocity(new Vector(-1, 6, 1).multiply(0.2));
		fireballs[2].setVelocity(new Vector(1, 6, -1).multiply(0.2));
		fireballs[3].setVelocity(new Vector(-1, 6, -1).multiply(0.2));
		new MultipleProjectileHandler(fireballs, plugin, rodbase) {
			protected void explode() {
				int totalLocations = 0;
				Location startingLocation = null;
				for (Location loc : this.oldLocation) {
					if (loc == null) {
						continue;
					}
					totalLocations++;
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
				double circleLength = 0.0f;
				for (Location loc : this.oldLocation) {
					if (loc == null) {
						continue;
					}
					circleLength += loc.subtract(startingLocation).lengthSquared();
				}
				assert circleLength != 0;// should not happend under normal code execution
				circleLength /= totalLocations;
				circleLength = Math.sqrt(circleLength); // I need it realy, sorry!
				new LigthingStormStart(startingLocation, circleLength, plugin, rodbase).start();
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
