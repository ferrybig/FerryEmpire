package me.ferry.bukkit.plugins.ferryempire;

import java.util.List;
import java.util.Random;
import me.ferry.bukkit.plugins.ferryempire.powerup.ActivePowerupAction;
import me.ferry.bukkit.plugins.ferryempire.powerup.AntiGravityPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.AnvilPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.ArrowPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.BlackHolePowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.BlockBasedPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.BloodBlockPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.BombPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.CapturePowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.CarrotCannonPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.ClusterPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.CometPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.ConfusionPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.DecayPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.DoubleJumpPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.EMPPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.EnderBombPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.EnderEggPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.EpicExplodePowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.EruptingPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.FarmGrowPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.FireBallPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.FireTrailPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.FragPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.FreezePowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.GrenadePowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.HarvesterPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.HealPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.HeatPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.HyperSonicPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.LeapPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.LightningPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.LightningStormPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.LoveWavePowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.MinePowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.PowerFullBangPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.PowerupAction;
import me.ferry.bukkit.plugins.ferryempire.powerup.PullPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.PushPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.RechargePowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.RefoodPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.RemoteExplodePowerupAction;
import me.ferry.bukkit.plugins.ferryempire.powerup.RemoteHitPowerupAction;
import me.ferry.bukkit.plugins.ferryempire.powerup.ShieldPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.SignalBeanPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.SmokePowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.SnowGolemWalkingPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.SparkPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.SparkWavePowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.SuckPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.TeleportEnderPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.TeleportPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.TerrorSheepPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.ThrowPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.TntTrailPowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.TunnelBorePowerup;
import me.ferry.bukkit.plugins.ferryempire.powerup.VulcanePowerup;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.factory.EffectFactory;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.EfectGenerator;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.SkillBasedRandomGenerator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;

/**
 * Contains the basic skill implementations
 *
 * @author Fernando
 */
public enum Skill implements PowerupAction, BlockBasedPowerup, ActivePowerupAction, RemoteHitPowerupAction, RemoteExplodePowerupAction {
	LEAP(7, "Moves to a location", new LeapPowerup()),
	SPARK(6, "Sparks a location", new SparkPowerup(), SkillColors.SPARK_COLOR),
	LIGHTNING(6, "This powerup shoots strong\nligthing bolds fromt he sky", new LightningPowerup()),
	GRAB(1, "With this powerup, you can\ngrab creatures from a distance", new PullPowerup(), SkillColors.GRAB_COLOR),
	PUSH(2, "With this powerup, you can\npush creatures from a distance", new PushPowerup(), SkillColors.GRAB_COLOR),
	THROW(2, "With this powerup, you can\nthrow creatures up from a distance", new ThrowPowerup(), SkillColors.GRAB_COLOR),
	ARROW(1, "Schoots a arrow", new ArrowPowerup(false), SkillColors.SPARK_COLOR),
	BURNING_ARROW(2, "Shoots a burning arrow", new ArrowPowerup(true), SkillColors.BURNING_ARROW_COLOR),
	FIREBALL(3, "Shoots a fireball", new FireBallPowerup(false), SkillColors.FIRE_BALL_COLOR),
	EXPLOTION_FIREBALL(9, "Shoots a explosive fireball", new FireBallPowerup(true), SkillColors.FIRE_BALL_COLOR),
	EXPLOTION_BOMB(7, "Shoots a explosive bomb", new BombPowerup()),
	CARROT_CANNON(9, "shoots a explosive wortel", new CarrotCannonPowerup()),
	COMET(9, "Shoots a fast comet, that\ncauses a explotion on impact", new CometPowerup(), SkillColors.BURNING_ARROW_COLOR),
	BLOOD_BLOCK(16, "MultiStage spell, \nthrows a exploding \nredstone block", new BloodBlockPowerup(Material.REDSTONE_BLOCK, Material.REDSTONE_WIRE), SkillColors.BURNING_ARROW_COLOR),
	//FIRE_BLOCK(16, "MultiStage spell, \nthrows a exploding \nfire block", new BloodBlockPowerup(Material.CAULDRON, Material.MOB_SPAWNER), SkillColors.BURNING_ARROW_COLOR),
	SIGNAL_BEAM(1, "shoots a signal beam up to the sky", new SignalBeanPowerup()),
	CAPTURE(30, "captures mobs and players", new CapturePowerup(), SkillColors.SPARK_COLOR),
	SNOW_GOLEM(20, "you are the snow golem\n(trail of snow)", new SnowGolemWalkingPowerup(), SkillColors.FREEZE),
	TERROR_SHEEP(35, "shoots a terror sheep", new TerrorSheepPowerup()),
	/*TREE(7, "Grows a tree"),
	 BIG_TREE(14, "Grows a big tree"),
	 JUNGLE_TREE(7, "Grows a jungle tree"),
	 BIG_JUNGLE_TREE(28, "Grows a big jungle tree"),
	 BIRCH_TREE(7, "Grows a birch tree"),
	 RED_TREE(7, "Grows a redwood tree"),*/
	FARM_GROW(5, "Grows all nearby crops", new FarmGrowPowerup()),
	FLYING_EGG(20, "fly on a dragon egg to other places", new EnderEggPowerup(), SkillColors.BLACK_LARGE),
	ENDER_BOMB(25, "shoots a dragon egg", new EnderBombPowerup(), SkillColors.BLACK_LARGE),
	FREEZE(5, "Freezes nearby terrain", new FreezePowerup(), SkillColors.FREEZE),
	MINE(9, "Mines nearby terrain", new MinePowerup()),
	//TREE_GROW(5, "Grows all nearby trees"),
	SMOKE_GRENADE(11, "Throw a smoke grenade", new SmokePowerup(), SkillColors.BURNING_ARROW_COLOR),
	FRAG_GRENADE(11, "Throw a frag grenade", new FragPowerup(), SkillColors.BURNING_ARROW_COLOR),
	TNT_GRENADE(11, "Throws a small tnt", new GrenadePowerup(), SkillColors.BURNING_ARROW_COLOR),
	TELEPORT(10, "Teleport direct", new TeleportPowerup()),
	TELEPORT_ENDER(5, "Teleport using ender pearl", new TeleportEnderPowerup(), SkillColors.ENDER_COLOR),
	//TELEPORT_ENDER_SMOKE(25, "Teleport using ender pearl\ncombined with a smoke grenade"),
	CONFUSION(15, "Makes others blind for 10 sec", new ConfusionPowerup(), SkillColors.BLACK_LARGE),
	FIRE_TRAIL(23, "Trail of Fire!", new FireTrailPowerup(), SkillColors.FIRE_BALL_COLOR),
	TNT_TRAIL(23, "Trail of Tnt!", new TntTrailPowerup(), SkillColors.TNT_TRAIL),
	LOVE_WAVE(23, "The trail of lives\nbrings regeneration", new LoveWavePowerup(), SkillColors.LOVE_WAVE),
	SPARK_WAVE(23, "The trail of sparking", new SparkWavePowerup(), SkillColors.SPARK_WAVE_COLOR),
	HEAL(9, "Heal yourself for 3 hearts", new HealPowerup(1)),
	HEAL_BIG(24, "heal yourself for 9 hearts", new HealPowerup(3)),
	REFOOD(10, "Refills your food bar", new RefoodPowerup()),
	POWERFULL_BANG(100, "High power explosion", new PowerFullBangPowerup()),
	HYPER_SONIC(90, "Shoots a a arrow up to the sky\nand summons a powerfull tnt that will\n create a massive crator", new HyperSonicPowerup()),
	LAVA_VULCANE(50, "Shoots a a arrow up to the sky\nand summons a powerfull tnt that will\n create a small vulcane", new VulcanePowerup(Material.FIRE, Material.LAVA), SkillColors.BURNING_ARROW_COLOR),
	TNT_VULCANE(50, "Shoots a a arrow up to the sky\nand summons a powerfull tnt that will\n create a small crater", new VulcanePowerup(Material.FIRE, Material.TNT), SkillColors.BURNING_ARROW_COLOR),
	STORM(75, "Big circle causes a large storm shock", new LightningStormPowerup()),
	RECHARGE(10, "(useless)\nRecharges the Empire Stick using\ndiamonds inside your inventory", new RechargePowerup()),
	DOUBLE_JUMP(20, "Enables double jumps", new DoubleJumpPowerup()),
	EPIC_EXPLODE(96, "Compressed Anti-Matter\ncreates a large crater", new EpicExplodePowerup(), SkillColors.BIG_FREEZE),
	HEAT(10, "Heats the surrounding", new HeatPowerup(), SkillColors.FIRE_BALL_COLOR),
	SUCK(30, "Sucks blocks to a crtain location", new SuckPowerup(), SkillColors.FIRE_BALL_COLOR),
	CLUSTER(60, "Shoots a clusterbomb to a location", new ClusterPowerup(), SkillColors.FIRE_BALL_COLOR),
	BLACKHOLE(60, "Long lasting blackhole", new BlackHolePowerup(), SkillColors.ENDER_COLOR),
	SHIELD(10, "Deploys a shield blocking projectiles", new ShieldPowerup(), SkillColors.SPARK_COLOR),
	EMP(10, "Creates a shockwave that\ncrashes redstone circuits", new EMPPowerup()),
	DECAY(10, "Decays the area", new DecayPowerup()),
	ANTI_GRAVITY(90, "Anti gravity", new AntiGravityPowerup()),
	TUNNEL_BORE(50, "Tunnels down to the bunker", new TunnelBorePowerup()),
	HARVESTER(50, "Harvests plants", new HarvesterPowerup()),
	ANVIL_BOMB(10, "Launches anvils", new AnvilPowerup()),
	ERUPTING_TNT(30, "Tnt that errupts more tnt", new EruptingPowerup()),
	;
	private final int cost;
	private final String description;
	private final PowerupAction action;
	private final EffectFactory effect;

	Skill(int cost, String description, PowerupAction action) {
		this(cost, description, action, new EffectFactory() {
			@Override
			public EfectGenerator getEffect(Skill skill, RodBase rodbase) {
				return new SkillBasedRandomGenerator(skill);
			}
		});
	}

	Skill(int cost, String description, PowerupAction action, EffectFactory effect) {
		this.cost = cost;
		this.description = description;
		this.action = action;
		this.effect = effect;
	}

	public int getCost() {
		return cost;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * builds a new effect for this skill, the result depends on the skill
	 *
	 * @param rodBase
	 * @return
	 */
	public EfectGenerator getEffect(RodBase rodBase) {
		return this.effect.getEffect(this, rodBase);
	}

	/**
	 * fires the skill with the specified actions
	 *
	 * @param player the value of player
	 * @param item the value of item
	 * @param data the value of data
	 * @param plugin the value of plugin
	 * @param roBase the value of roBase
	 * @return the int
	 */
	@Override
	public int doAction(Player player, ItemStack item, RodData data, FerryEmpirePlugin plugin, RodBase roBase) {
		return this.action.doAction(player, item, data, plugin, roBase);
	}

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
	public void onRemoteBlockHit(FerryEmpirePlugin plugin, Player shooter, Location loc, Projectile ent, Random random, RodBase rodbase) {
		assert this.action instanceof RemoteHitPowerupAction;
		((RemoteHitPowerupAction) this.action).onRemoteBlockHit(plugin, shooter, loc, ent, random, rodbase);
	}

	@Override
	public void onRemoteExplode(FerryEmpirePlugin plugin, Location loc, Entity ent, Random random, List<Block> blocks, RodBase rodbase) {
		assert this.action instanceof RemoteExplodePowerupAction;
		((RemoteExplodePowerupAction) this.action).onRemoteExplode(plugin, loc, ent, random, blocks, rodbase);
	}

	@Override
	public void onBlockPlace(Block block) {
		assert this.action instanceof BlockBasedPowerup;
		((BlockBasedPowerup) this.action).onBlockPlace(block);
	}

	/**
	 *
	 *
	 * @param player the value of player
	 * @param remainingPower the value of remainingPower
	 * @param plugin the value of plugin
	 * @param rodBase the value of rodBase
	 * @param data the value of data
	 * @return the int
	 */
	@Override
	public int doInteractiveAction(Player player, int remainingPower, FerryEmpirePlugin plugin, RodBase rodBase, RodData data) {
		assert this.action instanceof ActivePowerupAction;
		return ((ActivePowerupAction) this.action).doInteractiveAction(player, remainingPower, plugin, rodBase, data);
	}

	@Override
	public void loadFromConfig(ConfigurationSection from) {
		this.action.loadFromConfig(from);
	}

	@Override
	public void writeToConfig(ConfigurationSection to) {
		this.action.writeToConfig(to);
	}
}
