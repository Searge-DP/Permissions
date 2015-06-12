package net.epoxide.permissions.common;

import net.epoxide.permissions.common.api.PermissionsRegistry;
import net.epoxide.permissions.common.utility.AbstractPermissionsFileReader;
import cpw.mods.fml.common.discovery.ASMDataTable;

public class PermissionsManager 
{
	private static final PermissionsManager INSTANCE = new PermissionsManager();	
	
	public static PermissionsManager instance()
	{
		return INSTANCE;
	}

	public void distributePermissionsRegistryToListeners(ASMDataTable asmData)
	{
		asmData.getAll("net.epoxide.permissions.common.api.PermissionHandler");
	}
	
	public void populateManager(AbstractPermissionsFileReader fileReader) 
	{
		fileReader.buildRanksFromFile();
		fileReader.buildPlayerSpecificPerms();
		PermissionsRegistry.instance().populate(fileReader);
	}

	public void populatePermissionsListeners(ASMDataTable asmData) 
	{
		
	}
}
