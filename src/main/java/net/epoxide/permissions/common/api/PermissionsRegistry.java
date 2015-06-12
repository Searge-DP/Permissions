package net.epoxide.permissions.common.api;

import java.util.List;

import net.epoxide.permissions.common.utility.AbstractPermissionsFileReader;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.collect.ImmutableMap;

public class PermissionsRegistry 
{
	private static final PermissionsRegistry INSTANCE = new PermissionsRegistry();
	private static final String NBT_RANK_TITLE = "Rank-Title";
	private static final String NBT_PERM_LVL_KEY = "Permissions-Level";	
	
	private boolean isAvailable = false;
	private ImmutableMap<Rank, List<String>> rankPermissions;
	private ImmutableMap<String, List<String>> playerSpecificPermissions;
	
	/**
	 * Determines whether the player has the specified permission.
	 * 
	 * @return boolean determining if the player has the permission.
	 **/
	public boolean isPlayerPermitted(EntityPlayer entityPlayer, String permission)
	{
		if(rankPermissions.get(getRankForPlayer(entityPlayer)).contains(permission) || playerSpecificPermissions.get(entityPlayer.getDisplayName()).contains(permission))
			return true;
		return false;
	}
	
	/**
	 * Retrieves data distinguishing the player's rank and returns the Rank object.
	 * 
	 * @return The {@link Rank} of the player.
	 **/
	private Rank getRankForPlayer(EntityPlayer entityPlayer) 
	{
		/**
		 * A rank's equality is based upon its title and permission level.
		 * @see Rank#equals(Object obj)
		 * If this is not the case, the player is not in this rank.
		 * If the specified rank does not exist, it will be reset to the default rank (The rank with the lowest permissions level).
		 **/
		String rankTitle = entityPlayer.getEntityData().getString(PermissionsRegistry.NBT_RANK_TITLE);
		int permLevel = entityPlayer.getEntityData().getInteger(PermissionsRegistry.NBT_PERM_LVL_KEY);
		Rank rank = new Rank(rankTitle, permLevel);
		if(!Rank.isValidRank(rank))
		{
			Rank.setDefault(entityPlayer);
			return Rank.getDefaultRank();
		}
		return rank;
	}

	public static PermissionsRegistry instance()
	{
		return INSTANCE;
	}

	public void populate(AbstractPermissionsFileReader fileReader) 
	{
		this.rankPermissions = fileReader.getRankPermissions();
		this.playerSpecificPermissions = fileReader.getPlayerPermissions();
		this.isAvailable = true;
	}
	
	public boolean isAvailable()
	{
		return this.isAvailable;
	}
}
