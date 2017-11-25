/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.ferry.bukkit.plugins.BetterBukkitRunnable;
import me.ferry.bukkit.plugins.BlockLocation;
import me.ferry.bukkit.plugins.SafeBlockIterator;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import me.ferry.bukkit.plugins.ferryempire.Skill;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

/**
 *
 * @author Fernando
 */
public class BloodBlockPowerup implements BlockBasedPowerup
{
	private final Map<String, BlockLocation> blocks = new HashMap<String, BlockLocation>();
	private Material blockMat;
	private Material effectMat;

	public BloodBlockPowerup(Material blockMat, Material effectMat)
	{
		this.blockMat = blockMat;
		this.effectMat = effectMat;
	}

	@Override
	public int doAction(Player player, ItemStack item, RodData data, final FerryEmpirePlugin plugin, RodBase roBase)
	{
		if (blocks.containsKey(player.getName()))
		{
			BlockLocation block = blocks.get(player.getName());
			try
			{
				// player have placed a block, check if block is valid
				final BlockLocation target = new BlockLocation(SafeBlockIterator.getTargetBlock(plugin, player, 50));
				if (!block.getBlock().isEmpty())
				{
					// block is valid, throw it!

					final FallingBlock fblock = block.getWorld().spawnFallingBlock(block.getLocation(), block.getBlock().getType(), (byte) 0);
					fblock.setDropItem(false);
					block.getBlock().setType(Material.AIR);
					fblock.setMetadata(Skill.class.getName(), new FixedMetadataValue(plugin, data.getSkill().name()));
					fblock.setVelocity(new Vector(0, 1, 0));
					new BetterBukkitRunnable()
					{
						@Override
						public void run()
						{
							Location playerLocation = fblock.getLocation();
							final Location otherLocation = target.getLocation();
							Vector vector = new Vector(otherLocation.getX() - playerLocation.getX(),
										   otherLocation.getY() - playerLocation.getY(),
										   otherLocation.getZ() - playerLocation.getZ());
							vector.normalize();
							vector.multiply(2);
							vector.setY(vector.getY() + 0.2);
							fblock.setVelocity(vector);


						}
					}.runTaskLater(plugin, 10);
					new BetterBukkitRunnable()
					{
						int timer = 0;

						@Override
						public void run()
						{
							int id = effectMat.getId();
							fblock.getWorld().playEffect(fblock.getLocation(), Effect.STEP_SOUND, id);
							fblock.getWorld().playEffect(fblock.getLocation(), Effect.STEP_SOUND, id);
							fblock.getWorld().playEffect(fblock.getLocation(), Effect.STEP_SOUND, id);
							//fblock.getWorld().playEffect(fblock.getLocation(), Effect.STEP_SOUND, id);
							if (!fblock.isValid() || timer++ > 1000)
							{
								cancel();
								fblock.remove();
							}
						}
					}.runTaskTimer(plugin, 5, 1);
					return -1;
				}
			}
			finally
			{
				blocks.remove(player.getName());
			}
		}
		assert !blocks.containsKey(player.getName());
		// place a block
		List<Block> bl = SafeBlockIterator.getLastTwoTargetBlocks(plugin, player, 20);
		final BlockLocation target;
		if (!bl.isEmpty() && bl.get(0).isEmpty())
		{
			target = new BlockLocation(bl.get(0));
		}
		else if (bl.size() > 1 && bl.get(1).isEmpty())
		{
			target = new BlockLocation(bl.get(1));
		}
		else
		{
			plugin.sendMessage(player, "Bloodblock cannot be placed!");
			return -2;
		}
		if (!target.getBlock().isEmpty())
		{
			target.getBlock().breakNaturally();
		}
		this.blocks.put(player.getName(), target);
		target.getBlock().setType(this.blockMat);
		return -1;
	}

	@Override
	public void onBlockPlace(Block block)
	{
		block.getLocation().getWorld().createExplosion(block.getLocation().add(0.5, 0.5, 0.5), 5.0f);
	}

	@Override
	public void loadFromConfig(ConfigurationSection from)
	{
		blockMat = Material.matchMaterial(from.getString("MaterialBlock", String.valueOf(blockMat.getId())));
		effectMat = Material.matchMaterial(from.getString("MaterialEffect", String.valueOf(effectMat.getId())));
	}

	@Override
	public void writeToConfig(ConfigurationSection to)
	{
		to.set("MaterialBlock", blockMat.name());
		to.set("MaterialEffect", effectMat.name());
	}
}
