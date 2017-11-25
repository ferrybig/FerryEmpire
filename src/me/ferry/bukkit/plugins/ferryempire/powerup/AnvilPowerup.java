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
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.util.Vector;

/**
 *
 * @author Fernando
 */
public class AnvilPowerup extends MetadataPowerup {

	int blockCount = 20;

	public AnvilPowerup() {
		super(Snowball.class);
	}

	@Override
	public void onRemoteBlockHit(FerryEmpirePlugin plugin, Player shooter, Location loc, Projectile ent, Random random, RodBase rodbase) {
		List<FallingBlock> blocks = new ArrayList<>(blockCount);
		Listener anvilListener = new Listener() {
			@EventHandler
			public void onEvent(EntityChangeBlockEvent evt) {
				if (blocks.contains(evt.getEntity())) {
					loc.getWorld().playEffect(evt.getEntity().getLocation(), Effect.ANVIL_LAND, null);
					loc.getWorld().playSound(evt.getEntity().getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
					evt.setCancelled(true);
					for(Entity near : evt.getEntity().getNearbyEntities(0.5, 0.5, 0.5)) {
						if(near instanceof LivingEntity) {
							LivingEntity livingEntity = (LivingEntity) near;
							livingEntity.damage(4, evt.getEntity());
						}
					}
				}
				
			}
		};
		plugin.getServer().getPluginManager().registerEvents(anvilListener, plugin);
		loc.getWorld().playEffect(loc, Effect.ANVIL_LAND, null);
		loc.getWorld().playEffect(loc, Effect.ANVIL_LAND, null);
		loc.getWorld().playEffect(loc, Effect.ANVIL_LAND, null);
		loc.getWorld().playEffect(loc, Effect.ANVIL_LAND, null);
		loc.getWorld().playEffect(loc, Effect.STEP_SOUND, Material.ANVIL);
		loc.add(0, 0.1, 0);
		for(int i = 0; i < blockCount; i++) {
			FallingBlock anvil = loc.getWorld().spawnFallingBlock(loc, Material.ANVIL, (byte)2);
			anvil.setHurtEntities(true);
			anvil.setVelocity(new Vector(random.nextDouble() - 0.5, random.nextDouble(), random.nextDouble() - 0.5).multiply(0.7));
			anvil.setDropItem(false);
			blocks.add(anvil);
		}
		new BetterBukkitRunnable() {
			@Override
			public void run() {
				blocks.forEach(FallingBlock::remove);
				EntityChangeBlockEvent.getHandlerList().unregister(anvilListener);
			}
		}.runTaskLater(plugin, 120);
		
	}

}
