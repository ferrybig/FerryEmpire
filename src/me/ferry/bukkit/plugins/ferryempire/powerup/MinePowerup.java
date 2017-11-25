/*
 * Copyright (C) 2014 Fernando
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
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

/**
 *
 * @author Fernando
 */
public class MinePowerup extends BlockAreaPowerup {

	public MinePowerup() {
		super();
	}

	@Override
	protected void doBlockActions(FerryEmpirePlugin plugin, Player shooter, Location loc, List<Block> b, Random random, RodBase rodbase) {
		loc.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 5, 0.5f);
		plugin.getfPlayer().playFirework(loc.getWorld(), loc, FireworkEffect.builder().withColor(Color.BLACK, Color.GRAY, Color.WHITE).
				withFade(Color.BLACK, Color.GRAY, Color.WHITE).with(FireworkEffect.Type.BALL_LARGE).withTrail().build());
		for (Block block : b) {
			if (!block.isEmpty() && block.getType() != Material.BEDROCK
					&& block.getType() != Material.ENDER_PORTAL_FRAME
					&& block.getType() != Material.LAVA
					&& block.getType() != Material.WATER
					&& block.getType() != Material.STATIONARY_LAVA
					&& block.getType() != Material.STATIONARY_WATER) {
				BlockBreakEvent newEvent = new BlockBreakEvent(block, shooter);
				plugin.getServer().getPluginManager().callEvent(newEvent);
				if (newEvent.isCancelled()) {
					continue;
				}

				block.setType(Material.AIR);

			}
		}
	}
}
