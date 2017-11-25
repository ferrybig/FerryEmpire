/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.FireWorkProjectilePlayer;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import me.ferry.bukkit.plugins.ferryempire.Skill;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

/**
 *
 * @author Fernando
 */
public abstract class MetadataPowerup implements RemoteHitPowerupAction
{
	private final Class<? extends Projectile> clazz;
	private final EffectPlayer effects;

	public MetadataPowerup(Class<? extends Projectile> clazz)
	{
		this(clazz, DEFAULT_EFFECT_PLAYER);
	}

	public MetadataPowerup(Class<? extends Projectile> clazz, EffectPlayer effects)
	{
		this.clazz = clazz;
		this.effects = effects;
	}

	@Override
	public int doAction(Player player, ItemStack item, final RodData data, final FerryEmpirePlugin plugin, RodBase roBase)
	{
		Projectile proj = player.launchProjectile(clazz);
		proj.setMetadata(Skill.class.getName(), new FixedMetadataValue(plugin, data.getSkill()));
		proj.setMetadata(RodBase.class.getName(), new FixedMetadataValue(plugin, roBase.getRodMagicId()));
		this.effects.playEffect(proj, player, item, data, plugin, roBase);
		return -1;
	}

	@Override
	public void loadFromConfig(ConfigurationSection from)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void writeToConfig(ConfigurationSection to)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public static interface EffectPlayer
	{
		public void playEffect(Entity entity, Player player, ItemStack item, final RodData data, final FerryEmpirePlugin plugin, RodBase roBase);
	}

	private static class DefaulftEffectPlayer implements EffectPlayer
	{

		@Override
		public void playEffect(Entity entity, Player player, ItemStack item, final RodData data, final FerryEmpirePlugin plugin, RodBase roBase)
		{
			new FireWorkProjectilePlayer(entity, data, plugin, roBase).start();
		}

	}
	public static final EffectPlayer DEFAULT_EFFECT_PLAYER = new DefaulftEffectPlayer();
}
