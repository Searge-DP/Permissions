package net.epoxide.permissions.common;

import java.util.HashMap;
import java.util.Map;

import net.epoxide.permissions.common.api.PermissionsRegistry;
import net.epoxide.permissions.common.ref.Constants;
import net.epoxide.permissions.common.utility.AbstractPermissionsFileReader;
import net.minecraft.command.ICommand;
import net.minecraft.command.ServerCommandManager;

import com.google.common.collect.ImmutableMap;

import cpw.mods.fml.common.FMLCommonHandler;

public class PermissionsManager 
{
	private static final PermissionsManager INSTANCE = new PermissionsManager();	
	
	public static PermissionsManager instance()
	{
		return INSTANCE;
	}
	
	public void populateManager(AbstractPermissionsFileReader fileReader) 
	{
		if(fileReader == null)
		{
			Constants.LOGGER.error("Invalid Permissions File Format.");
			PermissionsRegistry.instance().setDefaultValues();
			return;
		}
		
		Map<ICommand, String> cmdPermMap = buildCommandMap();
		fileReader.buildRanksFromFile();
		fileReader.buildPlayerSpecificPerms();
		PermissionsRegistry.instance().populate(ImmutableMap.copyOf(cmdPermMap), fileReader);
	}

	private Map<ICommand, String> buildCommandMap() 
	{
		Map<String, ICommand> commands = ((ServerCommandManager)FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager()).getCommands();
		Map<ICommand, String> permMap = new HashMap<ICommand, String>();
		for(String str : commands.keySet())
		{
			permMap.put(commands.get(str), "permissions." + str);
		}
		return permMap;
	}
}
