/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.GrenadeItem;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.FireWorkProjectilePlayer;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Fernando
 */
public class FragPowerup implements PowerupAction
{
	@Override
	public int doAction(Player player, ItemStack item, RodData data, FerryEmpirePlugin plugin, RodBase roBase)
	{
		World world = player.getWorld();
		final Item frag = world.dropItem(player.getEyeLocation(), new ItemStack(Material.FIREBALL));
		frag.setPickupDelay(55000); // so you cant pickup the item
		frag.setVelocity(player.getLocation().getDirection().normalize());
		frag.setTicksLived(55000); // item dies at 60000
		new GrenadeItem(frag)
		{
			@Override
			public void remove()
			{

			}

			@Override
			public void explode(Location loc)
			{
				frag.getLocation().getWorld().createExplosion(loc, 4.5f, false);
				frag.getLocation().getWorld().playSound(frag.getLocation(), Sound.BLOCK_ANVIL_FALL, 10, 1);
			}
		}.start(plugin, 10);
		new FireWorkProjectilePlayer(frag,data,plugin, false, roBase).start();

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
