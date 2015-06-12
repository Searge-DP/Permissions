package net.epoxide.permissions.common.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

public class Rank 
{	
	private static final List<Rank> RANKS = new ArrayList<Rank>();
	private static Rank DEFAULT;
	
	private final String rankName;
	private final int permissionsLevel;
	
	public Rank(String rankName, int permLevel)
	{
		this.rankName = rankName;
		this.permissionsLevel = permLevel;
		RANKS.add(this);
		
		if(DEFAULT == null || DEFAULT.permissionsLevel > this.permissionsLevel)
			DEFAULT = this;
	}
	
	public String getRankName()
	{
		return this.rankName;
	}
	
	public int getPermissionsLevel()
	{
		return this.permissionsLevel;
	}
	
	public boolean equals(Object obj)
	{
		if(!(obj instanceof Rank))
			return false;
		Rank rank = (Rank)obj;
		if(rank.rankName.equals(this.rankName) && rank.permissionsLevel == this.permissionsLevel)
			return true;
		return false;
	}

	public static boolean isValidRank(Rank rank) 
	{
		return RANKS.contains(rank);
	}

	public static Rank getDefaultRank() 
	{
		return Rank.DEFAULT;
	}

	public static void setDefault(EntityPlayer entityPlayer) 
	{
		
	}
}
