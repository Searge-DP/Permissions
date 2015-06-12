package net.epoxide.permissions.common;

import net.epoxide.permissions.common.utility.JSONPermissionsFileReader;

public class PermissionsManager 
{
	private static final PermissionsManager INSTANCE = new PermissionsManager();
	
	public static PermissionsManager instance()
	{
		return INSTANCE;
	}

	public void populateManager(JSONPermissionsFileReader file) 
	{
		
	}
}
