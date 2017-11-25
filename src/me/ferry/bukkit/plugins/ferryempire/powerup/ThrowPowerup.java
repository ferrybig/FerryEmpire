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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

/**
 *
 * @author Fernando
 */
public class ThrowPowerup extends MetadataPowerup
{
	public ThrowPowerup()
	{
		super(Snowball.class);
	}

	@Override
	public void onRemoteBlockHit(FerryEmpirePlugin plugin, Player shooter, Location loc, Projectile ent, Random random, RodBase rodbase)
	{
		new Smoker(
			plugin, // plugin instance
			5, // how many blocks around it need to be smoke
			10, // time in ticks the smoke will last (1 sec = 20 ticks)
			1, // the low limit of smoke partcles every tick
			3, //the high limit of max smoke particles each tick
			loc
 // the location object where the smoke must form
			).start();
		Location playerLocation = shooter.getLocation();
		for (Entity player1 : loc.getWorld().getEntities())
		{
			final Location otherLocation = player1.getLocation();
			if (otherLocation.distanceSquared(loc) < 49)
			{
				if (player1 != shooter)
				{
					Vector vector = new Vector(random.nextGaussian()*0.3, 1.5, random.nextGaussian()*0.3);
					player1.setVelocity(vector);
					if (player1 instanceof Player)
					{
						((Player) player1).sendMessage(
							ChatColor.DARK_RED + "You have been pushed by " + ((Player) shooter).getName() + "!");
					}
				}
			}
		}
	}
}
