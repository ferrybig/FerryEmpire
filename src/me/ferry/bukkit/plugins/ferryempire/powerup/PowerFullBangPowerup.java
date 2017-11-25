/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import java.util.Random;
import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.FlameThrower;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import me.ferry.bukkit.plugins.ferryempire.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Fernando
 */
public class PowerFullBangPowerup extends ActiveSkillPowerup implements RemoteHitPowerupAction
{
	public PowerFullBangPowerup()
	{
		super(10);
	}
	/*@Override
	 public int doAction(Player player, ItemStack item, RodData data, FerryEmpirePlugin plugin, RodBase rodBase)
	 {
	 player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 600, 1, true));
	 player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 2, true));
	 player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 1, true));
	 player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 600, 1, true));
	 player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 600, 2, true));
	 player.getWorld().playSound(player.getLocation(), Sound.ENDERDRAGON_DEATH, 10, 1);

	 return 120;
	 }*/

	/**
	 *
	 *
	 * @param plugin the value of plugin
	 * @param shooter the value of shooter
	 * @param loc the value of loc
	 * @param ent the value of ent
	 * @param random the value of random
	 * @param rodbase the value of rodbase
	 */

	@Override
	public void onRemoteBlockHit(FerryEmpirePlugin plugin, Player shooter, Location loc, Projectile ent, Random random, RodBase rodbase)
	{
		if (random.nextInt(8) == 1)
		{
			loc.getWorld().strikeLightning(loc);
			loc.getWorld().createExplosion(loc, 0.0f, true);
			loc.getWorld().playSound(loc, Sound.ENTITY_CREEPER_PRIMED, 50, 1);
		}
		else
		{
			loc.getWorld().strikeLightningEffect(loc);
		}
	}

	@Override
	public int doInteractiveAction(Player player, int remainingPower, FerryEmpirePlugin plugin, RodBase rodBase, RodData data)
	{
		new FlameThrower(plugin, 10, player).start();
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_DEATH, 10, 1);
		plugin.sendMessage(player,ChatColor.DARK_RED + rodBase.getRodName() + " " + ChatColor.DARK_GREEN + remainingPower + " Sec left!");
		return 1;
	}

	@Override
	public int doAction(Player player, ItemStack item, RodData data, FerryEmpirePlugin plugin, RodBase rodBase)
	{
		super.doAction(player, item, data, plugin, rodBase);
		return 60;
	}

	@Override
	protected Skill getSkill()
	{
		return Skill.POWERFULL_BANG;
	}
}
