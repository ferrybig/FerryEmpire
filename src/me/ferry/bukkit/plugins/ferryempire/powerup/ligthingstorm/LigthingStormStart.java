/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup.ligthingstorm;

import java.util.Random;
import me.ferry.bukkit.plugins.BetterBukkitRunnable;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.Skill;
import me.ferry.bukkit.plugins.ferryempire.SkillColors;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.EfectGenerator;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

/**
 *
 * @author Fernando
 */
public class LigthingStormStart extends BetterBukkitRunnable
{
	private final double radius;
	private final FerryEmpirePlugin plugin;
	private final EfectGenerator effect;
	private boolean failed = false;
	private double angle = Math.toRadians(45);
	public final static double ANGLE_INCREASE = Math.toRadians(10);
	public final static double ANGLE_DIFF = Math.toRadians(90);
	private int timer = 0;
	private final Location start;
	private final RodBase rodbase;

	public LigthingStormStart(Location start, double radius, FerryEmpirePlugin plugin,RodBase rodbase)
	{
		this.radius = radius;
		this.plugin = plugin;
		this.effect = SkillColors.STORM_COLOR.getEffect(null, null);
		this.start = start;
		this.rodbase = rodbase;
	}

	@Override
	public final void run()
	{
		if (timer++ > 20)
		{
			this.cancel();
			this.explode();
		}
		this.angle += ANGLE_INCREASE;
		try
		{
			FireworkEffect eff = this.effect.getNewEffect(rodbase);
			for (int i = 0; i < 4; i++)
			{
				Location loc = this.start.clone().add(this.getDirection(angle + (ANGLE_DIFF * i)).multiply(radius));

				plugin.getfPlayer().playFirework(loc.getWorld(), loc, eff);
			}
		}
		catch (Exception ex)
		{
			if (failed == false)
			{
				ex.printStackTrace();
				failed = true;
			}
		}
	}

	public void start()
	{
		this.runTaskTimer(plugin, 5, 5);
	}

	public Vector getDirection(double direction)
	{
		Vector vector = new Vector();
		vector.setX(-Math.sin(direction));
		vector.setZ(Math.cos(direction));
		return vector;
	}
	Random random = new Random();

	private void explode()
	{
		final double radiusSquared = this.radius * this.radius;

		World world = start.getWorld();

		int y = start.getBlockY();

		int offSetX = this.start.getBlockX();
		int offSetZ = this.start.getBlockZ();

		int startX = (int) (offSetX - radius);
		int startZ = (int) (offSetZ - radius);

		int endX = (int) (offSetX + radius);
		int endZ = (int) (offSetZ + radius);

		for (int counterX = startX; counterX < endX; counterX++)
		{
			for (int counterZ = startZ; counterZ < endZ; counterZ++)
			{
				if (this.random.nextInt(4) != 0)
				{
					continue;
				}
				Block block = world.getBlockAt(counterX, y, counterZ);
				Location l = block.getLocation().add(0.5, 0.5, 0.5);

				if (this.start.distanceSquared(l) > radiusSquared)
				{
					continue;
				}
				if (world.getHighestBlockYAt(counterX, counterZ) < y)
				{
					block = world.getHighestBlockAt(counterX, counterZ);
					l = block.getLocation().add(0.5, 0.5, 0.5);
				}

				world.strikeLightning(l);

			}
		}
	}
}
