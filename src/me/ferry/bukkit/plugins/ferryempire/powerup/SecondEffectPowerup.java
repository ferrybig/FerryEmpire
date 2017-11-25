/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.FireWorkProjectile;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.FireWorkProjectilePlayer;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import me.ferry.bukkit.plugins.ferryempire.Skill;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

/**
 *
 * @author Fernando
 */
public abstract class SecondEffectPowerup implements RemoteExplodePowerupAction {

	protected float yield;
	protected int fuse;
	
	protected Entity createProjectile(final FerryEmpirePlugin plugin, Player player) {
		Arrow arrow = player.launchProjectile(Arrow.class);
		arrow.setBounce(false);
		return arrow;
	}
	
	@Override
	public int doAction(Player player, ItemStack item, final RodData data, final FerryEmpirePlugin plugin, RodBase roBase) {
		final Entity arrow = createProjectile(plugin, player);
		new FireWorkProjectilePlayer(arrow, data, plugin, true, roBase).start();
		new FireWorkProjectile(arrow) {
			Location old;
			
			@Override
			public void remove(Entity grenade) {
				grenade.remove();
			}
			boolean fired = false;
			
			@Override
			public void playEfect(Location loc) {
				if (old != null) {
					if (old.getY() > loc.getY()) {
						if (!this.fired) {
							this.explode(loc);
						}
						arrow.remove();
					}
				}
				old = loc;
			}
			
			@Override
			public void explode(Location loc) {
				if (!this.fired) {
					this.fired = true;
					spawnNewProjectile(arrow, loc, plugin, data);
				}
				
			}
		}.start(plugin, 5);
		return -1;
	}
	
	protected void spawnNewProjectile(Entity orginal, Location loc, FerryEmpirePlugin plugin, RodData data) {
		TNTPrimed block = loc.getWorld().spawn(loc, TNTPrimed.class);
		block.setVelocity(orginal.getVelocity());
		block.setMetadata(Skill.class.getName(), new FixedMetadataValue(plugin, data.getSkill().name()));
		block.setFuseTicks(fuse);
		block.setYield(yield);
		block.setFireTicks(fuse);
	}
	
	@Override
	public void loadFromConfig(ConfigurationSection from) {
		yield = (float) from.getDouble("power", yield);
		fuse = from.getInt("fuse", fuse);
	}
	
	@Override
	public void writeToConfig(ConfigurationSection to) {
		to.set("power", yield);
		to.set("fuse", fuse);
	}
}
