/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import java.util.List;
import java.util.Random;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.Smoker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;

/**
 *
 * @author Fernando
 */
public class VulcanePowerup extends SecondEffectPowerup
{
	private final Material blockA;
	private final Material blockB;

	public VulcanePowerup(Material blockA, Material blockB)
	{
		this.yield = 1.0f;
		this.fuse = 0;
		this.blockA = blockA;
		this.blockB = blockB;
	}

	@Override
	public void onRemoteExplode(FerryEmpirePlugin plugin, Location loc, Entity ent, Random random, List<Block> blocks, RodBase rodbase)
	{
		int r = random.nextInt(30) + random.nextInt(30);
		for (int i = 0; i < r; i++)
		{
			final FallingBlock block = loc.getWorld().spawnFallingBlock(loc, blockA, (byte) 0);
			block.setVelocity(
				new Vector(random.nextDouble() - 0.5, random.nextDouble() + 0.1, random.nextDouble() - 0.5)
				.normalize().multiply(random.nextDouble() + 0.1));
			block.setDropItem(false);
		}
		r = random.nextInt(30) + random.nextInt(30);
		for (int i = 0; i < r; i++)
		{
			final FallingBlock block = loc.getWorld().spawnFallingBlock(loc, blockB, (byte) 1);
			block.setVelocity(
				new Vector(random.nextDouble() - 0.5, random.nextDouble() + 0.1, random.nextDouble() - 0.5)
				.normalize().multiply(random.nextDouble() + 0.1));
			block.setDropItem(false);
		}
		new Smoker(
			plugin, // plugin instance
			5, // how many blocks around it need to be smoke
			10, // time in ticks the smoke will last (1 sec = 20 ticks)
			10, // the low limit of smoke partcles every tick
			20, //the high limit of max smoke particles each tick
			loc
		// the location object where the smoke must form
		).start();
		loc.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 10, 1);
	}
}
