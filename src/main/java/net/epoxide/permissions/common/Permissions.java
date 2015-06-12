package net.epoxide.permissions.common;

import java.io.File;

import net.epoxide.permissions.common.ref.Constants;
import net.epoxide.permissions.common.utility.JSONPermissionsFileReader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION)
public class Permissions 
{
	@Instance(Constants.MOD_ID)
	private static Permissions INSTANCE;
		
	@EventHandler
	public void onPreInitialization(FMLPreInitializationEvent event)
	{
		PermissionsManager.instance().populatePermissionsListeners(event.getAsmData());
		JSONPermissionsFileReader file = new JSONPermissionsFileReader(new File("test"));
		PermissionsManager.instance().populateManager(file);
	}
	
	public static Permissions instance()
	{
		return INSTANCE;
	}
}
