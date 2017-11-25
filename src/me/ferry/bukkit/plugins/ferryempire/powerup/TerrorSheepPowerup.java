/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.BetterBukkitRunnable;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author Fernando
 */
public class TerrorSheepPowerup implements PowerupAction
{
	@Override
	public int doAction(Player player, ItemStack item, RodData data, FerryEmpirePlugin plugin, RodBase rodBase)
	{
		final Sheep terror = player.getWorld().spawn(player.getEyeLocation(), Sheep.class);
		terror.setColor(DyeColor.RED);
		terror.setMaxHealth(128); //128
		terror.setHealth(128); // 128
		terror.setCustomName(ChatColor.RED+"\u2622 TERROR SHEEP \u2622");
		terror.setCustomNameVisible(true);
		terror.setBreed(false);
		terror.setBaby();
		terror.setFireTicks(100);
		terror.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,100,10), true);
		terror.setVelocity(player.getLocation().getDirection().multiply(2.6));
		new  BetterBukkitRunnable()
		{
			@Override
			public void run()
			{
				terror.getWorld().createExplosion(terror.getEyeLocation(), 5.4f, true);
				terror.setHealth(0);
			}
		}.runTaskLater(plugin, 70);
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
