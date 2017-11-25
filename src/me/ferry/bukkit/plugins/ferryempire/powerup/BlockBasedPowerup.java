/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.powerup;

import org.bukkit.block.Block;

/**
 *
 * @author Fernando
 */
public interface BlockBasedPowerup extends PowerupAction
{
	public void onBlockPlace(Block block);
}
