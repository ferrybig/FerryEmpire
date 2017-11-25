/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import java.lang.ref.WeakReference;
import java.util.List;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.EfectGenerator;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/**
 *
 * @author Fernando
 */
public class LoveWavePowerup extends TrailPowerup
{
	public static final Vector VECTOR_ZERO = new Vector(0, 0, 0);
	@SuppressWarnings("unchecked")
	private WeakReference<LivingEntity>[] buffer = (WeakReference<LivingEntity>[]) new WeakReference<?>[10];
	int check = 0;
	int latestSlot = 0;

	public LoveWavePowerup()
	{
		this.trailName = "Love Wave";
		this.maxLength = 25;
		this.blockSkip = 0;
		this.blockStart =0;
	}

	@Override
	public void runTrail(Location loc, Player player, FerryEmpirePlugin plugin, EfectGenerator effect, RodBase rodbase)
	{
		for (Entity ent : playLoveEffect(loc).getNearbyEntities(2, 2, 2))
		{
			if (ent instanceof LivingEntity)
			{
				try
				{
					LivingEntity entity = (LivingEntity) ent;
					if (entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 20, 0), true))
					{
						plugin.getfPlayer().playFirework(loc.getWorld(), entity.getEyeLocation(), effect.getNewEffect(rodbase));
					}	
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		Vector dir = loc.getDirection();
		dir = new Vector(dir.getZ(), dir.getY(), dir.getX());
		loc.add(dir);
		playLoveEffect(loc);
		dir.multiply(-2);
		loc.add(dir);
		playLoveEffect(loc);



	}

	private Entity playLoveEffect(Location loc) throws IllegalArgumentException
	{
		check++;
		if (check >= 100)
		{
			check = 0;
			List<World> worlds = Bukkit.getWorlds();
			for (int i = 0; i < buffer.length; i++)
			{
				if (buffer[i] == null)
				{
					continue;
				}
				Entity entity = buffer[i].get();
				if (entity == null)
				{
					buffer[i] = null;
					continue;
				}
				if (!worlds.contains(entity.getWorld()))
				{
					buffer[i] = null;
				}
			}
		}
		LivingEntity closestEntity = null;
		double distance = Double.POSITIVE_INFINITY;
		for (int i = 0; i < buffer.length; i++)
		{
			if (buffer[i] == null)
			{
				continue;
			}
			LivingEntity entity = buffer[i].get();
			if (entity == null)
			{
				buffer[i] = null;
				continue;
			}
			if (!entity.isValid())
			{
				buffer[i] = null;
				continue;
			}
			if(entity.getTicksLived() > 40)
			{
				entity.remove();
			}
			if (entity.getWorld().getUID().equals(loc.getWorld().getUID()))
			{
				double distance1 = entity.getLocation().distanceSquared(loc);
				if (distance1 < distance)
				{
					closestEntity = entity;
					distance = distance1;
				}
			}
		}
		if (closestEntity == null)
		{
			if (latestSlot >= buffer.length)
			{
				latestSlot = 0;
			}
			int index = latestSlot;
			latestSlot++;
			if (buffer[index] != null)
			{
				Entity entity = buffer[index].get();
				if (entity != null)
				{
					entity.remove();
				}
			}
			closestEntity = loc.getWorld().spawn(loc, Wolf.class);
			closestEntity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, true), true);
			closestEntity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, Integer.MAX_VALUE, 0, true), true);
			//closestEntity.setFireTicks(Integer.MAX_VALUE);
			closestEntity.setHealth(3);
			closestEntity.setMaxHealth(3);
			closestEntity.setFallDistance(-10);
			closestEntity.setCanPickupItems(false);
			buffer[index] = new WeakReference<LivingEntity>(closestEntity);
		}
		else
		{
			if (closestEntity.hasPotionEffect(PotionEffectType.REGENERATION))
			{
				closestEntity.removePotionEffect(PotionEffectType.REGENERATION);
			}

			closestEntity.setHealth(3);
			closestEntity.setVelocity(VECTOR_ZERO);
		}
		closestEntity.teleport(loc);
		closestEntity.playEffect(EntityEffect.WOLF_HEARTS);
		closestEntity.setTicksLived(1);
		//closestEntity.playEffect(EntityEffect.WOLF_HEARTS);
		//closestEntity.playEffect(EntityEffect.WOLF_HEARTS);
		return closestEntity;
	}
}
