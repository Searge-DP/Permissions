package net.epoxide.permissions.common.api;

import java.util.ArrayList;
import java.util.Map;

import net.epoxide.permissions.common.utility.AbstractPermissionsFileReader;
import net.minecraft.command.ICommand;
import net.minecraft.entity.player.EntityPlayer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class PermissionsRegistry 
{
	private static final Logger PERM_REG_LOG = LogManager.getLogger("Permissions Registry");
	private static final PermissionsRegistry INSTANCE = new PermissionsRegistry();
	
	private boolean isAvailable = false;
	private ImmutableMap<ICommand, String> commandPerms;
	private ImmutableMap<Rank, ArrayList<String>> rankPermissions;
	private ImmutableMap<String, ArrayList<String>> playerSpecificPermissions;
	
	/**
	 * Determines whether the player has the specified permission.
	 * 
	 * @return boolean determining if the player has the permission.
	 **/
	public boolean isPlayerPermitted(EntityPlayer entityPlayer, String permission)
	{	
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
		{	
			/**
			 * If player is an OP, server owner, or any other individual that would always be allowed to use all commands, let them have permissions even
			 * if the permissions registry is not available because it has not been populated.
			 **/
			if(FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().func_152596_g(entityPlayer.getGameProfile()))
				return true;
		
			if(!this.isAvailable())
			{
				PermissionsRegistry.PERM_REG_LOG.warn("The Permissions Registry has not been populated and is currently unavailable. Disallowing player's Usage.");
				return false;
			}
			
			if(rankPermissions.get(getRankForPlayer(entityPlayer)).contains(permission) || playerSpecificPermissions.get(entityPlayer.getDisplayName()).contains(permission))
				return true;
		}
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
		String rankTitle = entityPlayer.getEntityData().getString(Rank.NBT_RANK_TITLE);
		int permLevel = entityPlayer.getEntityData().getInteger(Rank.NBT_PERM_LVL_KEY);
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

	public void populate(ImmutableMap<ICommand, String> cmdMap, AbstractPermissionsFileReader fileReader) 
	{
		this.commandPerms = cmdMap;
		this.rankPermissions = fileReader.getRankPermissions();
		this.playerSpecificPermissions = fileReader.getPlayerPermissions();
		this.isAvailable = true;
	}
	
	public void setDefaultValues()
	{
		rankPermissions = ImmutableMap.of(new Rank("Admin", -1), new ArrayList<String>());
	}
	
	public boolean isAvailable()
	{
		return this.isAvailable;
	}

	public String getPermString(ICommand command)
	{
		return commandPerms.get(command);
	}
	
	public static String getPermStringForCommand(ICommand command) 
	{
		return instance().getPermString(command);
	}
}
