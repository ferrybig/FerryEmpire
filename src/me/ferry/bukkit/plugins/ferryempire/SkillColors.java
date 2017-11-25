/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire;

import me.ferry.bukkit.plugins.ferryempire.randomfirework.factory.EffectFactory;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.factory.FreezeEffectFactory;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.factory.StaticEffectFactory;
import me.ferry.bukkit.plugins.ferryempire.randomfirework.factory.TntTrailEffectFactory;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;

/**
 *
 * @author Fernando
 */
public interface SkillColors {

	public static final StaticEffectFactory FIRE_BALL_COLOR = new StaticEffectFactory(new Color[][][]{
		new Color[][]{
			new Color[]{
				Color.RED, Color.YELLOW
			}, new Color[]{
				Color.ORANGE
			}
		},
		new Color[][]{
			new Color[]{
				Color.ORANGE
			}, new Color[]{
				Color.RED, Color.YELLOW
			}
		}
	});

	public static final StaticEffectFactory BURNING_ARROW_COLOR = new StaticEffectFactory(new Color[][][]{
		new Color[][]{
			new Color[]{
				Color.ORANGE, Color.RED, Color.YELLOW
			}, new Color[]{
				Color.ORANGE, Color.SILVER, Color.RED, Color.YELLOW
			}
		},
		new Color[][]{
			new Color[]{
				Color.ORANGE, Color.SILVER, Color.RED, Color.YELLOW
			}, new Color[]{
				Color.RED, Color.YELLOW, Color.ORANGE
			}
		},}, FireworkEffect.Type.BURST);
	public static final StaticEffectFactory GRAB_COLOR = new StaticEffectFactory(new Color[][][]{
		new Color[][]{
			new Color[]{
				Color.GRAY, Color.WHITE
			}, new Color[]{
				Color.SILVER
			}
		},
		new Color[][]{
			new Color[]{
				Color.SILVER
			}, new Color[]{
				Color.GRAY, Color.WHITE
			}
		},});
	public static final StaticEffectFactory ENDER_COLOR = new StaticEffectFactory(new Color[][][]{
		new Color[][]{
			new Color[]{
				Color.BLUE, Color.FUCHSIA
			}, new Color[]{
				Color.OLIVE
			}
		},
		new Color[][]{
			new Color[]{
				Color.OLIVE
			}, new Color[]{
				Color.BLUE, Color.FUCHSIA
			}
		},});
	public static final StaticEffectFactory ENDER_COLOR_BURST = new StaticEffectFactory(new Color[][][]{
		new Color[][]{
			new Color[]{
				Color.BLUE, Color.FUCHSIA
			}, new Color[]{
				Color.OLIVE
			}
		},
		new Color[][]{
			new Color[]{
				Color.OLIVE
			}, new Color[]{
				Color.BLUE, Color.FUCHSIA
			}
		},}, FireworkEffect.Type.BURST);
	public static final StaticEffectFactory POWER_COLOR = new StaticEffectFactory(new Color[][][]{
		new Color[][]{
			new Color[]{
				Color.BLUE, Color.GREEN
			}, new Color[]{
				Color.GREEN, Color.YELLOW
			}
		},
		new Color[][]{
			new Color[]{
				Color.GREEN, Color.YELLOW
			}, new Color[]{
				Color.YELLOW, Color.ORANGE
			}
		},
		new Color[][]{
			new Color[]{
				Color.YELLOW, Color.ORANGE
			}, new Color[]{
				Color.ORANGE, Color.RED
			}
		},
		new Color[][]{
			new Color[]{
				Color.ORANGE, Color.RED
			}, new Color[]{
				Color.RED, Color.PURPLE
			}
		},
		new Color[][]{
			new Color[]{
				Color.RED, Color.PURPLE
			}, new Color[]{
				Color.PURPLE, Color.BLUE
			}
		},
		new Color[][]{
			new Color[]{
				Color.PURPLE, Color.BLUE
			}, new Color[]{
				Color.BLUE, Color.GREEN
			}
		},}, FireworkEffect.Type.BURST);
	public static final StaticEffectFactory STORM_COLOR = new StaticEffectFactory(new Color[][][]{
		new Color[][]{
			new Color[]{
				Color.fromRGB(0x528B8B)
			}, new Color[]{
				Color.PURPLE
			}
		},}, FireworkEffect.Type.BURST);
	public static final StaticEffectFactory SPARK_COLOR = new StaticEffectFactory(new Color[][][]{
		new Color[][]{
			new Color[]{
				Color.YELLOW
			}, new Color[]{
				Color.BLACK
			}
		},}, FireworkEffect.Type.BURST);
	public static final StaticEffectFactory SPARK_WAVE_COLOR = new StaticEffectFactory(new Color[][][]{
		new Color[][]{
			new Color[]{
				Color.RED
			}, new Color[]{
				Color.BLACK
			}
		},}, FireworkEffect.Type.BURST);
	public static final EffectFactory FREEZE = new FreezeEffectFactory();
	public static final EffectFactory BIG_FREEZE = new FreezeEffectFactory();
	public static final EffectFactory TNT_TRAIL = new TntTrailEffectFactory();
	public static final EffectFactory LOVE_WAVE = new StaticEffectFactory(new Color[][][]{
		new Color[][]{
			new Color[]{
				Color.fromRGB(0xff0099),}, new Color[]{
				Color.fromRGB(0xff00ff),}
		},}, FireworkEffect.Type.BURST);
	public static final EffectFactory BLACK_LARGE = new StaticEffectFactory(new Color[][][]{
		new Color[][]{
			new Color[]{
				Color.BLACK,}, new Color[]{
				Color.BLACK,}
		},}, FireworkEffect.Type.BALL_LARGE);
;
}
