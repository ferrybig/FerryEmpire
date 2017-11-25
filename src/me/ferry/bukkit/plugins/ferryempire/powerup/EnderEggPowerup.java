/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.FlyingBlock;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Fernando
 */
public class EnderEggPowerup implements PowerupAction
{
	@Override
	public int doAction(final Player player, ItemStack item, RodData data, FerryEmpirePlugin plugin, RodBase rodBase)
	{
		if (player.getVehicle() != null)
		{
			plugin.sendMessage(player, ChatColor.DARK_RED + rodBase.getRodName() + ChatColor.DARK_GREEN + " You cant ride a dragon egg at this time");
			return -2;
		}
		FallingBlock bl = player.getWorld().spawnFallingBlock(player.getEyeLocation(), Material.DRAGON_EGG, (byte) 0);
		bl.setPassenger(player);
		new FlyingBlock(bl, player.getEyeLocation().add(0, 10, 0), 600, new FlyingBlock.Handler()
		{
			Location tmp = new Location(null, 0, 0, 0);

			@Override
			public void onTick(FallingBlock ent, Location target)
			{

				target.add(player.getLocation(tmp).getDirection());
				target.getWorld().playEffect(target, Effect.MOBSPAWNER_FLAMES, 50);
				if (player.getVehicle() != ent)
				{
					ent.remove();
				}
				if (target.distanceSquared(tmp) > 100)
				{
					ent.getLocation(target);
				}
				if (player.getEyeLocation().getBlock().getType().isSolid())
				{
					if (tmp.getY() - target.getY() < 2)
					{
						target.add(0, -0.5, 0);
					}
				}
				target.getWorld().playEffect(ent.getLocation(), Effect.ENDER_SIGNAL, 0);
			}
		}).start(plugin);

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
