/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire;

import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Fernando
 */
public final class FlameThrower implements Runnable {

	private final Plugin plugin;
	private int fireTick = 0;
	private int taskId = -1;
	private boolean cancelled = false;
	private final int smokeDuration;
	private final Player player;

	public FlameThrower(Plugin plugin, int smokeDuration, Player player) {
		this.plugin = plugin;
		this.smokeDuration = smokeDuration;
		this.player = player;
	}

	public void start() {
		if (taskId != -1) {
			throw new IllegalStateException("Already started!");
		}
		this.taskId = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 1, 2);
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

	@Override
	public void run() {
		if (fireTick > this.smokeDuration) {
			this.stop();
			return;
		}
		fireTick++;
		for (int i = 0; i < 1; i++) {
			Snowball arrow = player.launchProjectile(Snowball.class);
			arrow.setFireTicks(arrow.getMaxFireTicks());
			arrow.setMetadata(Skill.class.getName(), new FixedMetadataValue(this.plugin, Skill.POWERFULL_BANG));
		}

	}

	private boolean isCancelled() {
		return this.cancelled;
	}
}
