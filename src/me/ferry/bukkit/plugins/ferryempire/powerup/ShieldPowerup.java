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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import me.ferry.bukkit.plugins.ferryempire.Skill;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.EfectGenerator;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
/**
 *
 * @author Fernando
 */
public class ShieldPowerup extends ActiveSkillPowerup implements Listener {

	private Vector[] particleLocations;
	private boolean registered = false;
	private final Map<UUID, Long> protectedEntities = new HashMap<>();
	
	private final Map<UUID, Long> hits = new HashMap<>();

	public ShieldPowerup() {
		super(20);
	}
	
	@EventHandler
	private void onEvent(EntityDamageByEntityEvent evt) {
		if(evt.getDamager() instanceof Projectile && evt.getEntity() instanceof Player) {
			Player player = (Player)evt.getEntity();
			if(!protectedEntities.containsKey(player.getUniqueId())) {
				return;
			}
			long tick = protectedEntities.get(player.getUniqueId());
			if(tick < player.getWorld().getTime()) {
				protectedEntities.remove(player.getUniqueId());
				hits.remove(player.getUniqueId());
				return;
			}
			evt.setCancelled(true);
			long hit = hits.getOrDefault(player.getUniqueId(), 0l);
			hit++;
			hits.put(player.getUniqueId(), hit);
		}
	}
	
	public Vector[] getLocations() {
		if(particleLocations != null)
			return particleLocations;
		List<Vector> locations = new ArrayList<>();
		for (double i = 0.0; i < 360.0; i += 15) {
			double angle = i * Math.PI / 180;
            double x = (4 * Math.cos(angle));
            double z = (4 * Math.sin(angle));
     
			locations.add(new Vector(x, 0.7, z));
        }
		return particleLocations = locations.toArray(new Vector[locations.size()]);
	}
	
	@Override
	public int doInteractiveAction(Player player, int remainingPower, FerryEmpirePlugin plugin, RodBase rodBase, RodData data) {
		tryRegister(plugin);
		EfectGenerator effect = data.getSkill().getEffect(rodBase);
		Location orginal = player.getLocation();
		Location tmp = orginal.clone();
		for(Vector vector : getLocations()) {
			tmp.setX(vector.getX() + orginal.getX());
			tmp.setY(vector.getY() + orginal.getY());
			tmp.setZ(vector.getZ() + orginal.getZ());
			plugin.getParticles().sendToPlayer("DRIP_LAVA", tmp, 0, 1, 0, 1, 10, player.getWorld().getEntitiesByClass(Player.class).toArray(new Player[0]));
			//plugin.getfPlayer().playFirework(orginal.getWorld(), tmp, effect.getNewEffect(rodBase));
		}
		protectedEntities.put(player.getUniqueId(), player.getWorld().getTime() + 25);
		long hitpoints = hits.getOrDefault(player.getUniqueId(), 0l);
		hits.remove(player.getUniqueId());
		return (int) (1 + hitpoints);
	}

	@Override
	protected Skill getSkill() {
		return Skill.SHIELD;
	}
	
	private void tryRegister(FerryEmpirePlugin plugin) {
		if(registered)
			return;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		registered = true;
	}
}
