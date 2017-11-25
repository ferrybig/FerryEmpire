package me.ferry.bukkit.plugins.ferryempire.powerup;

import me.ferry.bukkit.plugins.ferryempire.FerryEmpirePlugin;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/**
 *
 * @author Fernando
 */
public class SparkPowerup extends InstantSpellPowerup
{

	public SparkPowerup()
	{
		super(null, false);
	}

	@Override
	public void onExplode(Location loc, Player player, FerryEmpirePlugin plugin, RodData rodData, RodBase rodbase)
	{
		ThrownPotion proj = loc.getWorld().spawn(loc, ThrownPotion.class);
		ItemStack i = proj.getItem();
		PotionMeta m = (PotionMeta) i.getItemMeta();
		m.addCustomEffect(new PotionEffect(PotionEffectType.HARM,1,2), true);
		i.setItemMeta(m);
		i.setDurability((short)8236);
		proj.setShooter(player);
		proj.setItem(i);
		proj.setVelocity(new Vector(0,-10,0));
		try
		{
			plugin.getfPlayer().playFirework(loc.getWorld(), loc, rodData.getSkill().getEffect(rodbase).getNewEffect(rodbase));
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
}
