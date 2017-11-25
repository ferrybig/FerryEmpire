/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.BetterBukkitRunnable;
import me.ferry.bukkit.plugins.SafeBlockIterator;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.EfectGenerator;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Fernando
 */
public class CapturePowerup implements PowerupAction
{
	private int distance = 100;
	private int fireworkDistance = 10;
	private int radius = 2;

	@Override
	public int doAction(final Player player, ItemStack item, RodData data, FerryEmpirePlugin plugin, RodBase rodBase)
	{
		Iterable<Entity> loca;
		if (player.getPassenger() != null)
		{
			player.getPassenger().setVelocity(player.getLocation().getDirection().multiply(3));
			Location loc = player.getPassenger().getLocation();
			loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 50);
			loc.getWorld().playEffect(loc, Effect.EXTINGUISH, 0);
			loc.getWorld().playSound(loc, Sound.ENTITY_ENDERMEN_TELEPORT, 2.0F, 1.0F);
			player.eject();
			return -1;
		}
		EfectGenerator effect = data.getSkill().getEffect(rodBase);
		SafeBlockIterator iterator = new SafeBlockIterator(player, distance);
		loca = player.getNearbyEntities(distance,distance,distance);
		int i = 0;
		while (iterator.hasNext())
		{
			Block item1 = iterator.next();

			if (i++ % fireworkDistance == fireworkDistance - 1)
			{
				try
				{
					plugin.getfPlayer().playFirework(player.getWorld(), item1.getLocation().add(0.5, 0.5, 0.5), effect.getNewEffect(rodBase));
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
			Location tmpLoc = new Location(null,0,0,0);
			for (Entity entity : loca)
			{
				if (entity != player)
				{
					for (int x = -radius; x < radius; x++)
					{
						for (int z = -radius; z < radius; z++)
						{
							for (int y = -radius; y < radius; y++)
							{
								Location l = entity.getLocation(tmpLoc).add(x, y, z);
								if (l.getBlockX() == item1.getX() && l.getBlockY() == item1.getY() && l.getBlockZ() == item1.getZ() && (entity instanceof LivingEntity))
								{
									int loop = 0;
									{
										while (loop < 20 && entity.getVehicle() != null && entity.getVehicle() instanceof LivingEntity)
										{
											loop++;
											entity = (LivingEntity) entity.getVehicle();
										}
									}
									loop = 0;
									{
										Entity tmp = entity;
										while (loop < 20 && tmp != null)
										{
											loop++;
											if (tmp.getPassenger() == player)
											{
												tmp.getPassenger().eject();
												break;
											}
											tmp = tmp.getPassenger();
										}
									}

									player.setPassenger(entity);
									Location loc = player.getPassenger().getLocation();
									loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 50);
									loc.getWorld().playEffect(loc, Effect.EXTINGUISH, 0);
									loc.getWorld().playSound(loc, Sound.ENTITY_ENDERMEN_TELEPORT, 2.0F, 1.0F);
									new BetterBukkitRunnable()
									{
										@Override
										public void run()
										{
											if (player.getPassenger() == null || !player.isValid())
											{
												cancel();
												return;
											}
											player.getWorld().playEffect(player.getPassenger().getLocation().add(0, player.getEyeHeight(), 0),
														     Effect.ENDER_SIGNAL, 0);
										}
									}.runTaskTimer(plugin, 1, 2);
									return -1;
								}
							}
						}
					}
				}
			}
			if (i > this.distance)
			{
				player.sendMessage(ChatColor.DARK_RED + rodBase.getRodName() + ChatColor.DARK_GREEN + " No target found!");
				return -2;
			}
			if (plugin.getBlockUtil().isSolid(item1.getTypeId()))
			{
				player.sendMessage(ChatColor.DARK_RED + rodBase.getRodName() + ChatColor.DARK_GREEN + " No target found!");
				return -2;
			}
		}
		player.sendMessage(ChatColor.DARK_RED + rodBase.getRodName() + ChatColor.DARK_GREEN + " No target found!");
		return -2;
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
