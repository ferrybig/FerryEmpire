/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import java.util.Random;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.FireWorkProjectilePlayer;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import me.ferry.bukkit.plugins.ferryempire.Skill;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

/**
 *
 * @author Fernando
 */
public class ArrowPowerup implements RemoteHitPowerupAction
{
	private final boolean onFire;
	protected boolean removeOnHit = true;
	protected int fireTicks = 2000;

	public ArrowPowerup(boolean onFire)
	{
		this.onFire = onFire;
	}

	/**
	 *
	 *
	 * @param player the value of player
	 * @param item the value of item
	 * @param data the value of data
	 * @param plugin the value of plugin
	 * @param rodBase the value of roBase
	 * @return the int
	 */
	@Override
	public int doAction(Player player, ItemStack item, RodData data, me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin plugin, me.ferry.bukkit.plugins.ferryempire.RodBase rodBase)
	{
		Arrow arrow = player.launchProjectile(Arrow.class);
		arrow.setBounce(false);
		if (onFire)
		{
			arrow.setFireTicks(fireTicks);
		}
		arrow.setMetadata(Skill.class.getName(), new FixedMetadataValue(plugin, data.getSkill().name()));
		new FireWorkProjectilePlayer(arrow, data, plugin, rodBase).start();
		return -1;
	}

	@Override
	public void onRemoteBlockHit(FerryEmpirePlugin plugin, Player shooter, Location loc, Projectile ent, Random random, RodBase rodbase)
	{
		if (this.removeOnHit)
		{
			ent.remove();
		}
	}

	@Override
	public void loadFromConfig(ConfigurationSection from)
	{
		if (onFire)
		{
			fireTicks = from.getInt("FireTicks", fireTicks);
		}
		this.removeOnHit = from.getBoolean("RemoveOnHit", removeOnHit);
	}

	@Override
	public void writeToConfig(ConfigurationSection to)
	{
		if (onFire)
		{
			to.set("FireTicks", fireTicks);
		}
		to.set("RemoveOnHit", removeOnHit);
	}
}
