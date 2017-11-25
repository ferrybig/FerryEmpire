/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import java.util.Random;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;

/**
 *
 * @author Fernando
 */
public class FreezePowerup extends MetadataPowerup
{
	public FreezePowerup()
	{
		super(Snowball.class);
	}

	@Override
	public void onRemoteBlockHit(FerryEmpirePlugin plugin, Player shooter, Location loc, Projectile ent, Random random, RodBase rodbase)
	{
		int radius = 6;
		loc.getWorld().playSound(loc, Sound.BLOCK_GRASS_PLACE, 5, 0.1f);
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
				boolean lastSolid = false;
				for (int counterY = startY; counterY <= endY; counterY++)
				{

					Block block = world.getBlockAt(counterX, counterY, counterZ);

					switch (block.getTypeId())
					{
						case 0:
							if (lastSolid)
							{
								block.setType(Material.SNOW);
								lastSolid = false;
							}
							break;
						case 6:
							block.setType(Material.DEAD_BUSH);
							lastSolid = false;
							break;
						case 8:
						case 9:
							if (block.getData() == 0)
							{
								loc.getWorld().playEffect(block.getLocation(), Effect.EXTINGUISH, 1);
								block.setType(Material.ICE);
							}
							
							lastSolid = false;
							break;
						case 10:
						case 11:
							if (block.getData() == 0)
							{
								block.setType(Material.OBSIDIAN);
							}
							else
							{
								block.setType(Material.COBBLESTONE);
							}
							loc.getWorld().playEffect(block.getLocation(), Effect.EXTINGUISH, 1);
							lastSolid = true;
							break;
						case 78:
							if (block.getData() != 7)
							{
								block.setData((byte) (block.getData() + 1));
								lastSolid = false;
							}
							break;
						default:
							lastSolid = true;
					}
				}
			}
		}
	}

	@Override
	public void loadFromConfig(ConfigurationSection from)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void writeToConfig(ConfigurationSection to)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
