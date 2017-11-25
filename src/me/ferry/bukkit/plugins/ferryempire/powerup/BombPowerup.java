/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import java.util.Random;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

/**
 *
 * @author Fernando
 */
public class BombPowerup extends MetadataPowerup
{
	float power = 2.0f;

	public BombPowerup()
	{
		super(Egg.class);
	}

	@Override
	public void onRemoteBlockHit(FerryEmpirePlugin plugin, Player shooter, Location loc, Projectile ent, Random random, RodBase rodbase)
	{
		loc.getWorld().createExplosion(loc, power, true);
	}

	@Override
	public void loadFromConfig(ConfigurationSection from)
	{
		super.loadFromConfig(from);
		power = (float) from.getDouble("Power", power);

	}

	@Override
	public void writeToConfig(ConfigurationSection to)
	{
		super.writeToConfig(to);
		to.set("Power", power);
	}
}
