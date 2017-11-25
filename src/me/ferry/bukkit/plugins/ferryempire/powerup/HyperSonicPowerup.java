/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

/**
 *
 * @author Fernando
 */
public class HyperSonicPowerup extends SecondEffectPowerup
{
	public HyperSonicPowerup()
	{
		this.fuse = 100;
		this.yield = 10.0f;
	}
	
	@Override
	public void onRemoteExplode(FerryEmpirePlugin plugin, Location loc, Entity ent, Random random, List<Block> block, RodBase rodbase)
	{
		Iterator<Block> blocks = block.iterator();
		int spawned = 0;
		loc.getWorld().playSound(loc, Sound.ENTITY_IRONGOLEM_ATTACK, 10, 10);
		for (Entity e : ent.getNearbyEntities(7, 10, 7))
		{
			if (e instanceof TNTPrimed)
			{
				TNTPrimed tntPrimed = (TNTPrimed) e;
				if (tntPrimed.getFuseTicks() > 10)
				{
					tntPrimed.setFuseTicks(tntPrimed.getFuseTicks() - random.nextInt(10));
				}
				continue;
			}
			final Location otherLocation = e.getLocation();
			if (otherLocation.distanceSquared(loc) < 64)
			{
				Vector vector = new Vector(otherLocation.getX() - loc.getX(), 100, otherLocation.getZ() - loc.getZ());
				vector.normalize();
				vector.setY(2);
				vector.normalize();
				vector.multiply(3);
				e.setVelocity(vector.add(e.getVelocity()));
			}

		}
		while (blocks.hasNext())
		{
			Block bl = blocks.next();
			spawned++;
			Location otherLocation = bl.getLocation();
			if (spawned <= 5)
			{
				loc.getWorld().playEffect(otherLocation, Effect.ZOMBIE_DESTROY_DOOR, bl.getTypeId());
			}
			if (spawned <= 50)
			{

				FallingBlock fall = loc.getWorld().spawnFallingBlock(bl.getLocation().add(0.5, 0.5, 0.5), bl.getType(), bl.getData());
				fall.setDropItem(false);
				Vector vector = new Vector(otherLocation.getX() - loc.getX(), 125, otherLocation.getZ() - loc.getZ());
				vector.normalize();
				vector.setY(2);
				vector.normalize();
				vector.multiply(3);
				fall.setVelocity(vector);

			}
		}
	}
}
