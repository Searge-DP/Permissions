package net.epoxide.permissions.common.utility;

public class Rank 
{	
	private final String rankName;
	private final int permissionsLevel;
	
	public Rank(String rankName, int permLevel)
	{
		this.rankName = rankName;
		this.permissionsLevel = permLevel;
	}
	
	public String getRankName()
	{
		return this.rankName;
	}
	
	public int getPermissionsLevel()
	{
		return this.permissionsLevel;
	}
}
