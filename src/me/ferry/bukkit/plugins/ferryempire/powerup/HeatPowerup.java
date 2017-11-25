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

import java.util.Random;
import me.ferry.bukkit.plugins.FireWorkProjectile;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author ferrybig
 */
public class HeatPowerup extends MetadataPowerup
{
	int radius = 2;
	public HeatPowerup()
	{
		super(Snowball.class,new EffectPlayer()
		{

			@Override
			public void playEffect(Entity entity, Player player, ItemStack item, RodData data, FerryEmpirePlugin plugin, RodBase roBase)
			{

				new FireWorkProjectile(entity){

					@Override
					public void explode(Location loc)
					{

					}

					@Override
					public void playEfect(Location loc)
					{
						loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, null);
					}

					@Override
					public void remove(Entity grenade)
					{

					}
				}.start(plugin, 1);
			}
		});
	}

	@Override
	public void onRemoteBlockHit(FerryEmpirePlugin plugin, Player shooter, Location loc, Projectile ent, Random random, RodBase rodbase)
	{
		ent.getLocation(loc);
		int offSetX = loc.getBlockX();
		int offSetY = loc.getBlockY();
		int offSetZ = loc.getBlockZ();
		World world = loc.getWorld();

		int startX = offSetX - radius;
		int startY = offSetY - radius;
		int startZ = offSetZ - radius;

		int endX = offSetX + radius;
		int endY = offSetY + radius;
		int endZ = offSetZ + radius;

		for (int counterX = startX; counterX <= endX; counterX++)
		{
			for (int counterZ = startZ; counterZ <= endZ; counterZ++)
			{
				for (int counterY = startY; counterY <= endY; counterY++)
				{
					Block block = world.getBlockAt(counterX, counterY, counterZ);
					Material oldType = block.getType();
					switch(block.getType()){
						case GRASS:
						case MYCEL:
							block.setType(Material.DIRT);
							break;
						case WATER:
						case STATIONARY_WATER:
						case SNOW:
						case SNOW_BLOCK:
							block.setType(Material.AIR);
							break;
						case ICE:
							block.setType(Material.WATER);
							break;
						case OBSIDIAN:
							block.setType(Material.LAVA);
							break;
						case LEAVES:
						case LADDER:
						case TORCH:
						case YELLOW_FLOWER:
						case LONG_GRASS:
							block.setType(Material.FIRE);
							break;
						case SAND:
							block.setType(Material.GLASS);
							break;
						case COBBLESTONE:
							block.setType(Material.NETHERRACK);
							break;
						case CLAY:
							block.setType(Material.BRICK);
							break;
					}
					if(oldType != block.getType())
					{
						block.getWorld().playEffect(block.getLocation(), Effect.MOBSPAWNER_FLAMES, null);
					}
				}
			}
		}
		Location playerLocation = shooter.getLocation();
		for (Entity player1 : loc.getWorld().getEntities())
		{
			final Location otherLocation = player1.getLocation();
			if (otherLocation.distanceSquared(loc) < radius*radius)
			{
				player1.setFireTicks(3);
			}
		}
	}
}