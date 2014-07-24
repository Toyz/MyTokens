package com.toyz.MyTokens.Utils;

import java.util.Random;

import com.toyz.MyTokens.MyTokens;

public class MathHelper {
	public static Boolean ShouldDropOnKill() {
		Random r = new Random();
		if (r.nextDouble() <= MyTokens._plugin.getConfig().getDouble("Drop.kills.percent")) {
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
}
