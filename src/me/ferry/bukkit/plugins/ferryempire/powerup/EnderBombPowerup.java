/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.FlyingBlock;
import me.ferry.bukkit.plugins.SafeBlockIterator;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 *
 * @author Fernando
 */
public class EnderBombPowerup implements PowerupAction
{
	@Override
	public int doAction(final Player player, ItemStack item, RodData data, FerryEmpirePlugin plugin, RodBase rodBase)
	{
		final Location target1 = SafeBlockIterator.getTargetBlock(plugin, player, 100).getLocation();
		final Location playerLoc = player.getEyeLocation();
		Vector subtract = target1.toVector().subtract(playerLoc.toVector());
		final double length = subtract.length() / 2;
		//if (length < 6)
		//{
		//	player.sendMessage(ChatColor.DARK_RED + rodBase.getRodName() + ChatColor.DARK_GREEN + " Target to close!");
		//	return -2;
		//}
		final Location middle = target1.clone().add(playerLoc).multiply(0.5);
		middle.add(0, length + player.getEyeHeight(), 0);
		final FallingBlock fblock = middle.getWorld().spawnFallingBlock(playerLoc, Material.DRAGON_EGG, (byte) 0);
		new FlyingBlock(fblock, middle, 200, new FlyingBlock.Handler()
		{
			boolean lastStep = false;
			int timer = 0;
			Location oldLoc = null;
			Location tmp = new Location(null, 0, 0, 0);

			@Override
			public void onTick(FallingBlock ent, Location target)
			{
				ent.getLocation(tmp);
				if (lastStep)
				{
					if (target.distanceSquared(tmp) < 5 * 5)
					{
						ent.getLocation(target);
						ent.getWorld().createExplosion(target, 4.0f, false);
						ent.remove();
					}
				}
				else
				{
					assert !lastStep;
					if (target.distanceSquared(tmp) < 5 * 5)
					{
						target.setX(target1.getX());
						target.setY(target1.getY());
						target.setZ(target1.getZ());
						lastStep = true;
					}
				}

				if (oldLoc != null)
				{
					if (tmp.getBlockX() == oldLoc.getBlockX() || tmp.getBlockY() == oldLoc.getBlockY() || tmp.getBlockZ() == oldLoc.getBlockZ())
					{
						ent.getWorld().createExplosion(target, 4.0f, false);
						ent.remove();
					}
				}
				else
				{
					ent.getLocation(oldLoc);
				}
				target.getWorld().playEffect(tmp, Effect.STEP_SOUND, Material.OBSIDIAN);
			}
		}).start(plugin);
		FallingBlock lastBlock = fblock;
		for (int i = 0; i < 3; i++)
		{
			final int counter = i;
			final FallingBlock bl = lastBlock;
			lastBlock = middle.getWorld().spawnFallingBlock(playerLoc, Material.DRAGON_EGG, (byte) 0);
			new FlyingBlock(lastBlock, middle.clone(), 200, new FlyingBlock.Handler()
			{
				Location oldLoc = null;
				int timer;

				@Override
				public void onTick(FallingBlock ent, Location target)
				{
					bl.getLocation(target);
					if (!bl.isValid())
					{
						if (playerLoc.distanceSquared(target) < 5 * 5)
						{
							if (timer++ < 40)
							{
								return;
							}
						}
						ent.getWorld().createExplosion(target, 4.0f - counter, false);
						ent.remove();
					}

				}
			}).start(plugin);
		}
		return -1;
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
