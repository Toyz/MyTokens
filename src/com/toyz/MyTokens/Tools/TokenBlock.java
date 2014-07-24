package com.toyz.MyTokens.Tools;

import java.util.Random;

import org.bukkit.Material;

import com.toyz.MyTokens.Utils.MathHelper;

public class TokenBlock {
	private Material type;
    private double chance;
    private int min;
    private int max;
    
    public TokenBlock(Material type, double chance, int min, int max)
    {
      this.type = type;
      this.chance = chance;
      this.min = min;
      this.max = max;
    }
    
    public Material getType()
    {
      return this.type;
    }
    
    public double getChance()
    {
      return this.chance;
    }
    
    public int minDrop()
    {
      return this.min;
    }
    
    public int maxDrop()
    {
      return this.max;
    }
    
    public boolean shouldDrop()
    {
      Random r = new Random();
      if (r.nextDouble() <= this.chance) {
        return true;
      }
      return false;
    }
    
    public int calculateDropAmount()
    {
      /*Random r = new Random();
      return this.min + r.nextInt(this.max - this.min);*/
    	return MathHelper.randInt(this.min, this.max);
    }
}
