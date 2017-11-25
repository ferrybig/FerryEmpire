/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.BetterBukkitRunnable;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.EfectGenerator;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author ferrybig
 */
public class GrenadePowerup implements PowerupAction
{

	@Override
	public int doAction(Player player, ItemStack item, final RodData data, final FerryEmpirePlugin plugin, final RodBase rodBase)
	{
		final Item tnt = player.getWorld().dropItem(player.getEyeLocation(),new ItemStack(Material.TNT));
		tnt.setPickupDelay(2147483647);
		tnt.setVelocity(player.getLocation().getDirection().multiply(1));
		new BetterBukkitRunnable()
		{
			int timer = 0;
			final EfectGenerator effect = data.getSkill().getEffect(rodBase);

			@Override
			public void run()
			{
				if (this.timer < 7)
				{
					tnt.getWorld().playSound(tnt.getLocation(), Sound.ITEM_FIRECHARGE_USE, 5.0F, 4.0F);
					tnt.getWorld().playEffect(tnt.getLocation(), Effect.STEP_SOUND, Material.FIRE);
					tnt.getWorld().playEffect(tnt.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_WIRE);
				}
				else
				{
					cancel();
					tnt.getWorld().createExplosion(tnt.getLocation(), 5.0F);
					try
					{
						plugin.getfPlayer().playFirework(tnt.getWorld(), tnt.getLocation(), effect.getNewEffect(rodBase));
						tnt.remove();
					}
					catch (Exception e)
					{
						throw new RuntimeException(e);
					}

				}
				this.timer += 1;
			}
		}.runTaskTimer(plugin, 20L, 10L);
		return -1;
	}

	@Override
	public void writeToConfig(ConfigurationSection to)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void loadFromConfig(ConfigurationSection from)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
