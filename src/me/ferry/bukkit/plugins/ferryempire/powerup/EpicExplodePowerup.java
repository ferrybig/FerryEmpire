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
import me.ferry.bukkit.plugins.BetterBukkitRunnable;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

/**
 *
 * @author ferrybig
 */
public class EpicExplodePowerup extends MetadataPowerup
{

	public static final int INCREASE_FACTOR = 3;
	public static final int TIMING_FACTOR = 4;

	public EpicExplodePowerup()
	{
		super(Snowball.class);
	}

	@Override
	public void onRemoteBlockHit(final FerryEmpirePlugin plugin, Player shooter, final Location loc, Projectile ent, final Random random, RodBase rodbase)
	{
		new BetterBukkitRunnable()
		{
			Location[] locs = new Location[]
			{
				loc
			};
			int loopNumber;

			@Override
			public void run()
			{
				if (loopNumber < TIMING_FACTOR + 1)
				{
					for (Location location : locs)
					{
						EpicExplodePowerup.this.applyEffect(location, plugin);
					}
					if (loopNumber != TIMING_FACTOR)
					{
						Location[] newLocs = new Location[locs.length * 3];
						for (int i = 0; i < locs.length; i++)
						{
							for (int j = 0; j < INCREASE_FACTOR; j++)
							{
								Location l = locs[i];
								if (j != (INCREASE_FACTOR - 1))
								{
									l = l.clone();
								}
								final Location workingLocation = newLocs[i * INCREASE_FACTOR + j] = l;
								double radius = TIMING_FACTOR - loopNumber;
								double randomDegree = random.nextDouble() * 2 * Math.PI;
								double x = Math.cos(randomDegree);
								double z = Math.sin(randomDegree);
								x *= radius;
								z *= radius;
								workingLocation.add(x, 0, z);
								for (int tmp = 0; tmp < 2; tmp++)
								{
									if (!workingLocation.getBlock().getType().isSolid())
									{
										workingLocation.add(0, -1, 0);
									}
								}
								for (int tmp = 0; tmp < 3; tmp++)
								{
									if (workingLocation.getBlock().getType().isSolid())
									{
										workingLocation.add(0, 1, 0);
									}
								}
							}
						}
						this.locs = newLocs;
					}
					else
					{
						Location totalLocation = new Location(loc.getWorld(), 0, 0, 0);

						for (Location location : locs)
						{
							totalLocation.add(location);

						}
						totalLocation.multiply(1.0 / locs.length);
						totalLocation.getWorld().createExplosion(totalLocation, 5.0f, true);
						final Vector vector = totalLocation.toVector();
						for (Location location : locs)
						{
							if (random.nextBoolean())
							{
								continue;
							}
							final FallingBlock block = totalLocation.getWorld().spawnFallingBlock(location.add(0, 7.1, 0), Material.FIRE, (byte) 0);
							block.setVelocity(location.toVector().subtract(vector).normalize().multiply(0.7));
							block.setDropItem(false);
						}
					}
					loopNumber++;
				}
				else
				{
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 7);
	}

	private void applyEffect(Location loc, FerryEmpirePlugin plugin)
	{
		loc.getWorld().strikeLightning(loc);
		try
		{
			plugin.getfPlayer().playFirework(loc.getWorld(), loc, FireworkEffect.builder()
				.withColor(Color.fromRGB(0x00ffff),Color.fromRGB(0x00eeee))
				.withFade(Color.fromRGB(0xffff00),Color.fromRGB(0xeeee00))
				.withFlicker()
				.with(FireworkEffect.Type.BURST)
				.build());
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}

}
