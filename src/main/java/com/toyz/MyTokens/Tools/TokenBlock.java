package com.toyz.MyTokens.Tools;

import java.util.Random;

import org.bukkit.Material;

import com.toyz.MyTokens.MyTokens;

public class TokenBlock {
	private Material type;
    private double chance;
    private int min;
    private int max;
    private Boolean enabled;
    public TokenBlock(Material type, double chance, int min, int max, Boolean enabled)
    {
      this.type = type;
      this.chance = chance;
      this.min = min;
      this.max = max;
      this.enabled = enabled;
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
    
    public Boolean enabled(){
    	return this.enabled;
    }
    
    public void SetEnabled(Boolean Enabled){
    	this.enabled = Enabled;
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
    	return MyTokens.getAPI().getMathHelper().randInt(this.min, this.max);
    }
}