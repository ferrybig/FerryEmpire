/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Fernando
 */
public class Statistics {

	public final Map<Skill, AtomicInteger> usesCounter = new EnumMap<Skill, AtomicInteger>(Skill.class);
	public final Map<Skill, AtomicInteger> definedCounter = new EnumMap<Skill, AtomicInteger>(Skill.class);
}
