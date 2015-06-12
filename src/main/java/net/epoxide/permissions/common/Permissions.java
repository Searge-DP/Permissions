package net.epoxide.permissions.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.epoxide.permissions.common.api.Rank;
import net.epoxide.permissions.common.ref.Constants;
import net.epoxide.permissions.common.utility.AbstractPermissionsFileReader;
import net.epoxide.permissions.common.utility.JSONPermissionsFileReader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.EntityEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION)
public class Permissions 
{
	@SidedProxy(clientSide = "net.epoxide.permissions.client.ClientProxy", serverSide = "net.epoxide.permissions.common.CommonProxy")
	public static IProxy proxy;
	
	@Instance(Constants.MOD_ID)
	private static Permissions INSTANCE;
	
	private static List<String> chatFormatSections = new ArrayList<String>();
	
	@EventHandler
	public void onPreInitialization(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), "permissions.cfg"));
		config.load();
		String permCfgPath = config.get(Constants.PERMISSIONS_FILE_PATH_CFG, "Permissions-File-Location", "permissions.json").getString();
		config.save();
		AbstractPermissionsFileReader file = getPermissionsFileReader(new File(event.getModConfigurationDirectory(), permCfgPath));
		PermissionsManager.instance().populateManager(file);
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new IMCHandler());
		MinecraftForge.EVENT_BUS.register(new CommandHandler());
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
	
	@SubscribeEvent
	public void onPlayerCreation(EntityEvent.EntityConstructing entityEvent)
	{
		if(entityEvent.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)entityEvent.entity;
			NBTTagCompound tag = player.getEntityData();
			if(!tag.hasKey(Rank.NBT_RANK_TITLE) || !tag.hasKey(Rank.NBT_PERM_LVL_KEY))
				Rank.setDefault(player);
		}
	}
	
	public static Permissions instance()
	{
		return INSTANCE;
	}
}