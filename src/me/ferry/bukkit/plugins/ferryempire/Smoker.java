/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire;

import java.util.Random;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Fernando
 */
public final class Smoker implements Runnable {

	private final Plugin plugin;
	private final int smokeRadius;
	private final int smokeDuration;
	private final int smokeDensityLow;
	private final int smokeDensityHigh;
	private int smokeTick = 0;
	private int taskId = -1;
	private boolean cancelled = false;
	private final Location smokeLocation;

	public Smoker(Plugin plugin, int smokeRadius, int smokeDuration, int smokeDensityLow, int smokeDensityHigh, Location smokeLocation) {
		this.plugin = plugin;
		this.smokeRadius = smokeRadius;
		this.smokeDuration = smokeDuration;
		this.smokeDensityLow = smokeDensityLow;
		this.smokeDensityHigh = smokeDensityHigh;
		this.smokeLocation = smokeLocation;
	}

	public void start() {
		if (taskId != -1) {
			throw new IllegalStateException("Already started!");
		}
		this.taskId = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 1, 1);
	}

	public void stop() {
		if (taskId == -1) {
			throw new IllegalStateException("Not started yet!");
		}
		if (this.isCancelled()) {
			return;
		}
		this.plugin.getServer().getScheduler().cancelTask(taskId);
		this.cancelled = true;
	}

	public Plugin getPlugin() {
		return plugin;
	}
	final Random random = new Random();

	private Location randomLocation() {
		double randomDegree = random.nextDouble() * 2 * Math.PI; // better code by desth
		double randomUp = random.nextDouble() * 2 * Math.PI;
		double randomRadius = random.nextInt(smokeRadius);
		double distanceUp = Math.cos(randomUp) * randomRadius;
		randomRadius = Math.sin(randomUp) * randomRadius;
		double distanceLeft = Math.cos(randomDegree) * randomRadius;
		double distanceFront = Math.sin(randomDegree) * randomRadius;
		return smokeLocation.clone().add(distanceLeft, distanceUp, distanceFront);
	}

	@Override
	public void run() {
		if (smokeTick > this.smokeDuration) {
			this.stop();
			return;
		}
		smokeTick++;
		int smokeAmount = this.smokeDensityLow;
		if (this.smokeDensityLow < this.smokeDensityHigh) {
			smokeAmount += this.random.nextInt(this.smokeDensityHigh - this.smokeDensityLow);
		}
		for (int i = 0; i < smokeAmount; i++) {
			smokeLocation.getWorld().playEffect(this.randomLocation(), Effect.SMOKE, random.nextInt(8));

		}
	}

	private boolean isCancelled() {
		return this.cancelled;
	}
}
