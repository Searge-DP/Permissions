package net.epoxide.permissions.common;

import java.lang.reflect.Field;

import net.epoxide.permissions.common.api.PermissionsRegistry;
import net.epoxide.permissions.common.ref.Constants;

import com.google.common.base.Throwables;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;

public class IMCHandler 
{
	@EventHandler
	public void handleIMCMessage(FMLInterModComms.IMCEvent event)
	{
		for(final IMCMessage message : event.getMessages())
		{
			if(message.isStringMessage())
			{
				if(message.key.equalsIgnoreCase("register"))
				{
					String fieldLocation = message.getStringValue();
					int index = fieldLocation.lastIndexOf(".");
					String className = fieldLocation.substring(0, index);
					String fieldName = fieldLocation.substring(index + 1);
					try 
					{
						Field field = Class.forName(className).getDeclaredField(fieldName);
						field.setAccessible(true);
						field.set(null, PermissionsRegistry.instance());
					} 
					catch (NoSuchFieldException e) 
					{
						Constants.LOGGER.error(String.format("The Mod %s supplied an invalid location %s. The field does not exist. Please report this to the mod creator.", message.getSender(), message.getStringValue()));
						Throwables.propagate(e);
					} 
					catch (SecurityException e) 
					{
						Constants.LOGGER.error("The Permissions Mod has encountered an error. Please report this to the mod creator.");
						Throwables.propagate(e);
					} 
					catch (ClassNotFoundException e) 
					{
						Constants.LOGGER.error(String.format("The Mod %s supplied an invalid location %s. The class does not exist. Please report this to the mod creator.", message.getSender(), message.getStringValue()));
						Throwables.propagate(e);
					} 
					catch (IllegalArgumentException e) 
					{
						Constants.LOGGER.error(String.format("The Mod %s supplied an invalid field location %s. This field is of the incorrect Type. The field should be of type PermissionsRegistry. Please report this to the mod creator.", message.getSender(), message.getStringValue()));
						Throwables.propagate(e);
					} 
					catch (IllegalAccessException e) 
					{
						Constants.LOGGER.error("The Permissions Mod has encountered an error. Please report this to the mod creator.");
						Throwables.propagate(e);
					}
				}
			}
		}
	}
}