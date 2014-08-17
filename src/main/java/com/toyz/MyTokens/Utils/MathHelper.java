package com.toyz.MyTokens.Utils;

import java.util.Random;

import org.bukkit.Material;

import com.toyz.MyTokens.MyTokens;

public class MathHelper {
	private static MathHelper instance = new MathHelper();
	private static Random random = new Random();

	/** Gets the singleton instance **/
	public static MathHelper getInstance() {
		return instance;
	}

	public boolean ShouldDropOnKill() {
		return random.nextDouble() <= MyTokens.DropConfig.getConfig().getDouble("Drop.kills.percent");
	}

	public int randInt(int min, int max) {
		return random.nextInt((max - min) + 1) + min;
	}

	public int currentTimeMillis() {
		return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
	}

	public int amountDroppedFromForture(int fortuneLevel, Material material,
			int returnValue) {
		if (fortuneLevel < 3) {
			switch (material) {
			case REDSTONE:
				for (int i = 0; i < fortuneLevel; i++) {
					returnValue++;
					if (returnValue == 4)
						break;
				}
				break;
			case MELON:
			case MELON_BLOCK:
				for (int i = 0; i < fortuneLevel; i++) {
					returnValue++;
					if (returnValue == 9)
						break;
				}
				break;
			case LONG_GRASS:
				for (int i = 0; i < fortuneLevel; i++)
					returnValue = returnValue + 2;
				break;
			default:
				int result = random.nextInt(100);
				if (fortuneLevel == 1) {
					int chance = 33;
					if (chance > result) {
						return returnValue * 2;
					}
				} else if (fortuneLevel == 2) {
					int chance = 50;
					if (chance > result) {
						if (result < 25) {
							returnValue = returnValue * 3;
						} else {
							returnValue = returnValue * 2;
						}
					}
				} else {
					int chance = 60;
					if (chance > result) {
						if (result <= 20) {
							returnValue = returnValue * 4;
						} else if (result > 20 && result <= 40) {
							returnValue = returnValue * 3;
						} else {
							returnValue = returnValue * 2;
						}
					}
				}
				break;
			}
		} else {
			int j = random.nextInt(fortuneLevel);

			if (j < 0)
				j = 0;

			return j;
		}
		return returnValue;
	}
}