/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import java.util.Random;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.Smoker;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author Fernando
 */
public class SmokePowerup extends MetadataPowerup
{
	public SmokePowerup()
	{
		super(Egg.class);
	}

	@Override
	public void onRemoteBlockHit(FerryEmpirePlugin plugin, Player shooter, Location loc, Projectile ent, Random random, RodBase rodbase)
	{
		new Smoker(
			plugin, // plugin instance
			3, // how many blocks around it need to be smoke
			40, // time in ticks the smoke will last (1 sec = 20 ticks)
			5, // the low limit of smoke partcles every tick
			8, //the high limit of max smoke particles each tick
			loc
		// the location object where the smoke must form
		).start();
		loc.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 30, 1);
		for (Player player1 : plugin.getServer().getOnlinePlayers())
		{
			final Location playerLocation = player1.getLocation();
			if (playerLocation.getWorld() != loc.getWorld())
			{
				continue;
			}
			if (playerLocation.distanceSquared(loc) < 9) // 5 * 5 = 25
			{
				player1.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 5));
				player1.sendMessage(ChatColor.DARK_RED + "You have been smoked!");
			}
		}
	}
}
