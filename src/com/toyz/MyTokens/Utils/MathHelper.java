package com.toyz.MyTokens.Utils;

import java.util.Random;

import org.bukkit.Material;

import com.toyz.MyTokens.MyTokens;

public class MathHelper {
	public static Boolean ShouldDropOnKill() {
		Random r = new Random();
		if (r.nextDouble() <= MyTokens.DropConfig.getConfig().getDouble("Drop.kills.percent")) {
			return true;
		}
		return false;
	}

	public static int randInt(int min, int max) {

		// Usually this can be a field rather than a method variable
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
	
	public static int currentTimeMillis() {
	    return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
	}
	
	public static int AmountDroppedFromForture(int fortuneLevel, Material typeOfBlock, int returnValue) {
        if(!(fortuneLevel > 3)) {
            if (typeOfBlock == Material.REDSTONE || typeOfBlock == Material.REDSTONE_ORE || typeOfBlock == Material.REDSTONE_BLOCK || typeOfBlock == Material.CARROT || typeOfBlock == Material.MELON || typeOfBlock == Material.MELON_BLOCK || typeOfBlock == Material.NETHER_WARTS || typeOfBlock == Material.POTATO || typeOfBlock == Material.SEEDS || typeOfBlock == Material.LONG_GRASS) {

                if (typeOfBlock == Material.REDSTONE) {
                    for (int i = 0; i < fortuneLevel; i++) {
                        returnValue++;
                        if (returnValue == 4)
                            break;
                    }
                }
                else if (typeOfBlock == Material.MELON || typeOfBlock == Material.MELON_BLOCK) {
                    for (int i = 0; i < fortuneLevel; i++) {
                        returnValue++;
                        if (returnValue == 9)
                            break;
                    }
                }
                else if (typeOfBlock == Material.LONG_GRASS) {
                    for (int i = 0; i < fortuneLevel; i++)
                        returnValue = returnValue + 2;
                }
                else {
                    for (int i = 0; i < fortuneLevel; i++)
                        returnValue++;
                }
            }
            else {
                Random rand = new Random();
                int result = rand.nextInt(100);
                if (fortuneLevel == 1) {
                    int chance = 33;
                    if (chance > result) {
                        return returnValue * 2;
                    }
                }
                else if (fortuneLevel == 2) {
                    int chance = 50;
                    if (chance > result) {
                        if (result < 25) {
                            returnValue = returnValue * 3;
                        }
                        else {
                            returnValue = returnValue * 2;
                        }
                    }
                }
                else {
                    int chance = 60;
                    if (chance > result) {
                        if (result <= 20) {
                            returnValue = returnValue * 4;
                        }
                        else if (result > 20 && result <= 40) {
                            returnValue = returnValue * 3;
                        }
                        else {
                            returnValue = returnValue * 2;
                        }
                    }
                }
            }
            return returnValue;
        }else {
            Random ran = new Random();
            int j = ran.nextInt(fortuneLevel);

            if(j < 0)
                j = 0;

            return j;
        }
    }
}
