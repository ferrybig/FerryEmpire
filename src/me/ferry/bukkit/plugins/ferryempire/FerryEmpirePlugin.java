/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import me.ferry.bukkit.plugins.FireWorkEffectPlayer;
import me.ferry.bukkit.plugins.Metrics;
import me.ferry.bukkit.plugins.Metrics.Graph;
import me.ferry.bukkit.plugins.PluginBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 *
 * @author Fernando
 */
public class FerryEmpirePlugin extends PluginBase implements Listener, Runnable {

	Set<RodBase> empireRods = new HashSet<RodBase>();
	private FireWorkEffectPlayer fPlayer;
	private ParticleLibrary particles;
	private Map<Skill, Permission> allowPermissions = new EnumMap<Skill, Permission>(Skill.class);
	private Permission allowPermission = null;
	private Map<RodBase, Permission> commandSpawnPerms = new LinkedHashMap<RodBase, Permission>();
	private Permission commandSpawnPerm = null;

	@Override
	public void onPluginEnable() {
		this.fPlayer = new FireWorkEffectPlayer();
		if (this.getConfig().contains("rods") && !("true".equals(System.getProperty("FerryDebug")))) {
			final ConfigurationSection rods = this.getConfig().getConfigurationSection("rods");
			for (String str : rods.getKeys(false)) {
				final RodBase rod = (RodBase) rods.get(str);
				this.empireRods.add(rod);
				for (Recipe recipe : rod.getRecipes()) {
					Bukkit.addRecipe(recipe);
				}
				for (Skill s : rod.getSkills()) {
					this.increaseDefinedStat(s);
				}
			}
		} else {
			RodBase rod;
			this.empireRods.add(rod = new ConfigureableRod());
			for (Recipe recipe : rod.getRecipes()) {
				Bukkit.addRecipe(recipe);
			}
			for (Skill s : rod.getSkills()) {
				this.increaseDefinedStat(s);
			}
		}
		if (this.empireRods.isEmpty()) {
			throw new NoSuchElementException("No empire rods found, check the config!");
		}
		this.saveConfig();
		this.getScheduler().runTaskTimer(this, this, 20, 20);
		Graph graph = this.metrics.createGraph("spells used");
		for (final Skill s : Skill.values()) {
			graph.addPlotter(new Metrics.Plotter(s.name().replace("_", " ").toLowerCase()) {
				@Override
				public int getValue() {
					AtomicInteger get = stats.usesCounter.get(s);
					if (get == null) {
						return 0;
					} else {
						return get.getAndSet(0);
					}
				}
			});
		}
		graph = this.metrics.createGraph("spells defined");
		for (final Skill s : Skill.values()) {
			graph.addPlotter(new Metrics.Plotter(s.name().replace("_", " ").toLowerCase()) {
				@Override
				public int getValue() {
					AtomicInteger get = stats.definedCounter.get(s);
					if (get == null) {
						return 0;
					} else {
						return get.get();
					}
				}
			});
		}
		for (Skill skill : Skill.values()) {
			final String spellName = skill.name().toLowerCase();
			final Permission allowPerm = new Permission("ferryempire.spells." + spellName,
					"allows you to use the spell " + spellName,
					PermissionDefault.OP);
			this.allowPermissions.put(skill, allowPerm);
			this.getServer().getPluginManager().addPermission(allowPerm);
		}
		final Collection<Permission> allow = this.allowPermissions.values();
		this.allowPermission = new Permission("ferryempire.spells.*",
				"allows you to use all the spells",
				PermissionDefault.FALSE,
				new PermissionsMapConvertor(allow));
		this.getServer().getPluginManager().addPermission(this.allowPermission);
		for (RodBase rod : this.empireRods) {
			final Permission rodPerm = new Permission("ferryempire.rods.spawn." + rod.getRodMagicId(),
					"allows you to use the spell " + rod.getRodName(),
					Permission.DEFAULT_PERMISSION);
			this.commandSpawnPerms.put(rod, rodPerm);
			this.getServer().getPluginManager().addPermission(rodPerm);
		}
		this.commandSpawnPerm = new Permission("ferryempire.rods.spawn.*",
				"ALlows you to spawn every empire want",
				PermissionDefault.FALSE,
				new PermissionsMapConvertor(this.commandSpawnPerms.values()));
		this.getServer().getPluginManager().addPermission(this.commandSpawnPerm);


		/*for(net.minecraft.server.v1_5_R2.Block bl : net.minecraft.server.v1_5_R2.Block.byId)
		 {
		 if(bl == null)continue;
		 this.logInfo("case "+bl.id + ": return "+bl.material.isSolid()+";");
		 }*/
		particles = new ParticleLibrary();
	}
	Random random = new Random();

	@Override
	public void saveConfig() {
		this.logInfo("Saving rods, " + this.empireRods.size());
		ConfigurationSection rodsConfig = this.getConfig().createSection("rods");
		for (RodBase rod : this.empireRods) {
			rodsConfig.set(rod.getRodName(), rod);
		}
		super.saveConfig();
	}

	@EventHandler
	public void onEvent(PlayerQuitEvent evt) {
		if (evt.getPlayer().getVehicle() instanceof FallingBlock) {
			evt.getPlayer().getVehicle().remove();
			logWarning("prevented player " + evt.getPlayer().getName() + " from duping a falling block");
		}
	}

	@EventHandler
	public void onEvent(ProjectileHitEvent evt) {
		final Projectile projectile = evt.getEntity();
		if (projectile.hasMetadata(Skill.class.getName())) {
			for (MetadataValue data : projectile.getMetadata(Skill.class.getName())) {
				String spell = data.asString();
				if (spell != null) {
					Skill skill = Skill.valueOf(spell);
					if (projectile.getShooter() instanceof Player) {
						RodBase rodbase = null;
						for (RodBase r : this.empireRods) {
							if (r.getRodMagicId() == getIntegerMetadata(projectile, RodBase.class.getName())) {
								rodbase = r;
								break;
							}
						}
						skill.onRemoteBlockHit(this, (Player) projectile.getShooter(), projectile.getLocation(), projectile, random, rodbase);
					}
				}
			}
			projectile.removeMetadata(Skill.class.getName(), this);
		}
	}

	private int getIntegerMetadata(Entity ent, String name) {
		if (ent.hasMetadata(name)) {
			for (MetadataValue data : ent.getMetadata(name)) {
				return data.asInt();
			}

		}
		return 0;
	}

	@EventHandler
	public void onEvent(EntityChangeBlockEvent evt) {
		final Entity projectile = evt.getEntity();
		if (projectile.hasMetadata(Skill.class.getName())) {
			for (MetadataValue data : projectile.getMetadata(Skill.class.getName())) {
				String spell = data.asString();
				if (spell != null) {
					Skill skill = Skill.valueOf(spell);
					skill.onBlockPlace(evt.getBlock());
					evt.setCancelled(true);
				}
			}
			projectile.removeMetadata(Skill.class.getName(), this);
		}
	}
	boolean bigExplotion = false;
	
	public ParticleLibrary getParticles() {
		return particles;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEvent(EntityExplodeEvent evt) {

		final Entity entity = evt.getEntity();
		List<Block> blocksList = evt.blockList();
		Location loc = evt.getLocation();
		if (entity != null) {
			if (entity.hasMetadata(Skill.class.getName())) {
				for (MetadataValue data : entity.getMetadata(Skill.class.getName())) {
					String spell = data.asString();
					if (spell != null) {
						Skill skill = Skill.valueOf(spell);
						RodBase rodbase = null;
						for (RodBase r : this.empireRods) {
							if (r.getRodMagicId() == getIntegerMetadata(entity, RodBase.class.getName())) {
								rodbase = r;
								break;
							}
						}
						skill.onRemoteExplode(this, loc, entity, random, blocksList, rodbase);

					}
				}
				entity.removeMetadata(Skill.class.getName(), this);
			}

		}
	}

	@Override
	public void run() {
		for (Player player : this.getServer().getOnlinePlayers()) {
			final PlayerInventory inventory = player.getInventory();
			int size = inventory.getSize();

			for (int i = 0; i < size; i++) {
				boolean changed = false;
				ItemStack item = inventory.getItem(i);
				for (RodBase rod : this.empireRods) {
					if (rod.isRod(item)) {
						ItemMeta meta = item.getItemMeta();
						RodData data = rod.getRodData(meta);
						if (data.getActiveSkill() != null) {
							int decrease = data.getActiveSkill().doInteractiveAction(player, data.getActiveSkillTime(), this, rod, data);
							assert decrease >= 0;
							if (decrease >= data.getActiveSkillTime()) {
								changed = true;
								data.setActiveSkill(null);
								data.setActiveSkillTime(0);
							} else if (decrease != 0) {
								changed = true;
								data.setActiveSkillTime(data.getActiveSkillTime() - decrease);
							}
						}
						if (rod.hasCooldown() || rod.hasDurability() || rod.hasPower()) {

							if (data.getCooldown() > 0 && rod.hasCooldown()) {
								data.setCooldown(data.getCooldown() - 1);
								changed = true;
							} else if ((data.getDurability() > 0) && (data.getPower() < rod.maxPower()) && rod.hasPower()) {
								if (rod.hasDurability()) {
									data.setDurability(data.getDurability() - 5);
								}
								data.setPower(data.getPower() + 5);
								if (data.getPower() >= rod.maxPower()) {
									this.sendMessage(player, ChatColor.DARK_RED + "Your " + rod.getRodName() + " is now fully charged!!");
								}
								changed = true;
							}
							if (data.getDurability() == 0 && data.getPower() <= 5 && rod.hasDurability()) {
								item = null;
								this.sendMessage(player, ChatColor.DARK_RED + "Your " + rod.getRodName() + " broke because it ran dry!");
								new Smoker(
										this, // plugin instance
										5, // how many blocks around it need to be smoke
										10, // time in ticks the smoke will last (1 sec = 20 ticks)
										1, // the low limit of smoke partcles every tick
										10, //the high limit of max smoke particles each tick
										player.getLocation() // the location object where the smoke must form
								).start();
								player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 10, 1);
								changed = true;
							}

						}
						if (changed) {
							if (item != null) {
								meta.setLore(data.toLore(rod));
								meta.setDisplayName(rod.getRodNameItem(data));
								item.setItemMeta(meta);
							} else {
								item = new ItemStack(rod.getRod().getType(), 1);
							}
							inventory.setItem(i, item);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onEvent(PlayerInteractEvent evt) {
		ItemStack item = evt.getPlayer().getItemInHand();
		if (item == null) {
			return;
		}
		if (evt.hasBlock()) {
			Material m = evt.getClickedBlock().getType();
			if (m == Material.WOODEN_DOOR
					|| m == Material.BED
					|| m == Material.WORKBENCH
					|| m == Material.CHEST
					|| m == Material.FURNACE
					|| m == Material.LEVER
					|| m == Material.STONE_BUTTON
					|| m == Material.ENCHANTMENT_TABLE) {
				return;
			}
		}
		if (evt.getAction() == Action.PHYSICAL) {
			return;
		}
		ItemMeta meta = item.getItemMeta();
		for (RodBase rod : this.empireRods) {
			if (rod.isRod(item)) {
				RodData data = rod.getRodData(meta);
				Player player = evt.getPlayer();
				if ((evt.getAction() == Action.LEFT_CLICK_AIR) || (evt.getAction() == Action.LEFT_CLICK_BLOCK)) {
					doLeftMouseClick(data, player, item, rod);
					meta.setLore(data.toLore(rod));
					meta.setDisplayName(rod.getRodNameItem(data));
					item.setItemMeta(meta);
					player.setItemInHand(item);
				} else if ((evt.getAction() == Action.RIGHT_CLICK_AIR) || (evt.getAction() == Action.RIGHT_CLICK_BLOCK)) {
					player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, 119, 30);
					if (data.getCooldown() < 3) {
						data.setCooldown(3);
					}
					int index = 0;
					Skill[] values = rod.getSkills();
					if (values.length <= 1) {
						this.sendMessage(player, ChatColor.DARK_RED + "There are no other spells inside this " + rod.getRodName() + ", sorry!");
						return;
					}
					for (int i = 0; i < values.length; i++) {
						if (values[i] == data.getSkill()) {
							index = i;
							break;
						}
					}
					if (player.isSneaking()) {
						index--;
					} else {
						index++;
					}
					if ((index >= 0) && (index < values.length)) {
						data.setSkill(values[index]);
					} else if (player.isSneaking()) {
						data.setSkill(values[index = (values.length - 1)]);
					} else {
						data.setSkill(values[index = 0]);
					}

					meta.setLore(data.toLore(rod));
					meta.setDisplayName(rod.getRodNameItem(data));
					item.setItemMeta(meta);
					player.setItemInHand(item);
					player.sendMessage(ChatColor.DARK_RED + "Selected spell " + index + " - " + ChatColor.DARK_GREEN + data.getSkill().name().replace("_", " ").toLowerCase());
				}
				break;
			}
		}
	}

	private void doLeftMouseClick(RodData data, Player player, ItemStack item, RodBase rod) {
		if (rod.hasPower()) {
			if (data.getSkill().getCost() > data.getPower()) {
				this.sendMessage(player, ChatColor.DARK_RED + rod.getRodName() + ChatColor.DARK_GREEN + " have no power left, wait some time to refull it!");
				return;
			}
		} else if (rod.hasDurability()) {
			if (data.getSkill().getCost() > data.getDurability()) {
				this.sendMessage(player, ChatColor.DARK_RED + rod.getRodName() + ChatColor.DARK_GREEN + " have no durability left, sorry!");
				return;
			}
		}
		boolean doCosts = true;
		int cooldownOverride = -2;
		try {
			Skill selected = data.getSkill();
			if (player.hasPermission(this.allowPermissions.get(selected))) {
				this.increaseUsedStat(selected);
				cooldownOverride = selected.doAction(player, item, data, this, rod);
			} else {
				cooldownOverride = -2;
				this.sendMessage(player,
						ChatColor.DARK_GREEN + "It looks like you can't use " + ChatColor.DARK_RED + rod.getRodName() + ChatColor.DARK_GREEN + " because of a server permission problem");
			}

		} finally {
			if (cooldownOverride == -2) {
				doCosts = false;
			}
			if (doCosts) {
				if (rod.hasPower()) {
					data.setPower(data.getPower() - data.getSkill().getCost());
				} else if (rod.hasDurability()) {
					data.setDurability(data.getDurability() - data.getSkill().getCost());
				}
				if (cooldownOverride == -1) {
					int cooldown = data.getCooldown();
					int cooldownSquare = cooldown * cooldown;
					if ((cooldownSquare < 400) && (cooldownSquare < data.getSkill().getCost() * 5)) {
						data.setCooldown(cooldown + 2);
					}

				} else if (data.getCooldown() < cooldownOverride) {
					data.setCooldown(cooldownOverride);
				}
			}
		}
	}

	@Override
	public void onPluginLoad() {

		ConfigurationSerialization.registerClass(ConfigureableRod.class);
		ConfigurationSerialization.registerClass(ConfigureableRod.class, "DefaultEmpireWand");
		ConfigurationSerialization.registerClass(DarkWand.class);
		ConfigurationSerialization.registerClass(DarkWand.class, "DarkWard");
		this.useMetrics();
		this.useConfig();

	}

	@Override
	public void onPluginDisable() {
	}

	@Override
	public void onPluginCleanup() {
		empireRods.clear();
	}

	public FireWorkEffectPlayer getfPlayer() {
		return fPlayer;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		@SuppressWarnings("unchecked")
		List<String> empty = Collections.EMPTY_LIST;
		if (command.getName().equals("wand") || command.getName().equals("ferryempire-wand")) {
			if (this.empireRods.size() == 1) {

				return empty;
			} else {
				if (args.length == 0) {
					List<String> l = new ArrayList<String>();
					for (RodBase r : this.empireRods) {
						if (r.maySpawnWithCommand()) {
							l.add(String.valueOf(r.getRodName()));
						}
					}
					return l;
				} else if (args.length == 1) {
					List<String> l = new ArrayList<String>();
					String id = String.valueOf(args[0]);
					for (RodBase r : this.empireRods) {
						String rId = String.valueOf(r.getRodMagicId());
						String rId1 = String.valueOf(r.getRodName());
						if (rId.startsWith(id) || rId1.startsWith(id)) {
							if (r.maySpawnWithCommand()) {
								l.add(String.valueOf(rId1));
							}
						}
					}
					return l;

				} else {
					return empty;
				}
			}
		}
		if (command.getName().equals("spell") || command.getName().equals("ferryempire-spell")) {
		}
		return empty;

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equals("wand") || command.getName().equals("ferryempire-wand")) {
			if ((sender instanceof Player)) {
				if (this.empireRods.size() == 1) {

					RodBase r = this.empireRods.iterator().next();
					if (r.maySpawnWithCommand()) {
						((Player) sender).getInventory().addItem(r.getRod());
						this.sendMessage(sender, r.getRodName() + " given!");
					} else {
						this.sendMessage(sender, "You may not spawn a " + r.getRodName());
					}
				} else {
					if (args.length != 1) {
						this.sendMessage(sender, "say the id of the empire wand you want to spawn (default id = 27)");
					} else {
						try {
							int id = Integer.parseInt(args[0]);
							for (RodBase r : this.empireRods) {
								if (r.getRodMagicId() == id) {
									if (r.maySpawnWithCommand()) {
										((Player) sender).getInventory().addItem(r.getRod());
										this.sendMessage(sender, r.getRodName() + " given!");
									} else {
										this.sendMessage(sender, "You may not spawn a " + r.getRodName());
									}
								}
								return true;
							}
						} catch (NumberFormatException ex) {
							String id = args[0];
							for (RodBase r : this.empireRods) {
								if (r.getRodName().equalsIgnoreCase(id)) {
									if (r.maySpawnWithCommand()) {
										((Player) sender).getInventory().addItem(r.getRod());
										this.sendMessage(sender, r.getRodName() + " given!");
									} else {
										this.sendMessage(sender, "You may not spawn a " + r.getRodName());
									}
								}
								return true;
							}
						}
						this.sendMessage(sender, "id invalid, ask server admins for correct id");
					}
				}
			} else {
				this.sendMessage(sender, "Sorry I can't give to you");
			}
			return true;
		}
		return false;
	}

	private void increaseUsedStat(Skill skill) {
		AtomicInteger get = stats.usesCounter.get(skill);
		if (get == null) {
			stats.usesCounter.put(skill, new AtomicInteger(1));
		} else {
			get.incrementAndGet();
		}

	}

	private void increaseDefinedStat(Skill skill) {
		AtomicInteger get = stats.definedCounter.get(skill);
		if (get == null) {
			stats.definedCounter.put(skill, new AtomicInteger(1));
		} else {
			get.incrementAndGet();
		}

	}
	private final Statistics stats = new Statistics();
}
