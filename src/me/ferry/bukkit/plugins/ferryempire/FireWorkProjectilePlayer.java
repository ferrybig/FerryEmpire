/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire;

import me.ferry.bukkit.plugins.FireWorkProjectile;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.EfectGenerator;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 *
 * @author Fernando
 */
public class FireWorkProjectilePlayer extends FireWorkProjectile {

	private final RodData data;
	private final FerryEmpirePlugin plugin;
	private final boolean mayRemove;
	private final EfectGenerator effect;

	/**
	 *
	 * @param grenade the value of grenade
	 * @param data the value of data
	 * @param plugin the value of plugin
	 * @param mayRemove the value of mayRemove
	 * @param rodbase the value of rodbase
	 */
	public FireWorkProjectilePlayer(Entity grenade, RodData data, FerryEmpirePlugin plugin, boolean mayRemove, RodBase rodbase) {

		super(grenade);
		assert data != null;
		assert plugin != null;

		this.data = data;
		this.plugin = plugin;
		this.mayRemove = mayRemove;
		effect = data.getSkill().getEffect(rodbase);
		//random = new Random(data.getSkill().ordinal() ^ 0x58934523);
	}

	/**
	 *
	 * @param grenade the value of grenade
	 * @param data the value of data
	 * @param plugin the value of plugin
	 * @param rodbase the value of rodbase
	 */
	public FireWorkProjectilePlayer(Entity grenade, RodData data, FerryEmpirePlugin plugin, RodBase rodbase) {
		this(grenade, data, plugin, true, rodbase);
	}
	boolean failed = false;

	@Override
	public void remove(Entity grenade) {
		if (this.mayRemove) {
			grenade.remove();
		}
	}

	@Override
	public void playEfect(Location loc) {
		try {
			FireworkEffect ef = effect.getNewEffect(null);
			assert ef != null;
			assert plugin.getfPlayer() != null;
			assert loc != null;
			assert loc.getWorld() != null;
			plugin.getfPlayer().playFirework(loc.getWorld(), loc, new FireworkEffect[]{ef});
		} catch (Exception ex) {
			if (failed == false) {
				ex.printStackTrace();
				failed = true;
			}
		}
	}

	@Override
	public void explode(Location loc) {
	}

	public void start() {
		this.runTaskTimer(plugin, 12, 4);
	}
}
