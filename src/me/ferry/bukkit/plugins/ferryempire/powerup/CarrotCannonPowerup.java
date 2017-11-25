/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.FireWorkProjectile;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Fernando
 */
public class CarrotCannonPowerup implements PowerupAction
{
	@Override
	public int doAction(Player player, ItemStack item, RodData data, FerryEmpirePlugin plugin, RodBase rodBase)
	{
		ItemStack carrot = new ItemStack(Material.CARROT_ITEM);
		ItemMeta meta = carrot.getItemMeta();
		meta.setDisplayName("Do not pickup. "+Math.random());
		carrot.setItemMeta(meta);
		Item item1 = player.getWorld().dropItem(player.getEyeLocation(), carrot);
		item1.setPickupDelay(Integer.MAX_VALUE);
		//item1.setTicksLived(60000 - 300);
		item1.setVelocity(player.getLocation().getDirection().multiply(3).add(player.getVelocity()));

		new FireWorkProjectile(item1) {

			@Override
			public void explode(Location loc)
			{
				TNTPrimed tnt = loc.getWorld().spawn(loc, TNTPrimed.class);
				tnt.setFuseTicks(0);
				tnt.setYield(4.0f);
			}

			@Override
			public void remove(Entity grenade)
			{
				grenade.remove();
			}

			@Override
			public void playEfect(Location loc)
			{
				loc.getWorld().playEffect(loc, Effect.STEP_SOUND, Material.LAVA);
			}

		}.start(plugin, 1);

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
