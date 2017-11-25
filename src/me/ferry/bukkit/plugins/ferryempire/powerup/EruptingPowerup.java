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

import java.util.List;
import java.util.Random;
import me.ferry.bukkit.plugins.BetterBukkitRunnable;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import me.ferry.bukkit.plugins.ferryempire.Skill;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.bukkit.Material;

/**
 *
 * @author Fernando
 */
public class EruptingPowerup extends SecondEffectPowerup {
	
	public EruptingPowerup() {
		this.yield = 4;
		this.fuse = 80;
	}

	@Override
	protected void spawnNewProjectile(Entity orginal, Location loc, FerryEmpirePlugin plugin, RodData data) {
		TNTPrimed block = loc.getWorld().spawn(loc, TNTPrimed.class);
		block.setVelocity(orginal.getVelocity());
		block.setMetadata(Skill.class.getName(), new FixedMetadataValue(plugin, data.getSkill().name()));
		block.setFuseTicks(fuse / 2);
		block.setYield(yield);
		block.setFireTicks(fuse / 2);
		Random random = new Random();
		new BetterBukkitRunnable() {
			@Override
			public void run() {
				if(!block.isValid()) {
					cancel();
					return;
				}
				if(block.getFuseTicks() < 15) {
					Vector orginal = block.getVelocity();
					block.setVelocity(new Vector(random.nextDouble() - 0.5,10,random.nextDouble() - 0.5).multiply(0.1).add(orginal));
					EruptingPowerup.super.spawnNewProjectile(block, block.getLocation(), plugin, data);
					block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, Material.TNT);
					block.setVelocity(orginal);
				}
				if(block.getFuseTicks() < 2) {
					block.remove();
				}
			}
		}.runTaskTimer(plugin, 1, 1);
	}

	@Override
	public void onRemoteExplode(FerryEmpirePlugin plugin, Location loc, Entity ent, Random random, List<Block> blocks, RodBase rodbase) {
		
	}
	
}
