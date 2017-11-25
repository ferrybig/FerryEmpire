/*
 * Copyright (C) 2016 Fernando
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ferry.bukkit.plugins.ferryempire;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author Fernando
 */
public class ParticleLibrary {

	private boolean particlesDisabled = false;
	
	public boolean sendToPlayer(String particlename, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count, Player... players) {
		Objects.requireNonNull(location, "location == null");
		Objects.requireNonNull(location.getWorld(), "location.getWorld() == null");
		Objects.requireNonNull(particlename, "particlename == null");
		Objects.requireNonNull(players, "players == null");
		for (int i = 0; i < players.length; i++) {
			Objects.requireNonNull(players[i++], "players[" + i + "] == null");
		}
		if (particlesDisabled) {
			return false;
		}
		try {
			sendToPlayer0(particlename, location, offsetX, offsetY, offsetZ, speed, count, players);
			return true;
		} catch (InvocationTargetException | IllegalArgumentException | IllegalAccessException | SecurityException | NoSuchFieldException | NoSuchMethodException | ClassNotFoundException | InstantiationException ex) {
			Bukkit.getLogger().log(Level.SEVERE, "Cannot use fireworks in this server", ex);
			particlesDisabled = true;
		}
		return false;
	}

	protected void sendToPlayer0(String particlename, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count, Player... players) throws ClassNotFoundException, SecurityException, NoSuchMethodException, NoSuchFieldException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (!initized) {
			initMethods(players[0]);
		}
		Object packetObject = packetConstructor.newInstance();
		a.set(packetObject, getParticleByName.invoke(null, particlename.toUpperCase()));
		b.setFloat(packetObject, (float) location.getX());
		c.setFloat(packetObject, (float) location.getY());
		d.setFloat(packetObject, (float) location.getZ());
		e.setFloat(packetObject, offsetX);
		f.setFloat(packetObject, offsetY);
		g.setFloat(packetObject, offsetZ);
		h.setFloat(packetObject, speed);
		i.setInt(packetObject, count);
		j.setBoolean(packetObject, true);
		k.set(packetObject, new int[0]);
		sendToPlayer(packetObject, players);

	}

	protected void sendToPlayer(Object packet, Player... players) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		for (Player player : players) {
			Object entityPlayer = playerGetHandle.invoke(player);
			Object connection = playerConnection.get(entityPlayer);
			playerSendPacket.invoke(connection, packet);
		}
	}

	private static Field a;
	private static Field b;
	private static Field c;
	private static Field d;
	private static Field e;
	private static Field f;
	private static Field g;
	private static Field h;
	private static Field i;
	private static Field j;
	private static Field k;
	private static Method playerGetHandle;
	private static Method playerSendPacket;
	private static Method getParticleByName;
	private static Constructor<?> packetConstructor;
	private static Field playerConnection;
	private static boolean initized = false;

	private static Method getMethod(Class<?> cl, String method) {
		for (Method m : cl.getMethods()) {
			if (m.getName().equals(method)) {
				return m;
			}
		}
		return null;
	}

	private static Field getField(Class<?> cl, String field) throws NoSuchFieldException {
		Field f = cl.getDeclaredField(field);
		if (f != null) {
			f.setAccessible(true);
			return f;
		}
		for (Field m : cl.getFields()) {
			if (m.getName().equals(field)) {
				m.setAccessible(true);
				return m;
			}
		}
		throw new IllegalArgumentException("Field " + field + " not found, class: " + cl.getName());
	}

	private static void initMethods(Player player) throws ClassNotFoundException, SecurityException, NoSuchMethodException, NoSuchFieldException {
		final String nmsPackage;
		final Class<?> nmsPacket63WorldParticles;
		final Class<?> nmsEntityPlayer;
		final Class<?> nmsConnection;
		final Class<?> nmsPacket;
		final Class<?> nmsEntity;
		final Class<?> enumParticle;
		final ClassLoader classLoader;
		playerGetHandle = getMethod(player.getClass(), "getHandle");
		nmsEntity = playerGetHandle.getReturnType();
		nmsPackage = nmsEntity.getPackage().getName();
		classLoader = nmsEntity.getClassLoader();
		nmsPacket63WorldParticles = Class.forName(nmsPackage + ".PacketPlayOutWorldParticles", true, classLoader);
		nmsEntityPlayer = Class.forName(nmsPackage + ".EntityPlayer", true, classLoader);
		enumParticle = Class.forName(nmsPackage + ".EnumParticle", true, classLoader);
		nmsConnection = Class.forName(nmsPackage + ".PlayerConnection", true, classLoader);
		nmsPacket = Class.forName(nmsPackage + ".Packet", true, classLoader);
		packetConstructor = nmsPacket63WorldParticles.getConstructor(new Class<?>[0]);
		a = getField(nmsPacket63WorldParticles, "a");
		b = getField(nmsPacket63WorldParticles, "b");
		c = getField(nmsPacket63WorldParticles, "c");
		d = getField(nmsPacket63WorldParticles, "d");
		e = getField(nmsPacket63WorldParticles, "e");
		f = getField(nmsPacket63WorldParticles, "f");
		g = getField(nmsPacket63WorldParticles, "g");
		h = getField(nmsPacket63WorldParticles, "h");
		i = getField(nmsPacket63WorldParticles, "i");
		j = getField(nmsPacket63WorldParticles, "j");
		k = getField(nmsPacket63WorldParticles, "k");
		playerConnection = nmsEntityPlayer.getDeclaredField("playerConnection");
		playerSendPacket = nmsConnection.getMethod("sendPacket", nmsPacket);
		getParticleByName = enumParticle.getMethod("valueOf", String.class);
		initized = true;
	}
}
