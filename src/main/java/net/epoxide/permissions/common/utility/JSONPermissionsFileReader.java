package net.epoxide.permissions.common.utility;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.epoxide.permissions.common.api.Rank;
import net.epoxide.permissions.common.ref.Constants;

import com.google.common.base.Throwables;
import com.google.gson.Gson;

public class JSONPermissionsFileReader extends AbstractPermissionsFileReader 
{
	private final Map<String, Object> jsonFile;
	
	public JSONPermissionsFileReader(String fileName)
	{
		this(new File(fileName));
	}
	
	public JSONPermissionsFileReader(File file) 
	{
		super(file);
		/**
		 * Read the file contents and create its String representation.
		 **/
		String jsonFileStr = null;
		try 
		{
			jsonFileStr = FileUtilities.readFileAsString(this.file);
		} 
		catch (IOException e) 
		{
			Constants.LOGGER.error("Failed to read the file as a String.");
			Constants.LOGGER.error(e.getMessage());
			Throwables.propagate(e);
		}
		/**
		 * Deserialize the String into a Map.
		 **/
		jsonFile = new Gson().fromJson(jsonFileStr, Map.class);
	}

	@Override
	public void buildRanksFromFile() 
	{
		Map<String, Map<String, List<String>>> rankMap = (Map<String, Map<String, List<String>>>) jsonFile.get("ranks");
		for(String rankTitle : rankMap.keySet())
		{
			Map<String, List<String>> rankData = rankMap.get(rankTitle);
			int permLevel = Integer.parseInt(rankData.get("perm-level").get(0));
			Rank rank = new Rank(rankTitle, permLevel);
			this.rankPermissions.put(rank, rankData.get("permissions"));
		}
		
		for(Rank rank : this.rankPermissions.keySet())
		{
			for(Rank otherRank : this.rankPermissions.keySet())
			{
				if(rank.getPermissionsLevel() > otherRank.getPermissionsLevel())
				{
					this.rankPermissions.get(rank).addAll(this.rankPermissions.get(otherRank));
				}
			}
		}
		
		/**
		 * The Administrator rank is automatically created. The -1 permissions level indicates access to every command.
		 **/
		this.rankPermissions.put(new Rank("Admin", -1), null);
	}
	
	@Override
	public void buildPlayerSpecificPerms() 
	{
		Map<String, List<String>> playerData = (Map<String, List<String>>) this.jsonFile.get("players");
		for(String playerName : playerData.keySet())
		{
			List<String> playerPerms = playerData.get(playerName);
			this.playerSpecificPermissions.put(playerName, playerPerms);
		}
	}
}
