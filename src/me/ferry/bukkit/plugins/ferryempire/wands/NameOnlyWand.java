/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire.wands;

import java.util.Collections;
import java.util.List;
import me.ferry.bukkit.plugins.ferryempire.RodBase;
import me.ferry.bukkit.plugins.ferryempire.RodData;
import me.ferry.bukkit.plugins.ferryempire.Skill;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.generator.EfectGenerator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Fernando
 */
public class NameOnlyWand extends RodBase {

	private final String name = "DarkWand";
	private final String color = ChatColor.GRAY.toString();
	private final Skill[] spells = Skill.values(); // Read all spells that are defined in Skill.java
	private final int itemMagicDecoded = 10;// random number
	private final Material material = Material.STICK; // Material
	private final String itemMagic = parseItemMagic(itemMagicDecoded); // THIS IS NEEDED!
	private final ItemStack empireStick = buildItemStack(material, spells[0], this);
	private final Recipe[] recipes = new Recipe[0]; // recipes
	private final boolean maySpawnWithCommand = true;
	private final EfectGenerator override = null; // Hiermee kan je een goede kleur automatich op ALLE spells doen

	/*private final EfectGenerator override = new StaticColorGenerator(new Color[][][]
	 {
	 new Color[][]
	 {
	 new Color[]
	 {
	 Color.RED, Color.YELLOW
	 }, new Color[]
	 {
	 Color.ORANGE
	 }
	 },
	 new Color[][]
	 {
	 new Color[]
	 {
	 Color.ORANGE
	 }, new Color[]
	 {
	 Color.RED, Color.YELLOW
	 }
	 }
	 }, FireworkEffect.Type.BALL); // dit zijn alle kleuren in de plugin */

	@Override
	public Recipe[] getRecipes() {
		return recipes;
	}

	@Override
	public Skill[] getSkills() {
		return spells;
	}

	@Override
	public ItemStack getRod() {
		return this.empireStick;
	}

	@Override
	public int getRodMagicId() {
		return this.itemMagicDecoded;
	}

	@Override
	public boolean maySpawnWithCommand() {
		return this.maySpawnWithCommand;
	}

	@Override
	public EfectGenerator getFireworkOverride(Skill skill) {
		return this.override;
	}

	@Override
	public String getRodName() {
		return this.name;
	}

	@Override
	public String getRodColor() {
		return this.color;
	}

	@Override
	public String getRodMagic() {
		return this.itemMagic;
	}

	@Override
	public boolean hasCooldown() {
		return false;
	}

	@Override
	public boolean hasPower() {
		return false;
	}

	@Override
	public boolean hasDurability() {
		return false;
	}

	@Override
	public int maxPower() {
		assert hasPower();
		return 0;
	}

	@Override
	public int maxDurability() {
		assert hasDurability();
		return 0;
	}

	@Override
	public RodData getRodData(ItemMeta meta) {
		String itemName = meta.getDisplayName();
		assert itemName != null; // should not be null isf this wand is valid
		String magicData = itemName.substring(this.itemMagic.length());
		//System.out.println("read: " + itemName);
		byte[] data = new byte[4];
		int index = 0;
		byte firstSet = 0;
		byte secondSet;
		boolean parseFirst = true;
		char[] c = magicData.toCharArray();
		char cc = ChatColor.COLOR_CHAR;
		for (int i = 0; i < c.length; i++) {
			if (c[i] == cc) {
				i++;
				try {
					//System.out.println("Found token! " + magicData.substring(i, i + 1));
					if (parseFirst) {
						firstSet = Byte.parseByte(magicData.substring(i, i + 1), 16);
					} else {

						secondSet = Byte.parseByte(magicData.substring(i, i + 1), 16);
						data[index] = (byte) (firstSet * 16 + secondSet);
						//System.out.println("data = " + data[index] + "!");
						index++;
					}
					parseFirst = !parseFirst;
				} catch (NumberFormatException err) {
					//System.out.println("Invalid token!");
					break;
				}
			} else {
				//System.out.println("Missing token!");
				break;
			}
		}
		//System.out.println(data[0]);
		//System.out.println(data[1]);
		//System.out.println(data[2]);
		Skill selected = this.spells[data[0]];
		Skill activeSkill;
		if (spells.length > data[1]) {
			activeSkill = this.spells[data[1]];
		} else {
			activeSkill = null;
		}
		int activeSkillTime = data[2];
		return new CustomRodData(selected, activeSkill, activeSkillTime);
	}
	final protected static char[] hexArray
			= {
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
			};

	@Override
	public String getRodNameItem(RodData rodData) {
		StringBuilder build = new StringBuilder();
		build.append(this.getRodMagic());
		char cc = ChatColor.COLOR_CHAR;
		{
			Skill skillPassive = rodData.getSkill();
			int indexPassive = 0;
			for (; indexPassive < this.spells.length;) {
				if (this.spells[indexPassive].equals(skillPassive)) {
					break;
				}
				indexPassive++;
			}
			int v = indexPassive;
			char a = hexArray[v >>> 4];
			char b = hexArray[v & 0x0F];
			build.append(cc).append(a).append(cc).append(b);
		}
		{
			Skill skillActive = rodData.getActiveSkill();
			int indexActive = 0;
			for (; indexActive < this.spells.length;) {
				if (this.spells[indexActive].equals(skillActive)) {
					break;
				}
				indexActive++;
			}
			int v = indexActive;
			char a = hexArray[v >>> 4];
			char b = hexArray[v & 0x0F];
			build.append(cc).append(a).append(cc).append(b);
		}
		{
			int v = rodData.getActiveSkillTime();
			char a = hexArray[v >>> 4];
			char b = hexArray[v & 0x0F];
			build.append(cc).append(a).append(cc).append(b);
		}
		build.append(this.getRodColor()).append(this.getRodName());
		//System.out.println("write: " + build.toString());
		return build.toString();
	}

	private static ItemStack buildItemStack(Material mat, Skill firstSpell, NameOnlyWand main) {
		ItemStack empireStick = new ItemStack(mat);
		RodData data = new CustomRodData(firstSpell, null, 0);
		ItemMeta meta = empireStick.getItemMeta();
		meta.setLore(data.toLore(main));
		meta.setDisplayName(main.getRodNameItem(data));
		empireStick.setItemMeta(meta);
		return empireStick;
	}

	private static String parseItemMagic(int itemMagicDecoded) {
		StringBuilder temp = new StringBuilder();
		temp.append(ChatColor.MAGIC);
		for (char ch : Integer.toHexString(itemMagicDecoded).toCharArray()) {
			temp.append(ChatColor.COLOR_CHAR).append(ch);
		}
		temp.append(ChatColor.RESET);
		return temp.toString();
	}

	private static class CustomRodData extends RodData {

		public CustomRodData(Skill skill, Skill active, int time) {
			super(skill, 0, 0, 0, active, time);
		}

		@Override
		@SuppressWarnings("unchecked")
		public List<String> toLore(RodBase base) {
			return (List<String>) Collections.EMPTY_LIST;
		}
	}
}
