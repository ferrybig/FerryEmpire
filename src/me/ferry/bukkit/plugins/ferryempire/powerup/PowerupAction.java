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

import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Fernando
 */
public interface PowerupAction
{
	/**
	 *
	 *
	 * @param player the value of player
	 * @param item the value of item
	 * @param data the value of data
	 * @param plugin the value of plugin
	 * @param rodBase
	 * @return -1 if the methode uses the defound cooldown calculation, -2 if it failed and an other integer for other cooldown calculations
	 */

	public abstract int doAction(Player player, ItemStack item, RodData data, FerryEmpirePlugin plugin, RodBase rodBase);





	public void loadFromConfig(ConfigurationSection from);

	public void writeToConfig(ConfigurationSection to);

}
