package net.epoxide.permissions.common;

import java.io.File;

import net.epoxide.permissions.common.ref.Constants;
import net.epoxide.permissions.common.utility.AbstractPermissionsFileReader;
import net.epoxide.permissions.common.utility.JSONPermissionsFileReader;
import net.minecraftforge.common.config.Configuration;
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
		Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), "permissions.cfg"));
		config.load();
		String permCfgPath = config.get(Constants.PERMISSIONS_FILE_PATH_CFG, "Permissions-File-Location", "permissions.json").getString();
		config.save();
		AbstractPermissionsFileReader file = getPermissionsFileReader(new File(event.getModConfigurationDirectory(), permCfgPath));
		PermissionsManager.instance().populateManager(file);
	}
	
	private AbstractPermissionsFileReader getPermissionsFileReader(File file) 
	{
		if(!file.exists())
			return null;
		if(file.getName().endsWith(".json"))
		{
			try
			{
				return new JSONPermissionsFileReader(file);
			}
			catch(Exception e)
			{
				Constants.LOGGER.error("Invalid Permissions File Location. No permissions file at location: " + file.getAbsolutePath());
				return null;
			}
		}
		return null;
	}

	public static Permissions instance()
	{
		return INSTANCE;
	}
}
