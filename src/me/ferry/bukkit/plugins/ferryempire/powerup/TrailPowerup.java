/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.BetterBukkitRunnable;
import me.ferry.bukkit.plugins.BlockUtil;
import me.ferry.bukkit.plugins.SafeBlockIterator;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.EfectGenerator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Fernando
 */
public abstract class TrailPowerup implements PowerupAction
{
	int maxLength = 100;
	int blockStart = 5;
	int blockSkip = 2;
	String trailName = "Trail";

	@Override
	public int doAction(final Player player, ItemStack item, RodData data, final FerryEmpirePlugin plugin, final RodBase rodBase)
	{
		final SafeBlockIterator bl = new SafeBlockIterator(player, maxLength);
		final Location playerLoc = player.getLocation();
		boolean skipBlock = true;
		final BlockUtil blocks = plugin.getBlockUtil();
		for (int i = 0; i < blockStart; i++)
		{
			skipBlock = next(bl, blocks);
			if (skipBlock == false)
			{
				break;
			}
		}
		// = next(bl) && next(bl) && next(bl) && next(bl) && next(bl);
		if (!skipBlock)
		{
			plugin.sendMessage(player, ChatColor.DARK_RED + rodBase.getRodName() + ChatColor.DARK_GREEN + ' '+trailName+" failed!");
			return -2;
		}
		else
		{
			final EfectGenerator effect = data.getSkill().getEffect(rodBase);
			new BetterBukkitRunnable()
			{
				private Location tmp = new Location(null, 0, 0, 0);

				@Override
				public void run()
				{
					if (bl.hasNext())
					{
						Block next = bl.next();
						boolean skipBlock = true;
						for (int i = 0; i < blockSkip; i++)
						{
							skipBlock = next(bl, blocks);
							if (skipBlock == false)
							{
								break;
							}
						}
						//boolean skipBlock = next(bl) && next(bl);//forward 2 blocks
						if (!skipBlock || blocks.isSolid(next.getTypeId()))
						{
							cancel();
						}
						next.getLocation(tmp);
						tmp.setPitch(playerLoc.getPitch());
						tmp.setYaw(playerLoc.getYaw());
						TrailPowerup.this.runTrail(tmp, player, plugin, effect, rodBase);
					}
					else
					{
						cancel();
					}
				}
			}.runTaskTimer(plugin, 2, 2);
			return -1;
		}
	}

	/**
	 *
	 *
	 * @param loc the value of loc
	 * @param player the value of player
	 * @param plugin the value of plugin
	 * @param effect the value of effect
	 * @param rodbase the value of rodbase
	 */

	public abstract void runTrail(Location loc, Player player, FerryEmpirePlugin plugin, EfectGenerator effect, RodBase rodbase);


	/**
	 *
	 * @param bl the value of bl
	 * @param blocks the value of blocks
	 * @return the boolean
	 */
	private boolean next(SafeBlockIterator bl, BlockUtil blocks)
	{
		if (bl.hasNext())
		{
			if (blocks.isSolid(bl.next().getTypeId()))
			{
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public void loadFromConfig(ConfigurationSection from)
	{
		this.blockSkip = from.getInt("blocksSkip", this.blockSkip);
		this.blockStart = from.getInt("blocksStart", this.blockStart);
		this.maxLength = from.getInt("maxLength", maxLength);
		this.trailName = from.getString("name", this.trailName);
	}

	@Override
	public void writeToConfig(ConfigurationSection to)
	{
		to.set("blocksSkip", this.blockSkip);
		to.set("blocksStart", this.blockStart);
		to.set("maxLength", this.maxLength);
		to.set("name", this.trailName);
	}
}
