/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup.ligthingstorm;

import me.ferry.bukkit.plugins.BetterBukkitRunnable;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.SkillColors;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.EfectGenerator;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Fernando
 */
public abstract class MultipleProjectileHandler extends BetterBukkitRunnable {

	protected final FerryEmpirePlugin plugin;
	protected final EfectGenerator effect;
	protected final Entity[] entities;
	private boolean exploded = false;
	protected final RodBase rodbase;
	protected final Location[] oldLocation;
	protected int timer = 10;

	/**
	 *
	 * @param grenade the value of grenade
	 * @param plugin the value of plugin
	 * @param rodbase
	 */
	public MultipleProjectileHandler(Entity[] grenade, FerryEmpirePlugin plugin, RodBase rodbase) {
		this(grenade, plugin, rodbase, SkillColors.POWER_COLOR.getEffect(null, null));
	}

	public MultipleProjectileHandler(Entity[] grenade, FerryEmpirePlugin plugin, RodBase rodbase, EfectGenerator effect) {
		assert plugin != null;
		this.entities = grenade;
		this.plugin = plugin;
		this.effect = effect;
		//random = new Random(data.getSkill().ordinal() ^ 0x58934523);
		this.rodbase = rodbase;
		oldLocation = new Location[grenade.length];
	}

	@Override
	public final void run() {

		if (timer-- <= 0) {
			if (!this.exploded) {
				this.explode();
				exploded = true;
			}
			this.removeGrenate();
			return;
		}
		for (int i = 0; i < entities.length; i++) {
			oldLocation[i] = entities[i].getLocation();

			if (!entities[i].isValid()) {
				this.removeGrenate();
				return;
			}
		}
		this.playEfect(oldLocation);
	}

	public void start(JavaPlugin plugin, int explotionTimer) {
		this.runTaskTimer(plugin, explotionTimer, explotionTimer);
	}

	private void removeGrenate() {
		this.cancel();
		this.remove(this.entities);
		this.playEfect(this.oldLocation);
		if(!exploded) {
			onFail();
		}
	}
	
	protected void onFail() {
	}

	public void remove(Entity[] grenade) {
		for (Entity ent : this.entities) {
			ent.remove();
		}
	}

	protected void playEfect(Location[] locs) {
		FireworkEffect ef = effect.getNewEffect(rodbase);
		for (Location loc : locs) {
			if (loc != null) {
				plugin.getfPlayer().playFirework(loc.getWorld(), loc, ef);
			}
		}
	}

	public void start() {
		this.start(plugin, 4);
	}

	protected abstract void explode();
}
