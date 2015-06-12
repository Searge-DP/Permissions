package net.epoxide.permissions.common;

import net.epoxide.permissions.common.api.PermissionsRegistry;
import net.epoxide.permissions.common.ref.Constants;
import net.epoxide.permissions.common.utility.AbstractPermissionsFileReader;

public class PermissionsManager 
{
	private static final PermissionsManager INSTANCE = new PermissionsManager();	
	
	public static PermissionsManager instance()
	{
		return INSTANCE;
	}

	/* Not Needed if my plan works, but it's here for now
	public void distributePermissionsRegistryToListeners(ASMDataTable asmData)
	{
		Set<ASMData> data = asmData.getAll("net.epoxide.permissions.common.api.PermissionHandler");
		for(ASMData dataEntry : data)
		{
			try 
			{
				Field[] fields = Class.forName(dataEntry.getClassName()).getDeclaredFields();
				for(Field field : fields)
				{
					if(field.isAnnotationPresent(PermissionHandler.class) && field.getType().equals(PermissionsRegistry.class))
					{
						field.setAccessible(true);
						field.set(null, PermissionsRegistry.instance());
					}
				}
			} 
			catch (SecurityException e) 
			{
				e.printStackTrace();
			} 
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
			} 
			catch (IllegalArgumentException e) 
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e) 
			{
				e.printStackTrace();
			}
		}
	}*/
	
	public void populateManager(AbstractPermissionsFileReader fileReader) 
	{
		if(fileReader == null)
		{
			Constants.LOGGER.error("Invalid Permissions File Format.");
			return;
		}
		
		fileReader.buildRanksFromFile();
		fileReader.buildPlayerSpecificPerms();
		PermissionsRegistry.instance().populate(fileReader);
	}
}
