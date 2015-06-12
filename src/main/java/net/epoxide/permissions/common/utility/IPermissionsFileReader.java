package net.epoxide.permissions.common.utility;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IPermissionsFileReader 
{
	/**
	 * Read the supplied permissions file and establish ranks based upon these.
	 **/
	public void buildRanksFromFile();
	
	/**
	 * Retrieve Player-Specific Permissions from the permissions file.
	 **/
	public void buildPlayerSpecificPerms();
}
