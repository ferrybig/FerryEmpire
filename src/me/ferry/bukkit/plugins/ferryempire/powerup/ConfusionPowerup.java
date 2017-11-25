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
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author Fernando
 */
public class ConfusionPowerup extends MetadataPowerup
{
	int fireworkRadius = 2;
	int effectRadius = 4;
	public ConfusionPowerup()
	{
		super(Egg.class);
	}

	/**
	 *
	 *
	 * @param plugin the value of plugin
	 * @param shooter the value of shooter
	 * @param loc the value of loc
	 * @param ent the value of ent
	 * @param rodbase the value of rodbase
	 */

	@Override
	public void onRemoteBlockHit(FerryEmpirePlugin plugin, Player shooter, Location loc, Projectile ent, Random random, RodBase rodbase)
	{
		for (Player player1 : shooter.getServer().getOnlinePlayers())
		{
			final Location playerLocation = player1.getLocation();
			if (playerLocation.getWorld() != loc.getWorld())
			{
				continue;
			}
			if (playerLocation.distanceSquared(loc) < effectRadius * effectRadius)
			{
				player1.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 700, 5));
				player1.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 600, 5));
				player1.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 5));
				player1.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 400, 5));
				player1.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 5));
				player1.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 200, 1));
				player1.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 2));
				player1.sendMessage(ChatColor.DARK_RED + "You have been confused!");
				new Smoker(
					plugin, // plugin instance
					2, // how many blocks around it need to be smoke
					10, // time in ticks the smoke will last (1 sec = 20 ticks)
					1, // the low limit of smoke partcles every tick
					20, //the high limit of max smoke particles each tick
					player1.getLocation() // the location object where the smoke must form
					).start();
				player1.getWorld().playSound(player1.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 10, 1);
			}
			try
			{
				plugin.getfPlayer().playFirework(loc.getWorld(),loc,
					FireworkEffect.builder()
					.with(FireworkEffect.Type.BURST)
					.withColor(Color.fromRGB(3080239))
					.withColor(Color.fromRGB(4718664))
					.withFade(Color.fromRGB(0)).build());
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			int offSetX = loc.getBlockX();
			int offSetY = loc.getBlockY();
			int offSetZ = loc.getBlockZ();
			World world = loc.getWorld();

			int startX = offSetX - fireworkRadius;
			int startY = offSetY - fireworkRadius;
			int startZ = offSetZ - fireworkRadius;

			int endX = offSetX + fireworkRadius;
			int endY = offSetY + fireworkRadius;
			int endZ = offSetZ + fireworkRadius;

			Location tmp = new Location(loc.getWorld(),0,0,0);
			for (int counterX = startX; counterX <= endX; counterX++)
			{
				for (int counterY = startY; counterY <= endY; counterY++)
				{
					for (int counterZ = startZ; counterZ <= endZ; counterZ++)
					{
						if (((counterX - offSetX) ^ 2 + (counterY - offSetY) ^ 2 + (counterZ - offSetZ) ^ 2) <= 4)
						{
							tmp.setX(counterX);
							tmp.setY(counterY);
							tmp.setZ(counterZ);
							world.playEffect(tmp, Effect.SMOKE, BlockFace.UP);
						}
					}
				}
			}
		}
	}

	@Override
	public void loadFromConfig(ConfigurationSection from)
	{
		super.loadFromConfig(from);
		fireworkRadius = from.getInt("SmokeRadius", fireworkRadius);
		effectRadius = from.getInt("EffectRadius", effectRadius);
	}

	@Override
	public void writeToConfig(ConfigurationSection to)
	{
		super.writeToConfig(to);
		to.set("SmokeRadius", fireworkRadius);
		to.set("EffectRadius", effectRadius);
	}

}
