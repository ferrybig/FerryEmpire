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

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;
import org.bukkit.entity.Player;

/**
 *
 * @author Fernando
 */
public class EMPPowerup extends BlockAreaPowerup {

	public EMPPowerup() {
		this.radius = 10;
	}

	@Override
	public void doBlockActions(FerryEmpirePlugin plugin, Player player, Location loc, List<Block> blocks, Random random, RodBase rodbase) {
		loc.getWorld().strikeLightningEffect(loc);
		for(Block b : blocks) {
			byte data = b.getData();
			switch (b.getType()) {
				case REDSTONE_COMPARATOR_ON: {
					b.setType(Material.REDSTONE_COMPARATOR_OFF);
					b.setData(data);
				}
				break;
				case REDSTONE_TORCH_ON: {
					b.setType(Material.REDSTONE_TORCH_OFF);
					b.setData(data);
				}
				break;
				case DIODE_BLOCK_ON: {
					b.setType(Material.DIODE_BLOCK_OFF);
					b.setData(data);
				}
				break;
				case REDSTONE_WIRE: {
					b.setData((byte) 0);
				}
				break;
			}
		}
	}
}
