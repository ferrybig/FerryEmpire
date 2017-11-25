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
public class FarmGrowPowerup extends MetadataPowerup
{
	int radius = 10;

	public FarmGrowPowerup()
	{
		super(Snowball.class);
	}

	@Override
	public void onRemoteBlockHit(FerryEmpirePlugin plugin, Player shooter, Location loc, Projectile ent, Random random, RodBase rodbase)
	{
		loc.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 5, 0.5f);
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
		Location tmp = new Location(null, 0, 0, 0);
		for (int counterX = startX; counterX <= endX; counterX++)
		{
			for (int counterY = startY; counterY <= endY; counterY++)
			{
				for (int counterZ = startZ; counterZ <= endZ; counterZ++)
				{
					Block block = world.getBlockAt(counterX, counterY, counterZ);
					switch (block.getTypeId())
					{
						case 59:
						case 104:
						case 105:
						case 141:
						case 142:
							block.setData((byte) 7);
							world.playEffect(block.getLocation(tmp), Effect.MOBSPAWNER_FLAMES, 0);
							break;
						case 73:
							block.setData((byte) 3);
							world.playEffect(block.getLocation(tmp), Effect.MOBSPAWNER_FLAMES, 0);
							break;
					}
				}
			}
		}
	}

	@Override
	public void loadFromConfig(ConfigurationSection from)
	{
		radius = from.getInt("Radius", radius);
	}

	@Override
	public void writeToConfig(ConfigurationSection to)
	{
		to.set("Radius", radius);
	}
}
